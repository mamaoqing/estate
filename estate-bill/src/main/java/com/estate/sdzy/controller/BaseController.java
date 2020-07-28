package com.estate.sdzy.controller;

import com.estate.sdzy.entity.SUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Controller;

/**
 * @author mq
 * @date 2020/7/28 13:51
 * @description controller的父类
 */
@Controller
public class BaseController {

    @Autowired
    private RedisTemplate<String, Object> redisTemplate;


    protected SUser getUserByToken(String token) {
        Object o = redisTemplate.opsForValue().get(token);
        return (SUser) o;
    }
}
