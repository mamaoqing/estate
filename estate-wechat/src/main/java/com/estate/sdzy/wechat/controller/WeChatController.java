package com.estate.sdzy.wechat.controller;

import com.estate.sdzy.wechat.util.CheckUtil;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

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
    public void cutin(HttpServletResponse response ,String signature, String timestamp, String nonce,String echostr){
        try {
            PrintWriter out = response.getWriter();
            if(CheckUtil.checkWeChat(signature, timestamp, nonce)){
                out.println(echostr);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}
