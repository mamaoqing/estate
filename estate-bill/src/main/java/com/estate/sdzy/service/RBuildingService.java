package com.estate.sdzy.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.estate.sdzy.entity.RBuilding;
import com.estate.sdzy.entity.RCommunity;

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
    List<RBuilding> list(Map<String,String> map,Integer pageNo, Integer size);
    Integer listNum(Map<String,String> map);
    List<RCommunity> getUserComm(String token, Long compId);
    String checkBuildingRoomUnit(Long compId);
}
