package com.estate.sdzy.tariff.controller;


import com.estate.common.util.Result;
import com.estate.common.util.ResultUtil;
import com.estate.sdzy.tariff.entity.FBillDate;
import com.estate.sdzy.tariff.service.FBillDateService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import org.springframework.stereotype.Controller;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author mq
 * @since 2020-09-02
 */
@RestController
@RequestMapping("/order/fBillDate")
public class FBillDateController {

    @Autowired
    private FBillDateService billDateService;

    @GetMapping("/{ruleId}")
    public Result listBills(@PathVariable("ruleId") Long ruleId){
        return ResultUtil.success(billDateService.listBills(ruleId));
    }

    @PutMapping("/updateBillDate")
    public Result updateBillDate(@RequestBody FBillDate date,@RequestHeader("Authentication-Token") String token){
        return ResultUtil.success(billDateService.saveOrUpdate(date,token));
    }
}

