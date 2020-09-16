package com.estate.sdzy.tariff.controller;


import com.estate.common.controller.BaseController;
import com.estate.common.util.Result;
import com.estate.common.util.ResultUtil;
import com.estate.sdzy.tariff.service.FFinanceBillRecordService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author mq
 * @since 2020-09-08
 */
@RestController
@RequestMapping("/order/fFinanceBillRecord")
public class FFinanceBillRecordController extends BaseController {

    @Autowired
    public FFinanceBillRecordService financeBillRecordService;

    @GetMapping("/listFinanceBillRecord")
    public Result listFinanceBillRecord(Integer pageNo, Integer size, HttpServletRequest request){
        return ResultUtil.success(financeBillRecordService.getFinanceBillRecords(super.getParameterMap(request),pageNo,size));
    }

    @GetMapping("/{billId}")
    public Result listFinanceBillRecords(@PathVariable("billId") Long billId){
        return ResultUtil.success(financeBillRecordService.getFFinanceBillRecord(billId));
    }
}

