package com.estate.sdzy.tariff.mapper;

import com.estate.sdzy.tariff.entity.FFinanceRecord;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
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
}
