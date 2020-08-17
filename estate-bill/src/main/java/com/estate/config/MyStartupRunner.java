package com.estate.config;

import com.estate.sdzy.asstes.service.RProvinceService;
import com.estate.util.RedisUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import javax.annotation.PostConstruct;
import java.util.concurrent.TimeUnit;

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
     public void run() {
        if (StringUtils.isEmpty(redisUtil.get("District_Number"))){
            redisUtil.set("District_Number",rProvinceService.getProvinceChild(),30, TimeUnit.DAYS);
        }else {
            System.out.println("=============================================");
            System.out.println("行政区信息已加载，无需重复加载！！！！");
            System.out.println("=============================================");
        }
    }
}
