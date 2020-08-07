package com.estate.sdzy.controller;


import com.estate.sdzy.service.SUserCommService;
import com.estate.util.Result;
import com.estate.util.ResultUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import org.springframework.stereotype.Controller;

/**
 * <p>
 * 用户社区表 前端控制器
 * </p>
 *
 * @author mq
 * @since 2020-07-24
 */
@RestController
@RequestMapping("/sdzy/sUserComm")
public class SUserCommController {

    @Autowired
    private SUserCommService userCommService;

    @GetMapping("listUserComm")
    public Result listUserComm(Long id, Long compId) {
        return ResultUtil.success(userCommService.listUserComm(compId, id));
    }

    @PostMapping("/setUserComm")
    public Result setUserComm(Long userId, String commIds, @RequestHeader("Authentication-Token") String token, String remark) {
        return ResultUtil.success(userCommService.setUserComm(userId, commIds, token, remark));
    }
}

