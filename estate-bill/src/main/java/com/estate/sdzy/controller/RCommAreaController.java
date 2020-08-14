package com.estate.sdzy.controller;


import com.estate.sdzy.entity.SUser;
import com.estate.sdzy.service.RCommAreaService;
import com.estate.util.RedisUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
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

}

