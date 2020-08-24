package com.estate.sdzy.tariff.controller;


import com.estate.sdzy.common.controller.BaseController;
import com.estate.sdzy.tariff.service.FCostRuleService;
import com.estate.util.Result;
import com.estate.util.ResultUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import org.springframework.stereotype.Controller;

import javax.servlet.http.HttpServletRequest;

/**
 * <p>
 * 收费标准 前端控制器
 * </p>
 *
 * @author mq
 * @since 2020-08-04
 */
@RestController
@RequestMapping("/sdzy/fCostRule")
public class FCostRuleController extends BaseController {

    @Autowired
    private FCostRuleService costRuleService;

    @GetMapping("/listCostRule")
    public Result listCostRule(HttpServletRequest request,@RequestHeader("Authentication-Token") String token){
        return ResultUtil.success();
    }

    @PostMapping("/insertCostRule")
    public Result insertCostRule(HttpServletRequest request,@RequestHeader("Authentication-Token") String token){
        return ResultUtil.success();
    }

    @PutMapping("/updateCostRule")
    public Result updateCostRule(HttpServletRequest request,@RequestHeader("Authentication-Token") String token){
        return ResultUtil.success();
    }

    @DeleteMapping("/{id}")
    public Result deleteCostRule(@PathVariable("id")Long id,@RequestHeader("Authentication-Token") String token){
        return ResultUtil.success();
    }
}

