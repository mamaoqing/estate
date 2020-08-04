package com.estate.sdzy.controller;


import com.estate.sdzy.service.RProvinceService;
import com.estate.util.Result;
import com.estate.util.ResultUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RestController;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author mq
 * @since 2020-08-04
 */
@RestController
@RequestMapping("/sdzy/rProvince")
public class RProvinceController {

    @Autowired
    private RProvinceService provinceService;

    @GetMapping("/get")
    public Result getProvinces(){
        return ResultUtil.success(provinceService.listProvinces());
    }

}

