package com.estate.sdzy.tariff.controller;


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
public class FCostRuleRoomController {

    @Autowired
    private FCostRuleRoomService costRuleRoomService;

    @PostMapping("/insertRoomRule")
    public Result insertRoomRule(@RequestBody Map<String,Object> map, @RequestHeader("Authentication-Token") String token){

        return ResultUtil.success(costRuleRoomService.insertRoomRule(token, map));
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
}

