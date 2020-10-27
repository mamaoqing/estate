package com.estate.sdzy.wechat.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author mq
 * @description: TODO
 * @title: CheckController
 * @projectName estate-parent
 * @date 2020/10/1014:32
 */
@RestController
public class CheckController {

    @RequestMapping("/MP_verify_SewxNjDdhZY2dUCr.txt")
    public String check(){
        return "SewxNjDdhZY2dUCr";
    }
}
