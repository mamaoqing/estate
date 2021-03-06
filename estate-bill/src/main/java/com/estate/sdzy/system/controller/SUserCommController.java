package com.estate.sdzy.system.controller;


import com.estate.common.util.Result;
import com.estate.common.util.ResultUtil;
import com.estate.sdzy.system.service.SUserCommService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
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

