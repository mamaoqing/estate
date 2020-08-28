package com.estate.sdzy.asstes.controller;


import com.estate.sdzy.asstes.service.ROwnerPropertyService;
import com.estate.util.Result;
import com.estate.util.ResultUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import org.springframework.web.bind.annotation.RestController;

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
}

