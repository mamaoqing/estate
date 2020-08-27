package com.estate.sdzy.tariff.controller;


import com.estate.sdzy.common.controller.BaseController;
import com.estate.sdzy.tariff.entity.FCostType;
import com.estate.sdzy.tariff.service.FCostTypeService;
import com.estate.util.Result;
import com.estate.util.ResultUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import org.springframework.stereotype.Controller;

import javax.servlet.http.HttpServletRequest;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author mq
 * @since 2020-08-04
 */
@RestController
@RequestMapping("/sdzy/fCostType")
public class FCostTypeController extends BaseController {


    @Autowired
    private FCostTypeService costTypeService;

    @GetMapping("/listCostType")
    public Result listCostType(HttpServletRequest request){
       return ResultUtil.success(costTypeService.listCostType(super.getParameterMap(request)));
    }
    @PostMapping("/insertCostType")
    public Result insertCostType(@RequestHeader("Authentication-Token") String token, @RequestBody FCostType costType){
        return ResultUtil.success(costTypeService.save(costType,token));
    }
    @PutMapping("/updateCostType")
    public Result updateCostType(@RequestHeader("Authentication-Token") String token, @RequestBody FCostType costType){
        return ResultUtil.success(costTypeService.saveOrUpdate(costType,token));
    }
    @DeleteMapping("/{id}")
    public Result deleteCostType(@PathVariable("id") Long id,@RequestHeader("Authentication-Token") String token){
        return ResultUtil.success(costTypeService.removeById(id,token));
    }
}

