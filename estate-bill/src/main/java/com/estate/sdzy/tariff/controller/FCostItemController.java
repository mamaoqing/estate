package com.estate.sdzy.tariff.controller;


import com.estate.sdzy.common.controller.BaseController;
import com.estate.sdzy.tariff.entity.FCostItem;
import com.estate.sdzy.tariff.service.FCostItemService;
import com.estate.util.Result;
import com.estate.util.ResultUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import org.springframework.stereotype.Controller;

import javax.servlet.http.HttpServletRequest;

/**
 * <p>
 * 费用项目 前端控制器
 * </p>
 *
 * @author mq
 * @since 2020-08-04
 */
@RestController
@RequestMapping("/sdzy/fCostItem")
public class FCostItemController extends BaseController {

    @Autowired
    private FCostItemService costItemService;

    @GetMapping("/listCostItem")
    public Result listCostItem(@RequestHeader("Authentication-Token") String token,HttpServletRequest request){
        return ResultUtil.success(costItemService.listCostItem(super.getParameterMap(request),token));
    }

    @PostMapping("/insertCostItem")
    public Result insertCostItem(@RequestHeader("Authentication-Token") String token, @RequestBody FCostItem item){
        return ResultUtil.success(costItemService.save(item,token));
    }

    @PutMapping("/updateCostItem")
    public Result updateCostItem(@RequestHeader("Authentication-Token") String token, @RequestBody FCostItem item){
        return ResultUtil.success(costItemService.saveOrUpdate(item,token));
    }

    @DeleteMapping("/{id}")
    public Result deleteCostItem(@PathVariable("id") Long id,@RequestHeader("Authentication-Token") String token){
        return ResultUtil.success(costItemService.removeById(id,token));
    }

    @GetMapping("/listCostType")
    public Result listCostType(){
        return ResultUtil.success(costItemService.costTypeList());
    }
}

