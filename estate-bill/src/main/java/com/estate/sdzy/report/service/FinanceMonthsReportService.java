package com.estate.sdzy.report.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.estate.sdzy.report.entity.FinanceMonthsReport;

import java.util.List;
import java.util.Map;

public interface FinanceMonthsReportService  extends IService<FinanceMonthsReport> {

    List<FinanceMonthsReport> listFinanceMonthsReport(Map<String,String> map, String token);
}
