package com.estate.sdzy.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.estate.exception.BillException;
import com.estate.sdzy.entity.*;
import com.estate.sdzy.mapper.*;
import com.estate.sdzy.service.RCommunityService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.estate.util.BillExceptionEnum;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.util.*;

/**
 * <p>
 * 社区表 服务实现类
 * </p>
 *
 * @author mq
 * @since 2020-07-28
 */
@Service
@Slf4j
@RequiredArgsConstructor(onConstructor = @_(@Autowired))
public class RCommunityServiceImpl extends ServiceImpl<RCommunityMapper, RCommunity> implements RCommunityService {

    private final RCommunityMapper communityMapper;
    private final RedisTemplate<String, Object> redisTemplate;

    private final RCommAreaMapper commAreaMapper;
    private final RBuildingMapper buildingMapper;

    private final RUnitMapper unitMapper;
    private final RRoomMapper rRoomMapper;

    private final SUserCommMapper userCommMapper;

    @Override
    public boolean save(RCommunity community, String token) {
        SUser user = getUserByToken(token);
        if (null == community) {
            throw new BillException(BillExceptionEnum.PARAMS_MISS_ERROR);
        }
        community.setCreatedBy(user.getId());
        community.setCreatedName(user.getUserName());

        int insert = communityMapper.insert(community);
        if (insert > 0) {
            log.info("社区信息添加成功，添加人={}", user.getUserName());
            return true;
        }
        throw new BillException(BillExceptionEnum.SYSTEM_INSERT_ERROR);
    }

    @Override
    public boolean saveOrUpdate(RCommunity community, String token) {
        SUser user = getUserByToken(token);
        if (null == community) {
            throw new BillException(BillExceptionEnum.PARAMS_MISS_ERROR);
        }
        community.setCreatedBy(user.getId());
        community.setCreatedName(user.getUserName());
        community.setModifiedBy(user.getId());
        community.setModifiedName(user.getUserName());
        int update = communityMapper.updateById(community);
        if (update > 0) {
            log.info("社区信息修改成功，修改人={}", user.getUserName());
            return true;
        }
        throw new BillException(BillExceptionEnum.SYSTEM_UPDATE_ERROR);
    }

    @Override
    @Transactional
    public boolean removeById(Long id, String token) {
        SUser user = getUserByToken(token);
        if (null == id) {
            throw new BillException(BillExceptionEnum.PARAMS_MISS_ERROR);
        }
        int delete = communityMapper.deleteById(id);
        if (delete > 0) {
            // 删除社区之后，将社区分区，楼栋信息，单元信息，房间信息都逻辑删除
            QueryWrapper queryWrapper = new QueryWrapper<>();
            queryWrapper.eq("comm_id",id);
            commAreaMapper.delete(queryWrapper);
            buildingMapper.delete(queryWrapper);
            unitMapper.delete(queryWrapper);
            rRoomMapper.delete(queryWrapper);
            userCommMapper.delete(queryWrapper);

            log.info("社区信息删除成功，删除人={}", user.getUserName());
            return true;
        }
        throw new BillException(BillExceptionEnum.SYSTEM_DELETE_ERROR);

    }

    @Override
    public List<Object> getUserCommunity(List<Long> list) {
        List<Object> communityList = new ArrayList<>();
        for (Long l : list) {
            // 查询社区的map
            Map<String, Object> map = communityMapper.communityMap(l);
            if (null == map) {
                System.out.println(l);
                return null;
            }
            // 查询社区下的分区map
            List<Map<String, Object>> areaMapList = commAreaMapper.listCommAreaMap((Long) map.get("id"));
            List<Map<String, Object>> areaMaps = new ArrayList<>();
            for (Map<String, Object> areaMap : areaMapList) {
                // 查询社区分区下的楼栋的信息
                List<Map<String, Object>> buildMapList = buildingMapper.listBuildMap((Long) areaMap.get("id"));
                List<Map<String, Object>> buildMaps = new ArrayList<>();
                for (Map<String, Object> buildMap : buildMapList) {
                    // 查询楼栋下面单元信息
                    List<Map<String, Object>> unitMapList = unitMapper.listUnitMap((Long) buildMap.get("id"));
                    List<Map<String, Object>> unitMaps = new ArrayList<>();
                    for (Map<String, Object> unitMap : unitMapList) {
                        // 查询单元下的房间信息
                        List<Map<String, Object>> roomMapList = rRoomMapper.listRoomMap((Long) unitMap.get("id"));
                        unitMap.put("childList", roomMapList);
                        unitMaps.add(unitMap);
                    }
                    buildMap.put("childList", unitMaps);
                    buildMaps.add(buildMap);
                }
                // 将楼栋信息存放进上级map
                areaMap.put("childList", buildMaps);
                areaMaps.add(areaMap);
            }

            // 将社区分区集合放入社区map
            map.put("childList", areaMaps);
            communityList.add(map);
        }
        System.out.println(communityList);

        return communityList;
    }

    @Override
    public Page<RRoom> getRoomByMap(Map<String, String> map) {
        if (StringUtils.isEmpty(map.get("pageNo"))) {
            log.error("参数错误，请输入页码");
            throw new BillException(BillExceptionEnum.PARAMS_MISS_ERROR);
        }
        Integer pageNo = Integer.valueOf(map.get("pageNo"));
        Integer size = StringUtils.isEmpty(map.get("size")) ? 10 : Integer.valueOf(map.get("size"));

        QueryWrapper<RRoom> roomQueryWrapper = new QueryWrapper<>();
        roomQueryWrapper.eq(!StringUtils.isEmpty(map.get("commId")), "comm_id", map.get("commId"));
        roomQueryWrapper.eq(!StringUtils.isEmpty(map.get("commAreaId")), "comm_area_id", map.get("commId"));
        roomQueryWrapper.eq(!StringUtils.isEmpty(map.get("buildingId")), "building_id", map.get("commId"));
        roomQueryWrapper.eq(!StringUtils.isEmpty(map.get("unitId")), "unit_id", map.get("commId"));
        roomQueryWrapper.eq(!StringUtils.isEmpty(map.get("roomNo")), "room_no", map.get("roomNo"));

        Page<RRoom> page = new Page<>(pageNo, size);
        Page<RRoom> rRoomPage = rRoomMapper.selectPage(page, roomQueryWrapper);

        return rRoomPage;
    }

    @Override
    public Page<RCommunity> listCommunity(String token, Map<String, String> map) {
        Integer pageNo;
        Integer size;
        if (StringUtils.isEmpty(map.get("pageNo"))) {
            throw new BillException(BillExceptionEnum.PAGENO_MISS_ERROR);
        }
        pageNo = Integer.valueOf(map.get("pageNo"));
        size = StringUtils.isEmpty(map.get("size")) ? 10 : Integer.valueOf(map.get("size"));
        SUser user = getUserByToken(token);
        log.info("当前角色为->{}", user.getType());
        QueryWrapper<RCommunity> queryWrapper = new QueryWrapper<>();
        // 如果不是超级管理员，只能查看自己公司的社区。并且只能是自己有权限的社区。
        if (!"超级管理员".equals(user.getType())) {
            queryWrapper.eq("comp_id", user.getCompId());
            // 添加只能查看存在权限的社区条件
            queryWrapper.eq("is_delete", 0);
//            queryWrapper.in("id",userCommMapper.commIds(user.getId()));
            queryWrapper.inSql("id","select  c.comm_id from s_user_comm c where r_community_company.id=c.comm_id and c.user_id= "+user.getId());
        } else {
            // 物业公司
//            queryWrapper.in("comp_id", new ArrayList<>());
            // 删除状态
            queryWrapper.eq(StringUtils.isEmpty(map.get("isDelete")), "is_delete", 0);
            queryWrapper.eq(!StringUtils.isEmpty(map.get("isDelete")), "is_delete", map.get("isDelete"));
            queryWrapper.eq(!StringUtils.isEmpty(map.get("compId")),"comp_id", map.get("compId"));
        }
        // 省
        queryWrapper.eq(!StringUtils.isEmpty(map.get("provinceId")), "province_id", map.get("provinceId"));
        // 市
        queryWrapper.eq(!StringUtils.isEmpty(map.get("cityId")), "city_id", map.get("cityId"));
        // 县
        queryWrapper.eq(!StringUtils.isEmpty(map.get("districtId")), "district_id", map.get("districtId"));
        // 社区名称
        queryWrapper.like(!StringUtils.isEmpty(map.get("name")), "name", map.get("name"));

        // 用途类型
        queryWrapper.eq(!StringUtils.isEmpty(map.get("usableType")), "usable_type", map.get("usableType"));
        Page<RCommunity> page = new Page<>(pageNo, size);

        Page<RCommunity> rCommunityPage = communityMapper.listCommunity(page, queryWrapper);
        return rCommunityPage;
    }

    @Override
    public List<RCommunity> getUsersComm(String token) {
        return communityMapper.getUsersComm(getUserByToken(token).getId());
    }

    @Override
    public List<RCommunity> getAllComm(String token) {
        SUser user = getUserByToken(token);
        if (!"超级管理员".equals(user.getType())) {
            return communityMapper.getUsersComm(getUserByToken(token).getId());
        }
        return communityMapper.selectList(null);
    }

    private SUser getUserByToken(String token) {
        Object o = redisTemplate.opsForValue().get(token);
        if (null == o) {
            log.error("登录失效，请重新登录。");
            throw new BillException(BillExceptionEnum.LOGIN_TIME_OUT);
        }
        return (SUser) o;
    }
}
