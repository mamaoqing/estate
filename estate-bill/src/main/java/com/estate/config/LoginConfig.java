package com.estate.config;

import com.estate.interceptor.BuyInterceptor;
import com.estate.interceptor.LoginInterceptor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.dao.DataAccessException;
import org.springframework.data.redis.connection.RedisClusterConnection;
import org.springframework.data.redis.connection.RedisConnection;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.RedisSentinelConnection;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.servlet.config.annotation.InterceptorRegistration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * @author mq
 * @date 2020/7/24 16:29
 * @description 登录拦截器配置
 */
@Configuration
public class LoginConfig implements WebMvcConfigurer {

    @Bean
    public LoginInterceptor getSessionInterceptor() {
        return new LoginInterceptor();
    }


    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        InterceptorRegistration registration = registry.addInterceptor(getSessionInterceptor());
        // 拦截路径 /** 表示所有的路径都拦截
        registration.addPathPatterns("/sdzy/**");
        // 不拦截路径 多个用 "," 隔开
        registration.excludePathPatterns("/buy/**","/sdzy/login/**","/sdzy/rProvince/**");
    }
}
