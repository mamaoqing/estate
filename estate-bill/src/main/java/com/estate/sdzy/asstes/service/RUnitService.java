package com.estate.sdzy.asstes.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.estate.sdzy.asstes.entity.RUnit;

/**
 * <p>
 * 单元 服务类
 * </p>
 *
 * @author mq
 * @since 2020-07-28
 */
public interface RUnitService extends IService<RUnit> {

    boolean save(RUnit rUnit, String token);
}
