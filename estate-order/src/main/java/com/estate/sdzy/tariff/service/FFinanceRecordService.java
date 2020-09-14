package com.estate.sdzy.tariff.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.estate.sdzy.tariff.entity.FFinanceRecord;

/**
 * <p>
 * 财务流水 服务类
 * </p>
 *
 * @author mq
 * @since 2020-09-08
 */
public interface FFinanceRecordService extends IService<FFinanceRecord> {

    boolean save(FFinanceRecord financeRecord,String token);
}
