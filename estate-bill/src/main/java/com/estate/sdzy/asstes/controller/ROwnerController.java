package com.estate.sdzy.asstes.controller;


import com.estate.sdzy.asstes.entity.ROwner;
import com.estate.sdzy.asstes.service.ROwnerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import org.springframework.stereotype.Controller;

import java.util.List;
import java.util.Map;

/**
 * <p>
 * 业主表 前端控制器
 * </p>
 *
 * @author mq
 * @since 2020-08-04
 */
@RestController
@RequestMapping("/sdzy/rOwner")
public class ROwnerController {

    @Autowired
    private ROwnerService ownerService;

    @PostMapping("/getOwenerList")
    public List<ROwner> getOwenerList(@RequestBody  Map<String, String> map, @RequestHeader("Authentication-Token") String token){
        return ownerService.getOwenerList(map,token);
    }
}

