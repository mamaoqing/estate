package com.estate.sdzy.asstes.controller;


import com.estate.sdzy.asstes.service.RDistrictService;
import com.estate.util.Result;
import com.estate.util.ResultUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import org.springframework.web.bind.annotation.RestController;

/**
 * <p>
 * 县区 前端控制器
 * </p>
 *
 * @author mq
 * @since 2020-08-04
 */
@RestController
@RequestMapping("/sdzy/rDistrict")
public class RDistrictController {

    @Autowired
    private RDistrictService districtService;

    @GetMapping("/{id}")
    public Result getDistrict(@PathVariable("id") Long id){
        return ResultUtil.success(districtService.districtList(id));
    }

}

