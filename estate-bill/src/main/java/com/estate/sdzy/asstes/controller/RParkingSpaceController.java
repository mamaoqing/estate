package com.estate.sdzy.asstes.controller;


import com.estate.sdzy.asstes.entity.RParkingSpace;
import com.estate.sdzy.asstes.service.RParkingSpaceService;
import com.estate.sdzy.common.controller.BaseController;
import com.estate.util.Result;
import com.estate.util.ResultUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import org.springframework.stereotype.Controller;

import javax.servlet.http.HttpServletRequest;

/**
 * <p>
 * 停车位 前端控制器
 * </p>
 *
 * @author mq
 * @since 2020-08-17
 */
@RestController
@RequestMapping("/sdzy/rParkingSpace")
public class RParkingSpaceController extends BaseController {

    @Autowired
    private RParkingSpaceService parkingSpaceService;

    @GetMapping("/listPark")
    public Result listPark(@RequestHeader("Authentication-Token") String token, HttpServletRequest request){
        return ResultUtil.success(parkingSpaceService.listPark(super.getParameterMap(request),token));
    }
    @PostMapping("/insertPark")
    public Result insertPark(@RequestHeader("Authentication-Token") String token, @RequestBody RParkingSpace parkingSpace){
        return ResultUtil.success(parkingSpaceService.save(token,parkingSpace));
    }
    @PutMapping("/updatePark")
    public Result updatePark(@RequestHeader("Authentication-Token") String token, @RequestBody RParkingSpace parkingSpace){
        return ResultUtil.success(parkingSpaceService.saveOrUpdate(token,parkingSpace));
    }
    @DeleteMapping("/{id}")
    public Result deletePark(@PathVariable("id") Long id ,@RequestHeader("Authentication-Token") String token){
        return ResultUtil.success(parkingSpaceService.removeById(id,token));
    }
}

