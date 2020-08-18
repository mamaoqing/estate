package com.estate.sdzy.asstes.service;

import com.estate.sdzy.asstes.entity.RBuilding;
import com.estate.sdzy.asstes.entity.RUnit;
import com.baomidou.mybatisplus.extension.service.IService;
import com.estate.sdzy.asstes.entity.RUnit;
import com.estate.sdzy.system.entity.SUnitModel;

import java.util.List;

/**
 * <p>
 * 单元 服务类
 * </p>
 *
 * @author mq
 * @since 2020-07-28
 */
public interface RUnitService extends IService<RUnit> {

    List<RUnit> getAllUnit(String token);
    List<RBuilding> getAllBuilding(Long areaId,String token);
    List<SUnitModel> getAllModel(String token);
    boolean insert(RUnit unit,String token);
    boolean save(RUnit rUnit, String token);
}
