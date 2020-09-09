package com.estate.sdzy.system.controller;


import com.estate.common.util.Result;
import com.estate.common.util.ResultUtil;
import com.estate.sdzy.system.entity.SBank;
import com.estate.sdzy.system.service.SBankService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * <p>
 * 银行 前端控制器
 * </p>
 *
 * @author mq
 * @since 2020-08-04
 */
@RestController
@RequestMapping("/sdzy/sBank")
public class SBankController {

    @Autowired
    private SBankService sBankService;

    @GetMapping("getAllBank")
    public Result getAllBank(@RequestHeader("Authentication-Token") String token){
        return ResultUtil.success(sBankService.getAllBank(token));
    }
}

