package com.estate.sdzy.asstes.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.estate.sdzy.asstes.entity.RBuilding;
import com.estate.sdzy.asstes.entity.RCommunity;
import com.estate.sdzy.asstes.entity.RUnit;

import java.util.List;
import java.util.Map;

/**
 * <p>
 * 楼房 服务类
 * </p>
 *
 * @author mq
 * @since 2020-07-28
 */
public interface RBuildingService extends IService<RBuilding> {

    boolean save(RBuilding rBuilding,String token);
    boolean update(RBuilding rBuilding,String token);
    boolean delete(Long id,String token);
    List<RBuilding> list(Map<String,String> map,Integer pageNo, Integer size,String token);
    Integer listNum(Map<String,String> map,String token);
    List<RCommunity> getUserComm(String token, Long compId);
    String checkBuildingRoomUnit(Long compId);
    String copyBuildings(RBuilding rBuilding,String token);
    List<RBuilding> getList(Long commAreaId,Long commId);
    List<RUnit> getUnitList(Long buildingId);
    String checkBulidingNameNo(RBuilding rBuilding);
    String checkBulidingNameNoCopy(RBuilding rBuilding);
}
