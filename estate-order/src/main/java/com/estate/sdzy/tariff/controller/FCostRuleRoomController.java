package com.estate.sdzy.tariff.controller;


import com.estate.common.controller.BaseController;
import com.estate.common.util.Result;
import com.estate.common.util.ResultUtil;
import com.estate.sdzy.tariff.entity.FCostRuleRoom;
import com.estate.sdzy.tariff.service.FCostRuleRoomService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

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
@RequestMapping("/order/fCostRuleRoom")
public class FCostRuleRoomController extends BaseController {

    @Autowired
    private FCostRuleRoomService costRuleRoomService;

    @PostMapping("/insertRoomRule")
    public Result insertRoomRule(@RequestBody Map<String,Object> map, @RequestHeader("Authentication-Token") String token){

        return ResultUtil.success(costRuleRoomService.insertRoomRule(token, map));
    }
    @PostMapping("/insertRoomPark")
    public Result insertRoomPark(@RequestBody Map<String,String> map, @RequestHeader("Authentication-Token") String token){

        return ResultUtil.success(costRuleRoomService.insertParkRule(token, map));
    }

    @GetMapping("/{ruleId}")
    public Result getRoomIds(@PathVariable("ruleId") Long ruleId){
        return ResultUtil.success(costRuleRoomService.getRoomIds(ruleId));
    }

    @PostMapping("/updateRoomRule")
    public Result updateRoomRule(@RequestHeader("Authentication-Token") String token, FCostRuleRoom ruleRoom){
        return ResultUtil.success();
    }

    @PostMapping("/{id}")
    public Result insertRoomRule(@PathVariable("id") Long id,@RequestHeader("Authentication-Token") String token){
        return ResultUtil.success();
    }

    @GetMapping("/getParkIds/{ruleId}")
    public Result getParkIds(@PathVariable("ruleId") Long ruleId){
        return ResultUtil.success(costRuleRoomService.getParkIds(ruleId));
    }

    @GetMapping("/costPark/{ruleId}")
    public Result costPark(@PathVariable("ruleId") Long ruleId){
        return ResultUtil.success(costRuleRoomService.costPark(ruleId));
    }

    @DeleteMapping("{id}")
    public Result deleteCostRuleRoom(@PathVariable("id") Long id){
        return ResultUtil.success(costRuleRoomService.removeById(id));
    }

    @DeleteMapping("/deleteAllParks")
    public Result deleteAll(@RequestHeader("Authentication-Token") String token,HttpServletRequest request){
        return ResultUtil.success(costRuleRoomService.removeByIds(super.getParameterMap(request),token));
    }
}

