package com.estate.sdzy.tariff.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.estate.sdzy.tariff.entity.FFinanceBillRecord;
import org.apache.ibatis.annotations.Param;

import java.util.List;
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

    List<FFinanceBillRecord> getFinanceBillRecord(@Param("map") Map<String, String> map);

    List<Map<String ,Object>> findMap(@Param("billId") long billId);
}
