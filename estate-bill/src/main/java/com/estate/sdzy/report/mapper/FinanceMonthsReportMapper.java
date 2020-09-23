package com.estate.sdzy.report.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.estate.sdzy.report.entity.FinanceMonthsReport;
import org.apache.ibatis.annotations.Param;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * 楼房 Mapper 接口
 * </p>
 *
 * @author mq
 * @since 2020-07-28
 */
public interface FinanceMonthsReportMapper extends BaseMapper<FinanceMonthsReport> {

    List<Map<String,Object>> selectRule(@Param("commId") Long commId);

    //本月总收费额
    BigDecimal getTotalPay(@Param("firstDate") Date firstDate,@Param("lastDate") Date lastDate,@Param("commId") Long commId);
    //本月总收费额(按不同的收费标准)
    Map<String,Object> getTotalPayByRule(@Param("firstDate") Date firstDate, @Param("lastDate") Date lastDate, @Param("commId") Long commId,@Param("ruleId") Long ruleId);

    //本月应收额
    BigDecimal getReceivable(@Param("firstDate") Date firstDate,@Param("lastDate") Date lastDate,@Param("commId") Long commId);
    //本月应收额(按不同的收费标准)
    Map<String,Object> getReceivableByRule(@Param("firstDate") Date firstDate, @Param("lastDate") Date lastDate, @Param("commId") Long commId,@Param("ruleId") Long ruleId);

    //本月欠费额
    BigDecimal getOwed(@Param("firstDate") Date firstDate,@Param("lastDate") Date lastDate,@Param("commId") Long commId);
    //本月欠费额(按不同的收费标准)
    Map<String,Object> getOwedByRule(@Param("firstDate") Date firstDate, @Param("lastDate") Date lastDate, @Param("commId") Long commId,@Param("ruleId") Long ruleId);

    //本月收本月金额
    BigDecimal getReceived(@Param("firstDate") Date firstDate,@Param("lastDate") Date lastDate,@Param("commId") Long commId);
    //本月收本月金额(按不同的收费标准)
    Map<String,Object> getReceivedByRule(@Param("firstDate") Date firstDate, @Param("lastDate") Date lastDate, @Param("commId") Long commId,@Param("ruleId") Long ruleId);

    //本月收往期金额
    BigDecimal getPreviousPeriodReceived(@Param("firstDate") Date firstDate,@Param("lastDate") Date lastDate,@Param("commId") Long commId);
    //本月收往期金额(按不同的收费标准)
    Map<String,Object> getPreviousPeriodReceivedByRule(@Param("firstDate") Date firstDate, @Param("lastDate") Date lastDate, @Param("commId") Long commId,@Param("ruleId") Long ruleId);

    //往期欠费金额
    BigDecimal getPreviousOwed(@Param("firstDate") Date firstDate,@Param("lastDate") Date lastDate,@Param("commId") Long commId);
    //往期欠费金额(按不同的收费标准)
    Map<String,Object> getPreviousOwedByRule(@Param("firstDate") Date firstDate, @Param("lastDate") Date lastDate, @Param("commId") Long commId,@Param("ruleId") Long ruleId);
}
