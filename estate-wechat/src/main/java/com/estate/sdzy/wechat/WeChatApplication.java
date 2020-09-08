package com.estate.sdzy.wechat;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * @author mq
 * @description: TODO
 * @title: WechatApplication
 * @projectName estate-parent
 * @date 2020/9/79:48
 */
@SpringBootApplication
public class WeChatApplication {
    public static void main(String[] args) {
        SpringApplication.run(WeChatApplication.class,args);
        System.out.println("start boot wechat ok...");
    }
}
