package com.estate.config;

import com.estate.sdzy.service.RProvinceService;
import com.estate.util.RedisUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import javax.annotation.PostConstruct;
import java.util.List;

/**
 * @program: estate-parent
 * @description: 项目启动时执行类
 * @author: cfy
 * @create: 2020-08-12 09:51
 **/

@Component
public class MyStartupRunner {
    @Autowired
    private RedisUtil redisUtil;
    @Autowired
    private RProvinceService rProvinceService;

    @PostConstruct
     public void test() {
        if (StringUtils.isEmpty(redisUtil.get("District_Number"))){
            List list = rProvinceService.listProvince();
            System.out.println(list);
        }else {
            System.out.println("=============================================");
            System.out.println("");
            System.out.println("=============================================");
        }
    }
}
