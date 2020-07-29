package com.estate.config;

import com.estate.interceptor.BuyInterceptor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * @author mq
 * @date 2020/7/29 14:20
 * @description
 */
@Configuration
public class BuyConfig implements WebMvcConfigurer {
    @Bean
    public BuyInterceptor buyInterceptor(){
        return new BuyInterceptor();
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        InterceptorRegistration registration = registry.addInterceptor(buyInterceptor());
        // 拦截路径 /** 表示所有的路径都拦截
        registration.addPathPatterns("/buy/*");
        // 不拦截路径 多个用 "," 隔开
//        registration.excludePathPatterns();
    }
}
