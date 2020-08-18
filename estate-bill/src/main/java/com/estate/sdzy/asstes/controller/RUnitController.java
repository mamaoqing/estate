package com.estate.sdzy.asstes.controller;


import com.estate.sdzy.asstes.entity.RUnit;
import com.estate.sdzy.asstes.service.RUnitService;
import com.estate.sdzy.asstes.service.impl.RUnitServiceImpl;
import com.estate.sdzy.system.entity.SUser;
import com.estate.util.Result;
import com.estate.util.ResultUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import org.springframework.stereotype.Controller;

import java.util.HashMap;
import java.util.Map;

/**
 * <p>
 * 单元 前端控制器
 * </p>
 *
 * @author mq
 * @since 2020-07-28
 */
@RestController
@RequestMapping("/sdzy/rUnit")
public class RUnitController {

    @Autowired
    private RUnitService unitService;

    @RequestMapping("/getAllUnit")
    public Result getAll(@RequestParam("pageNo") Long pageNo, @RequestParam("size") Long size, @RequestHeader("Authentication-Token") String token) {
        return ResultUtil.success(unitService.getAllUnit(token));
    }

    @RequestMapping("/getBuilding/{areaId}")
    public Result getBuilding(@PathVariable Long areaId, @RequestHeader("Authentication-Token") String token) {
        return ResultUtil.success(unitService.getAllBuilding(areaId, token));
    }

    @GetMapping("/getUnitModel")
    public Result getUnitModel(@RequestHeader("Authentication-Token") String token) {
        return ResultUtil.success(unitService.getAllModel(token));
    }

    @PostMapping("/addUnit")
    public Result addUnit(@RequestBody RUnit unit, @RequestHeader("Authentication-Token") String token) {
        return ResultUtil.success(unitService.insert(unit, token));
    }

}

