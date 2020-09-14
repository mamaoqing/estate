package com.estate.sdzy.tariff.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.estate.sdzy.tariff.entity.FAccountCostItem;

/**
 * <p>
 * 账户消费标准 服务类
 * </p>
 *
 * @author mzc
 * @since 2020-09-10
 */
public interface FAccountCostItemService extends IService<FAccountCostItem> {

    boolean save(FAccountCostItem item,String token);
}
