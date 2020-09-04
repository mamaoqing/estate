package com.estate.sdzy.tariff.controller;


import com.estate.common.controller.BaseController;
import com.estate.common.util.Result;
import com.estate.common.util.ResultUtil;
import com.estate.sdzy.tariff.service.FBillService;
import com.estate.sdzy.tariff.service.impl.FBillServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import org.springframework.stereotype.Controller;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author mq
 * @since 2020-08-27
 */
@RestController
@RequestMapping("/order/fBill")
public class FBillController  extends BaseController {

    @Autowired
    private FBillService billService;

    @GetMapping("/listBill")
    public Result listBill(HttpServletRequest request,@RequestHeader("Authentication-Token") String token){
        return ResultUtil.success(billService.listBill(super.getParameterMap(request),token));
    }

    @PostMapping("/resetBillAll")
    public Result resetBillAll(@RequestBody Map<String,Object> map, @RequestHeader("Authentication-Token") String token){
        return ResultUtil.success(billService.resetBillAll(map));
    }

    @PostMapping("/resetBill/{id}")
    public Result resetBill(@PathVariable("id") Long id,@RequestHeader("Authentication-Token") String token){
        return ResultUtil.success(billService.resetBill(id));
    }
}

