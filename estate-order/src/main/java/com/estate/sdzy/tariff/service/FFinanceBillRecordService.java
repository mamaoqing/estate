package com.estate.sdzy.tariff.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.estate.sdzy.tariff.entity.FFinanceBillRecord;

import java.util.List;
import java.util.Map;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author mq
 * @since 2020-09-08
 */
public interface FFinanceBillRecordService extends IService<FFinanceBillRecord> {

    Page<FFinanceBillRecord> getFinanceBillRecords(Map<String,String> map, Integer pageNo, Integer size);

    List<Map<String,Object>> getFFinanceBillRecord(Long id);
}
