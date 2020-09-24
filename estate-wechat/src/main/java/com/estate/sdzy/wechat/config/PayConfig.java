package com.estate.sdzy.wechat.config;

import com.estate.sdzy.wechat.resource.WeChatResources;
import com.lly835.bestpay.config.WxPayConfig;
import com.lly835.bestpay.service.impl.BestPayServiceImpl;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

/**
 * @author mq
 * @description: TODO
 * @title: PayConfig
 * @projectName estate-parent
 * @date 2020/9/1816:31
 */
@Component
public class PayConfig {

    @Bean
    public BestPayServiceImpl bestPayService(){
        BestPayServiceImpl bestPayService = new BestPayServiceImpl();
        bestPayService.setWxPayConfig(wxPayConfig());

        return bestPayService;
    }

    @Bean
    public WxPayConfig wxPayConfig(){
        WxPayConfig wxPayConfig = new WxPayConfig();
        wxPayConfig.setAppId(WeChatResources.APPID);
        wxPayConfig.setAppSecret(WeChatResources.APPSECRET);
        // 商户号
        wxPayConfig.setMchId(WeChatResources.PAYID);
        // 商户秘钥
        wxPayConfig.setMchKey(WeChatResources.APPPAYSECRET);
        // 证书位置
        wxPayConfig.setKeyPath(WeChatResources.CERPATH);
        wxPayConfig.setNotifyUrl("http://www.baidu.com");
        return wxPayConfig;
    }
}
