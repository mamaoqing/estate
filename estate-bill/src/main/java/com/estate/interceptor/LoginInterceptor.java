package com.estate.interceptor;

import com.estate.sdzy.entity.SUser;
import com.estate.sdzy.service.SUserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;
import org.springframework.web.context.support.WebApplicationContextUtils;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @author mq
 * @date 2020/7/24 16:22
 * @description 登录拦截器，验证用户是否登录
 */
@Slf4j
@Component
public class LoginInterceptor implements HandlerInterceptor {

    @Autowired
    private RedisTemplate<String,Object> redisTemplate;

    @Autowired
    private SUserService userService;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        log.info("验证用户是否登录");
        try {

            String token = String.valueOf(request.getHeader("Authentication-Token"));
            SUser user = (SUser)redisTemplate.opsForValue().get(token);
//            SUser user = (SUser) request.getSession().getAttribute(token);
            if (user != null) {
                return true;
            }
//            response.sendRedirect(request.getContextPath()+"你的登陆页地址");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return true;
    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {

    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {

    }
}
