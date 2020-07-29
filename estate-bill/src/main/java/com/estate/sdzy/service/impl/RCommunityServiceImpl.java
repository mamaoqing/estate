package com.estate.sdzy.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.estate.sdzy.entity.*;
import com.estate.sdzy.mapper.*;
import com.estate.sdzy.service.RCommunityService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
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
        if (null == user) {
            return false;
        }
        community.setCreatedBy(user.getId());
        community.setCreatedName(user.getUserName());

        int insert = communityMapper.insert(community);
        if (insert > 0) {
            log.info("社区信息添加成功，添加人={}", user.getUserName());
        }
        return insert > 0;
    }

    @Override
    public boolean saveOrUpdate(RCommunity community, String token) {
        SUser user = getUserByToken(token);
        if (null == user) {
            return false;
        }
        community.setCreatedBy(user.getId());
        community.setCreatedName(user.getUserName());

        int update = communityMapper.updateById(community);
        if (update > 0) {
            log.info("社区信息修改成功，修改人={}", user.getUserName());
        }
        return update > 0;
    }

    @Override
    public boolean removeById(Long id, String token) {
        SUser user = getUserByToken(token);
        if (null == user) {
            return false;
        }
        int delete = communityMapper.deleteById(id);
        if (delete > 0) {
            log.info("社区信息删除成功，删除人={}", user.getUserName());
        }
        return delete > 0;
    }

    @Override
    public List<RCommunity> getUserCommunity(List<Long> list) {
        List<RCommunity> communityList = new ArrayList<>();
        for (Long l : list) {
            // 查询社区
            RCommunity community = communityMapper.selectById(l);
            QueryWrapper<RCommArea> areaQueryWrapper = new QueryWrapper<>();
            areaQueryWrapper.eq("comm_id", community.getId());
            // 查询社区下的分区，比如南区北区
            List<RCommArea> commAreas = commAreaMapper.selectList(areaQueryWrapper);
            List<RCommArea> commAreaList = new ArrayList<>();
            for (RCommArea x : commAreas) {
                // 查询分区下的楼栋信息，#1，#2，#3，#4
                QueryWrapper<RBuilding> buildQueryWrapper = new QueryWrapper<>();
                buildQueryWrapper.eq("comm_area_id", x.getId());
                List<RBuilding> rBuildings = buildingMapper.selectList(buildQueryWrapper);
                List<RBuilding> buildingList = new ArrayList<>();
                for (RBuilding b : rBuildings) {
                    QueryWrapper<RUnit> unitQueryWrapper = new QueryWrapper<>();
                    unitQueryWrapper.eq("building_id", b.getId());
                    // 根据楼栋id查询单元 一单元，二单元
                    List<RUnit> rUnits = unitMapper.selectList(unitQueryWrapper);
                    List<RUnit> unitList = new ArrayList<>();
                    for (RUnit r : rUnits) {
                        QueryWrapper<RRoom> rRoomQueryWrapper = new QueryWrapper<>();
                        rRoomQueryWrapper.eq("unit_id", r.getId());
                        // 根据单元信息查询所有房间
                        List<RRoom> rRooms = rRoomMapper.selectList(rRoomQueryWrapper);
                        r.setRoomList(rRooms);
                        unitList.add(r);
                    }

                    b.setUnitList(unitList);
                    buildingList.add(b);
                }
//                QueryWrapper<RUnit> unitQueryWrapper = new QueryWrapper<>();
//                unitQueryWrapper.eq("building_id",building.getId());
//                // 查询单元信息
//                List<RUnit> rUnits = unitMapper.selectList(unitQueryWrapper);
//                building.setUnitList(rUnits);
                x.setBuildingList(buildingList);
                commAreaList.add(x);
            }

            community.setCommAreaList(commAreaList);
            communityList.add(community);
        }

        return communityList;
    }

    @Override
    public Page<RRoom> getRoomByMap(Map<String, String> map) {
        if (StringUtils.isEmpty(map.get("pageNo"))) {
            log.error("参数错误，请输入页码");
            return null;
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


    private SUser getUserByToken(String token) {
        Object o = redisTemplate.opsForValue().get(token);
        if (null == o) {
            log.error("登录失效，请重新登录。");
            return null;
        }
        return (SUser) o;
    }
}
