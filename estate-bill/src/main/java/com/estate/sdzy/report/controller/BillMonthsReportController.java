package com.estate.sdzy.report.controller;


import com.estate.common.controller.BaseController;
import com.estate.common.util.Result;
import com.estate.common.util.ResultUtil;
import com.estate.sdzy.report.service.BillMonthsReportService;
import com.estate.sdzy.system.service.SUserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

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
@RequestMapping("/sdzy/billMonthsReport")
@Slf4j
public class BillMonthsReportController extends BaseController {

    @Autowired
    public BillMonthsReportService billMonthsReportService;

    @Autowired
    private SUserService userService;

    @PostMapping("/listBillRecord")
    public Result listBillRecord(@RequestBody Map<String,String> map, @RequestHeader("Authentication-Token") String token) {
        return ResultUtil.success(billMonthsReportService.listBillMonthsReport(map,token));

    }
}

