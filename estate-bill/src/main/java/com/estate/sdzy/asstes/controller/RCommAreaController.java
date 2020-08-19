package com.estate.sdzy.asstes.controller;


import com.estate.sdzy.system.entity.SUser;
import com.estate.sdzy.asstes.service.RCommAreaService;
import com.estate.util.RedisUtil;
import com.estate.util.Result;
import com.estate.util.ResultUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * <p>
 * 社区分区表 前端控制器
 * </p>
 *
 * @author mq
 * @since 2020-07-28
 */
@RestController
@RequestMapping("/sdzy/rCommArea")
public class RCommAreaController {

    @Autowired
    private RCommAreaService commAreaService;
    @Autowired
    private RedisUtil redisUtil;

    @GetMapping("/getAllAreaByUserId")
    public List<Map<String, Object>> getAllArea(@RequestHeader("Authentication-Token") String token) {
        SUser user = (SUser) redisUtil.get(token);
        return commAreaService.listAreaMapByUserId(user.getId());
    }

    @GetMapping("/listArea/{id}")
    public Result listArea(@PathVariable("id") Long id){

        return ResultUtil.success(commAreaService.listArea(id));
    }

}

