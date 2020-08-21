package com.estate.sdzy.asstes.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.estate.exception.BillException;
import com.estate.sdzy.asstes.entity.RBuilding;
import com.estate.sdzy.asstes.entity.RRoom;
import com.estate.sdzy.asstes.entity.RUnit;
import com.estate.sdzy.asstes.mapper.RRoomMapper;
import com.estate.sdzy.asstes.mapper.RUnitMapper;
import com.estate.sdzy.asstes.service.RRoomService;
import com.estate.sdzy.asstes.service.RUnitService;
import com.estate.sdzy.system.entity.SUnitModel;
import com.estate.sdzy.system.entity.SUser;
import com.estate.sdzy.system.mapper.SUnitModelMapper;
import com.estate.util.BillExceptionEnum;
import com.estate.util.Result;
import com.estate.util.ResultUtil;
import lombok.extern.slf4j.Slf4j;
import com.estate.sdzy.asstes.mapper.RBuildingMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * 单元 服务实现类
 * </p>
 *
 * @author mq
 * @since 2020-07-28
 */
@Slf4j
@Service
public class RUnitServiceImpl extends ServiceImpl<RUnitMapper, RUnit> implements RUnitService {

    @Autowired
    private RUnitMapper mapper;
    @Autowired
    private RBuildingMapper buildingMapper;
    @Autowired
    private RedisTemplate<String, Object> redisTemplate;
    @Autowired
    private SUnitModelMapper modelMapper;
    @Autowired
    private RRoomMapper roomMapper;
    @Autowired
    private RRoomService roomService;
    @Override
    public List<RUnit> getAllUnit(Map map) {
        return mapper.getAllUnit(map);
    }

    @Override
    public List<RBuilding> getAllBuilding(Long areaId,String token) {
        getUserByToken(token);
        QueryWrapper<RBuilding> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("comm_area_id",areaId);
        return buildingMapper.selectList(queryWrapper);
    }

    @Override
    public List<SUnitModel> getAllModel(String token) {
        getUserByToken(token);
        QueryWrapper<SUnitModel> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("is_delete",0);
        return modelMapper.selectList(queryWrapper);
    }

    private SUser getUserByToken(String token) {
        Object o = redisTemplate.opsForValue().get(token);
        if (null == o) {
            log.error("登录失效，请重新登录。");
            throw new BillException(BillExceptionEnum.LOGIN_TIME_OUT);
        }
        return (SUser) o;
    }
    @Override
    public boolean insert(RUnit unit,String token) {
        getUserByToken(token);
        SUser user = getUserByToken(token);
        if (null == unit) {
            throw new BillException(BillExceptionEnum.PARAMS_MISS_ERROR);
        }
        unit.setCreatedBy(user.getId());
        unit.setCreatedName(user.getUserName());
        int insert = mapper.insert(unit);
        if (insert > 0) {
            log.info("单元信息添加成功，创建人={}", user.getUserName());
            return true;
        }
        throw new BillException(BillExceptionEnum.SYSTEM_UPDATE_ERROR);
    }

    @Override
    public boolean copyUnit(RUnit unit,Long oldId ,String token) {
        getUserByToken(token);
        SUser user = getUserByToken(token);
        if (null == unit) {
            throw new BillException(BillExceptionEnum.PARAMS_MISS_ERROR);
        }
        unit.setCreatedBy(user.getId());
        unit.setCreatedName(user.getUserName());
        int insert = mapper.insert(unit);
        if (insert > 0) {
            log.info("单元信息添加成功，创建人={}", user.getUserName());
            QueryWrapper<RRoom> queryWrapper = new QueryWrapper<>();
            queryWrapper.eq("unit_id",oldId);
            List<RRoom> rooms = roomMapper.selectList(queryWrapper);
            if (rooms.size()>0){
                for (int i =0;i<rooms.size();i++){
                    rooms.get(i).setId(null);
                    rooms.get(i).setUnitId(unit.getId());

                }
                boolean b = roomService.saveBatch(rooms);
                if (b) {
                    log.info("房间添加成功，添加人={}", user.getUserName());
                } else {
                    throw new BillException(BillExceptionEnum.SYSTEM_INSERT_ERROR);
                }
            }

            return true;
        }
        throw new BillException(BillExceptionEnum.SYSTEM_UPDATE_ERROR);
    }

    @Override
    public boolean delete(Long id,String token) {
        getUserByToken(token);
        SUser user = getUserByToken(token);
        if (null == id) {
            throw new BillException(BillExceptionEnum.PARAMS_MISS_ERROR);
        }
        QueryWrapper<RUnit> unitW = new QueryWrapper<>();
        unitW.eq("id",id);
        RUnit unit = new RUnit();
        unit.setIsDelete(1);
        int delete = mapper.update(unit,unitW);
        QueryWrapper<RRoom> queryWrapper = new QueryWrapper<>();
        RRoom room = new RRoom();
        room.setIsDelete(1);
        queryWrapper.eq("unit_id",id);
        roomMapper.update(room,queryWrapper);
        if (delete > 0) {
            log.info("单元信息删除成功，删除人={}", user.getUserName());
            return true;
        }
        throw new BillException(BillExceptionEnum.SYSTEM_UPDATE_ERROR);
    }

    @Override
    public boolean update(RUnit unit, String token) {
        getUserByToken(token);
        SUser user = getUserByToken(token);
        if (null == unit) {
            throw new BillException(BillExceptionEnum.PARAMS_MISS_ERROR);
        }

        unit.setModifiedBy(user.getId());
        unit.setModifiedName(user.getUserName());
        int update = mapper.updateById(unit);
        if (update > 0) {
            log.info("单元信息修改成功，修改人={}", user.getUserName());
            return true;
        }
        throw new BillException(BillExceptionEnum.SYSTEM_UPDATE_ERROR);
    }

    @Override
    public Integer getPageTotal(Map map) {
        return mapper.getPageTotal(map);
    }


    @Override
    public boolean save(RUnit rUnit, String token) {
        SUser user = getUserByToken(token);
        if (null == rUnit) {
            throw new BillException(BillExceptionEnum.PARAMS_MISS_ERROR);
        }
        rUnit.setCreatedBy(user.getId());
        rUnit.setCreatedName(user.getUserName());
        rUnit.setModifiedBy(user.getId());
        rUnit.setModifiedName(user.getUserName());
        int insert = mapper.insert(rUnit);
        if (insert > 0) {
            log.info("建筑添加成功，添加人={}", user.getUserName());
        } else {
            throw new BillException(BillExceptionEnum.SYSTEM_INSERT_ERROR);
        }
        return insert > 0;
    }

    @Override
    public Result PlAddRoom(Map map, String token) {
        SUser user = getUserByToken(token);
        if (null == map) {
            throw new BillException(BillExceptionEnum.PARAMS_MISS_ERROR);
        }
        int start = Integer.parseInt(map.get("start").toString());
        int end = Integer.parseInt(map.get("end").toString());
        List<RRoom> rooms = new ArrayList<>();
        for (int i=start;i<=end;i++){

            RRoom room = new RRoom();
            room.setCreatedBy(user.getId());
            room.setCreatedName(user.getUserName());
            room.setModifiedBy(user.getId());
            room.setModifiedName(user.getUserName());
            room.setCompId(Long.parseLong(map.get("compId").toString()));
            room.setFloor(i);
            room.setCommId(Long.parseLong(map.get("commId").toString()));
            room.setCommAreaId(Long.parseLong(map.get("commAreaId").toString()));
            room.setBuildingId(Long.parseLong(map.get("buildId").toString()));
            room.setUnitId(Long.parseLong(map.get("unitId").toString()));
            room.setUsableArea(BigDecimal.valueOf(Long.parseLong(map.get("usableArea").toString())));
            room.setBuildingArea(BigDecimal.valueOf(Long.parseLong(map.get("buildingArea").toString())));
            room.setState(map.get("state").toString());
            room.setRoomModel(map.get("roomModelName").toString());
            room.setUsable(map.get("usableName").toString());
            room.setState(map.get("state").toString());
            room.setName(map.get("suffix").toString());
            StringBuffer roomNo = new StringBuffer();
            if(!StringUtils.isEmpty(map.get("prefix"))){
                roomNo.append(map.get("prefix"));
            }
            roomNo.append(i);
            if(!StringUtils.isEmpty(map.get("separator"))){
                roomNo.append(map.get("separator"));
            }
            roomNo.append(map.get("suffix"));
            room.setRoomNo(roomNo.toString());
            QueryWrapper<RRoom> queryWrapper = new QueryWrapper<>();
            queryWrapper.eq("unit_id",room.getUnitId());
            queryWrapper.eq("floor",i);
            queryWrapper.eq("room_no",roomNo.toString());
            List<RRoom> rRooms = roomMapper.selectList(queryWrapper);
            if (rRooms.size()>0){
                return ResultUtil.error(i+"层"+roomNo.toString()+"已存在",1);
            }
            rooms.add(room);
        }
        boolean b = roomService.saveBatch(rooms);
        if (b) {
            log.info("房间添加成功，添加人={}", user.getUserName());
        } else {
            throw new BillException(BillExceptionEnum.SYSTEM_INSERT_ERROR);
        }
        return ResultUtil.success();
    }

}
