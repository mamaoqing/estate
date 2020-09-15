package com.estate.sdzy.tariff.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.estate.sdzy.tariff.entity.FFinanceRecord;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * <p>
 * 财务流水 Mapper 接口
 * </p>
 *
 * @author mq
 * @since 2020-09-08
 */
public interface FFinanceRecordMapper extends BaseMapper<FFinanceRecord> {
    List<Long> getOwnerPropId(@Param("ownerId") Long ownerId);

    Page<FFinanceRecord> getFinanceRecords(Page<FFinanceRecord> page,@Param("map") Map<String, String> map);
}
