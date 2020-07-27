package com.estate.config;


import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;

@Configuration
public class CommonConfig {

    @Bean
    public PathMatchingResourcePatternResolver resourcePatternResolver(){
        return new PathMatchingResourcePatternResolver();
    }

}
