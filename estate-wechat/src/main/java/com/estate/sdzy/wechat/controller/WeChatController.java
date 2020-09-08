package com.estate.sdzy.wechat.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author mq
 * @description: TODO
 * @title: WechatController
 * @projectName estate-parent
 * @date 2020/9/710:50
 */
@RestController
@RequestMapping("/weChat/")
public class WeChatController {

    @GetMapping("/hello")
    public String insex(){
        return "hello wechat!!";
    }
}
