package com.estate.sdzy.report.controller;


import com.estate.common.controller.BaseController;
import com.estate.common.util.Result;
import com.estate.common.util.ResultUtil;
import com.estate.sdzy.report.service.FinanceMonthsReportService;
import com.estate.sdzy.system.service.SUserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;

/**
 * <p>
 * 字典表 前端控制器
 * </p>
 *
 * @author mq
 * @since 2020-07-23
 */
@RestController
@RequestMapping("/sdzy/financeMonthsReport")
@Slf4j
public class FinanceMonthsReportController extends BaseController {

    @Autowired
    public FinanceMonthsReportService financeMonthsReportService;

    @Autowired
    private SUserService userService;

    @PostMapping("/listFinanceRecord")
    public Result listFinanceRecord(@RequestBody Map<String,String> map, HttpServletRequest request, @RequestHeader("Authentication-Token") String token) {
        return ResultUtil.success(financeMonthsReportService.listFinanceMonthsReport(map,token));

    }
}

