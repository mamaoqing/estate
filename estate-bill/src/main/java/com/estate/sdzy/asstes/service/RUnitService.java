package com.estate.sdzy.asstes.service;

import com.estate.common.util.Result;
import com.estate.sdzy.asstes.entity.RBuilding;
import com.estate.sdzy.asstes.entity.RUnit;
import com.baomidou.mybatisplus.extension.service.IService;
import com.estate.sdzy.system.entity.SUnitModel;

import java.util.List;
import java.util.Map;

/**
 * <p>
 * 单元 服务类
 * </p>
 *
 * @author mq
 * @since 2020-07-28
 */
public interface RUnitService extends IService<RUnit> {

    List<RUnit> getAllUnit(Map map);
    List<RBuilding> getAllBuilding(Long areaId,String token);
    List<SUnitModel> getAllModel(String token);
    boolean insert(RUnit unit,String token);
    boolean copyUnit(RUnit unit,Long oldId,String token);
    boolean save(RUnit rUnit, String token);
    Result PlAddRoom(Map map, String token);
    boolean delete(Long id, String token);
    boolean update(RUnit unit, String token);
    Integer getPageTotal(Map map);
}
