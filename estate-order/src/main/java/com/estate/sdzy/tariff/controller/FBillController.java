package com.estate.sdzy.tariff.controller;


import com.estate.common.controller.BaseController;
import com.estate.common.util.Result;
import com.estate.common.util.ResultUtil;
import com.estate.sdzy.tariff.service.FBillService;
import com.estate.sdzy.tariff.service.impl.FBillServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

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
}

