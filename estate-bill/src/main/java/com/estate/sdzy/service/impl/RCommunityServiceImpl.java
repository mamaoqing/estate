package com.estate.sdzy.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.estate.exception.BillException;
import com.estate.sdzy.entity.*;
import com.estate.sdzy.mapper.*;
import com.estate.sdzy.service.RCommunityService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.estate.util.BillExceptionEnum;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
public class RCommunityServiceImpl extends ServiceImpl<RCommunityMapper, RCommunity> implements RCommunityService {

    @Autowired
    private RCommunityMapper communityMapper;
    @Autowired
    private RedisTemplate<String, Object> redisTemplate;

    @Autowired
    private RCommAreaMapper commAreaMapper;
    @Autowired
    private RBuildingMapper buildingMapper;

    @Autowired
    private RUnitMapper unitMapper;
    @Autowired
    private RRoomMapper rRoomMapper;

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
        } else {
            throw new BillException(BillExceptionEnum.SYSTEM_INSERT_ERROR);
        }
        return insert > 0;
    }

    @Override
    public boolean saveOrUpdate(RCommunity community, String token) {
        SUser user = getUserByToken(token);
        if (null == community) {
            throw new BillException(BillExceptionEnum.PARAMS_MISS_ERROR);
        }
        community.setCreatedBy(user.getId());
        community.setCreatedName(user.getUserName());

        int update = communityMapper.updateById(community);
        if (update > 0) {
            log.info("社区信息修改成功，修改人={}", user.getUserName());
        } else {
            throw new BillException(BillExceptionEnum.SYSTEM_UPDATE_ERROR);
        }
        return update > 0;
    }

    @Override
    public boolean removeById(Long id, String token) {
        SUser user = getUserByToken(token);
        if (null == id) {
            throw new BillException(BillExceptionEnum.PARAMS_MISS_ERROR);
        }
        int delete = communityMapper.deleteById(id);
        if (delete > 0) {
            log.info("社区信息删除成功，删除人={}", user.getUserName());
        } else {
            throw new BillException(BillExceptionEnum.SYSTEM_DELETE_ERROR);
        }
        return delete > 0;
    }

    @Override
    public List<Object> getUserCommunity(List<Long> list) {
        List<Object> communityList = new ArrayList<>();
        for (Long l : list) {
            // 查询社区的map
            Map<String, Object> map = communityMapper.communityMap(l);
            if (map.isEmpty()) {
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
//            RCommunity community = communityMapper.selectById(l);
//            QueryWrapper<RCommArea> areaQueryWrapper = new QueryWrapper<>();
//            areaQueryWrapper.eq("comm_id", community.getId());
//            // 查询社区下的分区，比如南区北区
//            List<RCommArea> commAreas = commAreaMapper.selectList(areaQueryWrapper);
//            List<RCommArea> commAreaList = new ArrayList<>();
//            for (RCommArea x : commAreas) {
//                // 查询分区下的楼栋信息，#1，#2，#3，#4
//                QueryWrapper<RBuilding> buildQueryWrapper = new QueryWrapper<>();
//                buildQueryWrapper.eq("comm_area_id", x.getId());
//                List<RBuilding> rBuildings = buildingMapper.selectList(buildQueryWrapper);
//                List<RBuilding> buildingList = new ArrayList<>();
//                for (RBuilding b : rBuildings) {
//                    QueryWrapper<RUnit> unitQueryWrapper = new QueryWrapper<>();
//                    unitQueryWrapper.eq("building_id", b.getId());
//                    // 根据楼栋id查询单元 一单元，二单元
//                    List<RUnit> rUnits = unitMapper.selectList(unitQueryWrapper);
//                    List<RUnit> unitList = new ArrayList<>();
//                    for (RUnit r : rUnits) {
//                        QueryWrapper<RRoom> rRoomQueryWrapper = new QueryWrapper<>();
//                        rRoomQueryWrapper.eq("unit_id", r.getId());
//                        // 根据单元信息查询所有房间
//                        List<RRoom> rRooms = rRoomMapper.selectList(rRoomQueryWrapper);
//                        r.setChildList(rRooms);
//                        unitList.add(r);
//                    }
//
//                    b.setChildList(unitList);
//                    buildingList.add(b);
//                }
////                QueryWrapper<RUnit> unitQueryWrapper = new QueryWrapper<>();
////                unitQueryWrapper.eq("building_id",building.getId());
////                // 查询单元信息
////                List<RUnit> rUnits = unitMapper.selectList(unitQueryWrapper);
////                building.setUnitList(rUnits);
//                x.setChildList(buildingList);
//                commAreaList.add(x);
//            }
//
//            community.setChildList(commAreaList);
//            communityList.add(community);
        }

        /**
         * name id type childList
         */

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
        if (!StringUtils.isEmpty(map.get("commId"))) {
            roomQueryWrapper.eq("comm_id", map.get("commId"));
        }
        if (!StringUtils.isEmpty(map.get("commAreaId"))) {
            roomQueryWrapper.eq("comm_area_id", map.get("commId"));
        }
        if (!StringUtils.isEmpty(map.get("buildingId"))) {
            roomQueryWrapper.eq("building_id", map.get("commId"));
        }
        if (!StringUtils.isEmpty(map.get("unitId"))) {
            roomQueryWrapper.eq("unit_id", map.get("commId"));
        }
        if (!StringUtils.isEmpty(map.get("roomNo"))) {
            roomQueryWrapper.eq("room_no", map.get("roomNo"));
        }
        Page<RRoom> page = new Page<>(pageNo, size);
//        rRoomMapper.selectPage();
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
        // 如果不是超级管理员，只能查看自己公司的社区。
        if (!"超级管理员".equals(user.getType())) {
            queryWrapper.eq("comp_id", user.getCompId());
        } else {
            // 物业公司
            queryWrapper.in("comp_id", new ArrayList<>());
        }
        // 省
        queryWrapper.eq(!StringUtils.isEmpty(map.get("")), "province", map.get(""));
        // 市
        queryWrapper.eq(!StringUtils.isEmpty(map.get("")), "city", map.get(""));
        // 县
        queryWrapper.eq(!StringUtils.isEmpty(map.get("")), "district", map.get(""));
        // 社区名称
        queryWrapper.like(!StringUtils.isEmpty(map.get("")), "name", map.get(""));

        // 用途类型
        queryWrapper.eq("usable_type", map.get(""));
        Page<RCommunity> page = new Page<>(pageNo, size);

        return communityMapper.selectPage(page, queryWrapper);
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
