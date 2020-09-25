package com.estate.sdzy.report.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.estate.sdzy.report.entity.BillMonthsReport;

import java.util.List;
import java.util.Map;

public interface BillMonthsReportService extends IService<BillMonthsReport> {

    List<Map<String,Object>> listBillMonthsReport(Map<String,String> map, String token);
}
