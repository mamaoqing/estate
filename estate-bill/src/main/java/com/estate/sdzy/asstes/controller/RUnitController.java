package com.estate.sdzy.asstes.controller;


import com.estate.sdzy.asstes.entity.RUnit;
import com.estate.sdzy.asstes.service.RUnitService;
import com.estate.sdzy.asstes.service.impl.RUnitServiceImpl;
import com.estate.sdzy.system.entity.SUser;
import com.estate.util.RedisUtil;
import com.estate.util.Result;
import com.estate.util.ResultUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import org.springframework.stereotype.Controller;

import javax.servlet.http.HttpServletRequest;
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
    @Autowired
    private RedisUtil redisUtil;

    @RequestMapping("/getAllUnit")
    public Result getAll(@RequestParam("commId") Long commId,@RequestParam("pageNo") Long pageNo,HttpServletRequest request, @RequestParam("size") Long size, @RequestHeader("Authentication-Token") String token) {
        SUser user = (SUser) redisUtil.get(token);
        Map<String,Object> map = new HashMap<>();
        Map<String,Object> dataMap = new HashMap<>();
        if(!StringUtils.isEmpty(commId)){
            map.put("commId",commId);
        }
        if(!StringUtils.isEmpty(request.getParameter("commAreaId"))){
            map.put("commAreaId",request.getParameter("commAreaId"));
        }
        if(!StringUtils.isEmpty(request.getParameter("buildingId"))){
            map.put("buildingId",request.getParameter("buildingId"));
        }
        if(!StringUtils.isEmpty(request.getParameter("no"))){
            map.put("no",request.getParameter("no"));
        }
        if(!StringUtils.isEmpty(request.getParameter("name"))){
            map.put("name",request.getParameter("name"));
        }


        if(!StringUtils.isEmpty(pageNo)&&!StringUtils.isEmpty(size)){
            Long pageNum = (pageNo-1)*size;
            map.put("pageNo",pageNum);
            map.put("size",size);
        }
        if (!"超级管理员".equals(user.getType())) {
            // 添加只能查看存在权限
            if(!StringUtils.isEmpty(user)){
                map.put("userId",user.getId());
            }
        }
        dataMap.put("data",unitService.getAllUnit(map));
        dataMap.put("pageTotal",unitService.getPageTotal(map));
        return ResultUtil.success(dataMap);
//        return ResultUtil.success(unitService.getAllUnit(token));
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

    @PostMapping("/copyUnit")
    public Result copyUnit(@RequestBody RUnit unit, @RequestHeader("Authentication-Token") String token) {
        return ResultUtil.success(unitService.copyUnit(unit, token));
    }
    @DeleteMapping("/delUnit/{id}")
    public Result delUnit(@PathVariable Long id, @RequestHeader("Authentication-Token") String token) {
        return ResultUtil.success(unitService.delete(id, token));
    }

    @PostMapping("/updateUnit")
    public Result updateUnit(@RequestBody RUnit unit, @RequestHeader("Authentication-Token") String token) {
        return ResultUtil.success(unitService.update(unit, token));
    }

    @PostMapping("/PlAddRoom")
    public Result PlAddRoom(@RequestBody  Map<String, String> map, @RequestHeader("Authentication-Token") String token) {
        return unitService.PlAddRoom(map, token);
    }

}

