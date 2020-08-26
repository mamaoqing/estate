package com.estate.sdzy.asstes.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.estate.exception.BillException;
import com.estate.sdzy.asstes.entity.RBuilding;
import com.estate.sdzy.asstes.entity.RCommunity;
import com.estate.sdzy.asstes.entity.RRoom;
import com.estate.sdzy.asstes.entity.RUnit;
import com.estate.sdzy.asstes.mapper.RBuildingMapper;
import com.estate.sdzy.asstes.mapper.RCommunityMapper;
import com.estate.sdzy.asstes.mapper.RRoomMapper;
import com.estate.sdzy.asstes.mapper.RUnitMapper;
import com.estate.sdzy.asstes.service.RBuildingService;
import com.estate.sdzy.asstes.service.RRoomService;
import com.estate.sdzy.asstes.service.RUnitService;
import com.estate.sdzy.system.entity.SUser;
import com.estate.sdzy.system.service.SUserCommService;
import com.estate.util.BillExceptionEnum;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * 楼房 服务实现类
 * </p>
 *
 * @author mq
 * @since 2020-07-28
 */
@Service
@Slf4j
public class RBuildingServiceImpl extends ServiceImpl<RBuildingMapper, RBuilding> implements RBuildingService {

    @Autowired
    private RBuildingMapper rBuildingMapper;

    @Autowired
    private SUserCommService sUserCommService;

    @Autowired
    private RCommunityMapper rCommunityMapper;

    @Autowired
    private RRoomMapper rRoomMapper;

    @Autowired
    private RUnitMapper rUnitMapper;

    @Autowired
    private RUnitService rUnitService;

    @Autowired
    private RRoomService rRoomService;

    @Autowired
    private RedisTemplate<String, Object> redisTemplate;

    @Override
    public List<RCommunity> getUserComm(String token, Long compId){
        SUser user = getUserByToken(token);
        if(user.getCompId()==0){//开发公司获取全部的物业公司下的社区.
            QueryWrapper<RCommunity> queryWrapper= new QueryWrapper<>();
            queryWrapper.eq("comp_id",compId);
            List<RCommunity> rCommunities = rCommunityMapper.selectList(queryWrapper);
            return rCommunities;
        }else{//获取有数据权限的社区列表
            return sUserCommService.getUserComm(user.getId(),compId);
        }

    }

    @Override
    public String checkBuildingRoomUnit(Long buildingId) {
        //查询该建筑下的房间和单元信息，如果有提示用户
        List<RRoom> rRooms = getRooms(buildingId);
        String message = "";
        if(rRooms.size()>0){
            message += "该建筑下有"+rRooms.size()+"条房间信息，";
        }
        List<RUnit> rUnits = getUnits(buildingId);
        if(rUnits.size()>0){
            if(StringUtils.isEmpty(message)){
                message += "该建筑下有"+rUnits.size()+"条单元信息,";
            }else{
                message += "还有"+rUnits.size()+"条单元信息,";
            }
        }
        if(!StringUtils.isEmpty(message)){
            if(rRooms.size()>0&&rUnits.size()>0){
                message += "点击删除将同时删除单元和房间信息，是否继续删除？";
            }else if(rRooms.size()>0){
                message += "点击删除将同时删除房间信息，是否继续删除？";
            }else if(rUnits.size()>0){
                message += "点击删除将同时删除单元信息，是否继续删除？";
            }

        }
        return message;
    }

    @Override
    @Transactional
    public String copyBuildings(RBuilding rBuilding, String token) {
        SUser user = getUserByToken(token);
        RBuilding rBuildingCopy = new RBuilding();
        BeanUtils.copyProperties(rBuilding,rBuildingCopy);
        boolean save = save(rBuildingCopy, token);
        if(save){

            //查询原unit并复制
            List<RUnit> rUnits = rUnitMapper.selectUnitByBuildingId(rBuilding.getId(),rBuildingCopy.getId());
            for(RUnit rUnit:rUnits){
                rUnit.setId(null);
                rUnitService.save(rUnit);
            }
            //#{item.id}
            //rBuildingMapper.insertUnitCopy(user.getId(), user.getUserName(), rUnits1,rBuildingCopy.getId());
            if(rUnits.size()>0)rBuildingMapper.insertRoomCopy(user.getId(), user.getUserName(), rUnits,rBuildingCopy.getId());
        }else{
            return "建筑复制失败";
        }
        return "建筑复制成功";
    }

    @Override
    public List<RBuilding> getList(Long commAreaId,Long commId) {
        QueryWrapper<RBuilding> queryBuilding = new QueryWrapper<>();
        queryBuilding.eq("is_delete",0);
        if(!StringUtils.isEmpty(commAreaId))queryBuilding.eq("comm_area_id",commAreaId);
        if(!StringUtils.isEmpty(commId))queryBuilding.eq("comm_id",commId);
        List<RBuilding> rBuildings = rBuildingMapper.selectList(queryBuilding);
        return rBuildings;
    }

    @Override
    public List<RUnit> getUnitList(Long buildingId) {
        QueryWrapper<RUnit> queryUnit = new QueryWrapper<>();
        queryUnit.eq("is_delete",0);
        queryUnit.eq("building_id",buildingId);
        List<RUnit> rRUnits = rUnitMapper.selectList(queryUnit);
        return rRUnits;
    }

    public List<RRoom> getRooms(Long buildingId){
        QueryWrapper<RRoom> queryroom= new QueryWrapper<>();
        queryroom.eq("building_id",buildingId);
        queryroom.eq("is_delete",0);
        List<RRoom> rRooms = rRoomMapper.selectList(queryroom);
        return rRooms;
    }

    public List<RUnit> getUnits(Long buildingId){
        QueryWrapper<RUnit> queryunit= new QueryWrapper<>();
        queryunit.eq("building_id",buildingId);
        queryunit.eq("is_delete",0);
        List<RUnit> rUnits = rUnitMapper.selectList(queryunit);
        return rUnits;
    }

    @Override
    public boolean save(RBuilding rBuilding, String token) {
        SUser user = getUserByToken(token);
        if (null == rBuilding) {
            throw new BillException(BillExceptionEnum.PARAMS_MISS_ERROR);
        }
        rBuilding.setCreatedBy(user.getId());
        rBuilding.setCreatedName(user.getUserName());
        rBuilding.setModifiedBy(user.getId());
        rBuilding.setModifiedName(user.getUserName());
        int insert = rBuildingMapper.insert(rBuilding);
        if (insert > 0) {
            log.info("建筑添加成功，添加人={}", user.getUserName());
        } else {
            throw new BillException(BillExceptionEnum.SYSTEM_INSERT_ERROR);
        }
        return insert > 0;
    }

    public String checkBulidingNameNo(RBuilding rBuilding){
        rBuilding.getName();
        rBuilding.getNo();
        Integer checkName = rBuildingMapper.checkName(rBuilding.getName(),rBuilding.getCommAreaId());
        Integer checkNo = rBuildingMapper.checkNo(rBuilding.getNo(),rBuilding.getCommAreaId());
        if(checkName>0&&checkNo>0){
            return "建筑名称和编号重复";
        }else if(checkName>0){
            return "建筑名称重复";
        }else if(checkNo>0){
            return "建筑编号重复";
        }else{
            return "";
        }
    }

    @Override
    public boolean update(RBuilding rBuilding, String token) {
        SUser user = getUserByToken(token);
        if (null == rBuilding) {
            throw new BillException(BillExceptionEnum.PARAMS_MISS_ERROR);
        }
        rBuilding.setModifiedBy(user.getId());
        rBuilding.setModifiedName(user.getUserName());
        int update = rBuildingMapper.updateById(rBuilding);
        if (update > 0) {
            log.info("建筑修改成功，修改人={}", user.getUserName());
        } else {
            throw new BillException(BillExceptionEnum.SYSTEM_UPDATE_ERROR);
        }
        return update > 0;
    }

    private boolean isNum(String str) {
        try {
            new BigDecimal(str);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    @Override
    @Transactional
    public boolean delete(Long id, String token) {
        SUser user = getUserByToken(token);
        if(StringUtils.isEmpty(id)){
            throw new BillException(BillExceptionEnum.PARAMS_MISS_ERROR);
        }
        RBuilding rBuilding = rBuildingMapper.selectById(id);
        rBuilding.setModifiedBy(user.getId());
        rBuilding.setModifiedName(user.getUserName());
        rBuilding.setIsDelete(1);
        QueryWrapper<RBuilding> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("id",id);
        int delete = rBuildingMapper.update(rBuilding,queryWrapper);
        //同时删除单元和房间信息
        List<RRoom> rRooms = getRooms(id);
        for(RRoom room:rRooms){
            QueryWrapper<RRoom> queryRoom = new QueryWrapper<>();
            queryRoom.eq("id",room.getId());
            room.setModifiedBy(user.getId());
            room.setModifiedName(user.getUserName());
            room.setIsDelete(1);
            rRoomMapper.update(room,queryRoom);
        }
        List<RUnit> rUnits = getUnits(id);
        for(RUnit unit:rUnits){
            QueryWrapper<RUnit> queryUnit = new QueryWrapper<>();
            queryUnit.eq("id",unit.getId());
            unit.setModifiedBy(user.getId());
            unit.setModifiedName(user.getUserName());
            unit.setIsDelete(1);
            rUnitMapper.update(unit,queryUnit);
        }
        //int delete = sDictMapper.deleteById(id);
        if(delete>0){
            log.info("建筑删除成功，删除人={}",user.getUserName());
        }else{
            throw new BillException(BillExceptionEnum.SYSTEM_DELETE_ERROR);
        }
        return delete>0;
    }

    @Override
    public List<RBuilding> list(Map<String, String> map, Integer pageNo, Integer size,String token) {
        if(StringUtils.isEmpty(pageNo)){
            throw new BillException(BillExceptionEnum.PARAMS_MISS_ERROR);
        }
        if(StringUtils.isEmpty(size)){
            size = 10;
        }
        SUser user = getUserByToken(token);
        if(user.getCompId()==0){
            List<RBuilding> listBuilding = rBuildingMapper.getListBuilding(map.get("name"),map.get("no"),map.get("type"),map.get("compName"),map.get("commName"),map.get("commAreaName") ,(pageNo-1)*size,size,null);
            return listBuilding;
        }else{
            List<RBuilding> listBuilding = rBuildingMapper.getListBuilding(map.get("name"),map.get("no"),map.get("type"),map.get("compName"),map.get("commName"),map.get("commAreaName") ,(pageNo-1)*size,size,user.getId());
            return listBuilding;
        }
    }

    public Integer listNum(Map<String, String> map,String token) {
        SUser user = getUserByToken(token);
        if(user.getCompId()==0){
            Integer list = rBuildingMapper.getListBuildingNum(map.get("name"),map.get("no"),map.get("type"),map.get("compName"),map.get("commName"),map.get("commAreaName") ,null,null,null);
            return list;
        }else{
            Integer list = rBuildingMapper.getListBuildingNum(map.get("name"),map.get("no"),map.get("type"),map.get("compName"),map.get("commName"),map.get("commAreaName") ,null,null,user.getId());
            return list;
        }
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
