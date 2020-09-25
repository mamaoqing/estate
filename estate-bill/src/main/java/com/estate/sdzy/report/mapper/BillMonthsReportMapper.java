package com.estate.sdzy.report.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.estate.sdzy.report.entity.BillMonthsReport;
import org.apache.ibatis.annotations.Param;

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
public interface BillMonthsReportMapper extends BaseMapper<BillMonthsReport> {

    //本月合计
    Map<String,Object> selectBillMonthsReport(@Param("firstDate") Date firstDate,@Param("lastDate") Date lastDate,@Param("commId") Long commId, @Param("ruleId") String ruleId);
    //本月合计(按不同的收费标准)
    List<Map<String,Object>> getBillMonthsReportGbRule(@Param("firstDate") Date firstDate, @Param("lastDate") Date lastDate, @Param("commId") Long commId, @Param("ruleId") String ruleId);
}
