package com.estate.sdzy.asstes.controller;


import com.estate.common.util.Result;
import com.estate.common.util.ResultUtil;
import com.estate.sdzy.asstes.service.ROwnerPropertyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

/**
 * <p>
 * 业主与物业对应关系 前端控制器
 * </p>
 *
 * @author mq
 * @since 2020-08-04
 */
@RestController
@RequestMapping("/sdzy/rOwnerProperty")
public class ROwnerPropertyController {

    @Autowired
    private ROwnerPropertyService ownerPropertyService;

    @GetMapping("/ownerPro/{id}")
    public Result ownerPro(@PathVariable("id")Long id){
        return ResultUtil.success(ownerPropertyService.ownerProByParkId(id));
    }

    @GetMapping("/getOwnerProp/{ownerId}")
    public Result getOwnerProperty(@PathVariable("ownerId")Long ownerId){
        return ResultUtil.success(ownerPropertyService.getOwnerProperty(ownerId));
    }

    @GetMapping("/deleteOwnerProp/{id}")
    public Result deleteOwnerProp(@PathVariable("id")Long id, @RequestHeader("Authentication-Token") String token){
        return ResultUtil.success(ownerPropertyService.delete(id,token));
    }

    @PostMapping("/insertRoomOwner")
    public Result getOwnerProperty(@RequestBody Map map, @RequestHeader("Authentication-Token") String token){
        boolean b = ownerPropertyService.insertRoomOwner(map, token);
        if (b){
            return ResultUtil.success();
        }else {
            return ResultUtil.error("业主重复或者其他错误",1);
        }
    }
}

