package com.estate.sdzy.tariff.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.estate.sdzy.tariff.entity.FFinanceBillRecord;
import org.apache.ibatis.annotations.Param;

import java.util.Map;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author mq
 * @since 2020-09-08
 */
public interface FFinanceBillRecordMapper extends BaseMapper<FFinanceBillRecord> {

    Page<FFinanceBillRecord> getFinanceBillRecord(Page<FFinanceBillRecord> page, @Param("map") Map<String, String> map);
}
