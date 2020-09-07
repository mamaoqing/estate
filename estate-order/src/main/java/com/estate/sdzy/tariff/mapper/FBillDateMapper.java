package com.estate.sdzy.tariff.mapper;

import com.estate.sdzy.tariff.entity.FBillDate;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author mq
 * @since 2020-09-02
 */
public interface FBillDateMapper extends BaseMapper<FBillDate> {

    FBillDate billDateByRuleId(@Param("ruleId") Long ruleId);
}
