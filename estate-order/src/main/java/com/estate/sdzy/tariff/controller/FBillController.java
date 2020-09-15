package com.estate.sdzy.tariff.controller;


import com.estate.common.controller.BaseController;
import com.estate.common.util.Result;
import com.estate.common.util.ResultUtil;
import com.estate.sdzy.tariff.entity.FBill;
import com.estate.sdzy.tariff.service.FBillService;
import com.estate.sdzy.tariff.service.impl.FBillServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import org.springframework.stereotype.Controller;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;

/**
 * <p>
 * 前端控制器
 * </p>
 *
 * @author mq
 * @since 2020-08-27
 */
@RestController
@RequestMapping("/order/fBill")
public class FBillController extends BaseController {

    @Autowired
    private FBillService billService;

    @GetMapping("/listBill")
    public Result listBill(HttpServletRequest request, @RequestHeader("Authentication-Token") String token) {
        return ResultUtil.success(billService.listBill(super.getParameterMap(request), token));
    }

    @GetMapping("/listBillNoPage")
    public Result listBillNoPage(HttpServletRequest request, @RequestHeader("Authentication-Token") String token) {
        return ResultUtil.success(billService.listBillNoPage(super.getParameterMap(request), token));
    }

    @PostMapping("/resetBillAll")
    public Result resetBillAll(@RequestBody Map<String,Object> map, @RequestHeader("Authentication-Token") String token){
        return ResultUtil.success(billService.resetBillAll(map,token));
    }

    @PostMapping("/resetBill/{id}")
    public Result resetBill(@PathVariable("id") Long id,@RequestHeader("Authentication-Token") String token){
        return ResultUtil.success(billService.resetBill(id));
    }

    @PostMapping("/addBill")
    public Result addBill(@RequestBody FBill bill, @RequestHeader("Authentication-Token") String token) {
        return ResultUtil.success(billService.addBill(bill, token));
    }

    @GetMapping("/listOwner")
    public Result listOwner(@RequestHeader("Authentication-Token") String token){
        return ResultUtil.success(billService.listOwner(token));
    }

    @PostMapping("/doPay")
    public Result doPay(@RequestBody Map<String,Object> map,@RequestHeader("Authentication-Token") String token){
        return ResultUtil.success(billService.doPay(map, token));
    }

    @PostMapping("/getOwners")
    public Result getOwners(@RequestBody Map<String,Object> map){
        return ResultUtil.success(billService.getOwners(map));
    }

    @PostMapping("/insertBill")
    public Result insertBill(@RequestBody FBill bill,@RequestHeader("Authentication-Token") String token){
        return ResultUtil.success(billService.save(bill,token));
    }
}

