package com.estate.sdzy.tariff.controller;


import com.estate.common.controller.BaseController;
import com.estate.common.util.Result;
import com.estate.common.util.ResultUtil;
import com.estate.sdzy.tariff.entity.FCostRule;
import com.estate.sdzy.tariff.service.FCostItemService;
import com.estate.sdzy.tariff.service.FCostRuleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

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
@RequestMapping("/order/fCostRule")
public class FCostRuleController extends BaseController {

    @Autowired
    private FCostRuleService costRuleService;
    @Autowired
    private FCostItemService costItemService;

    @GetMapping("/listCostRule")
    public Result listCostRule(HttpServletRequest request, @RequestHeader("Authentication-Token") String token){
        return ResultUtil.success(costRuleService.listCostRule(super.getParameterMap(request),token));
    }

    @PostMapping("/insertCostRule")
    public Result insertCostRule(@RequestBody FCostRule rule, @RequestHeader("Authentication-Token") String token){
        return ResultUtil.success(costRuleService.save(rule,token));
    }

    @PutMapping("/updateCostRule")
    public Result updateCostRule(@RequestBody FCostRule rule,@RequestHeader("Authentication-Token") String token){
        return ResultUtil.success(costRuleService.saveOrUpdate(rule,token));
    }

    @DeleteMapping("/{id}")
    public Result deleteCostRule(@PathVariable("id")Long id,@RequestHeader("Authentication-Token") String token){
        return ResultUtil.success(costRuleService.removeById(id,token));
    }

    @GetMapping("/costItemList/{compId}")
    public Result costItemList(@PathVariable("compId") Long compId){
        return ResultUtil.success(costItemService.costItemList(compId));
    }
}

