package com.estate.config;

import com.baomidou.mybatisplus.extension.plugins.PaginationInterceptor;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;

@Configuration
@MapperScan("com.estate.sdzy.*.mapper")
public class MybatisPlusConfig {


    private PathMatchingResourcePatternResolver resolver;

    private Environment environment;

//    public MybatisPlusConfig(Environment environment, PathMatchingResourcePatternResolver resolver){
//        this.environment = environment;
//        this.resolver = resolver;
//    }

    // 分页插件
    @Bean
    public PaginationInterceptor paginationInterceptor() {
        PaginationInterceptor paginationInterceptor = new PaginationInterceptor();
        paginationInterceptor.setLimit(-1);
        return paginationInterceptor;
    }
}
