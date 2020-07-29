package com.estate.aop;

import com.estate.exception.BillException;
import com.estate.sdzy.entity.SUser;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.util.Arrays;

/**
 * @author mq
 * @date 2020/7/29 16:10
 * @description aop配置
 */
@Slf4j
@Aspect
@Component
public class WebLogAop {

    @Autowired
    private RedisTemplate<String, Object> redisTemplate;


    @Pointcut("execution(public * com.estate.sdzy.controller.*.bill*(..))")
    public void aopLog() {
    }

    @Before("aopLog()")
    public void before(JoinPoint point) {
        ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        HttpServletRequest request = attributes.getRequest();
        writeLog(point);
        String token = request.getParameter("token");
        log.info("token={}",token);
        SUser user =  (SUser)redisTemplate.opsForValue().get(token);
        if(user.getId() == null){
            // 在这里进行数据权限的控制
            throw new BillException(1,"抱歉您没有权限访问");
        }


    }

    @After("aopLog()")
    public void after(JoinPoint point) {
        log.info("=============我来源与after========================");
    }

    @Pointcut("execution(public * com.estate.sdzy.controller.*.*(..))")
    public void allLog() {
    }

    @Before("allLog()")
    public void allLogBefore(JoinPoint point) {
        writeLog(point);
    }

    @After("allLog()")
    public void allLogAfter(JoinPoint point) {

    }

    private void writeLog(JoinPoint point){
        // 接收到请求，记录请求内容
        ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        HttpServletRequest request = attributes.getRequest();

        // 记录下请求内容
        log.info("URL : " + request.getRequestURL());
        log.info("HTTP_METHOD : " + request.getMethod());
        log.info("IP : " + request.getRemoteAddr());
        log.info("CLASS_METHOD : " + point.getSignature().getDeclaringTypeName() + "." + point.getSignature().getName());
        log.info("ARGS : " + Arrays.toString(point.getArgs()));
    }
}
