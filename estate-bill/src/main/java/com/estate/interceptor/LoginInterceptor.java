package com.estate.interceptor;

import com.estate.common.entity.SUser;
import com.estate.sdzy.system.service.SUserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Date;
import java.util.concurrent.TimeUnit;

/**
 * @author mq
 * @date 2020/7/24 16:22
 * @description 登录拦截器，验证用户是否登录
 */
@Slf4j
@Component
public class LoginInterceptor implements HandlerInterceptor {

    @Autowired
    private RedisTemplate<String, Object> redisTemplate;

    @Autowired
    private SUserService userService;

    // 登录路径
    @Value("${loginPath}")
    private String loginPath;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        log.info("验证用户是否登录");
        try {

            String token = String.valueOf(request.getHeader("Authentication-Token"));
            SUser user = (SUser) redisTemplate.opsForValue().get(token);
//            SUser user = (SUser) request.getSession().getAttribute(token);
            if (user != null) {
                log.info("用户{},登录成功。记录时间{}", user.getUserName(), new Date());
                // 重置过期时间。单位秒
                redisTemplate.expire(token, 50 * 60 * 60 , TimeUnit.SECONDS);
                return true;
            }
//            response.sendRedirect(request.getContextPath());
        } catch (Exception e) {
            e.printStackTrace();
        }
        log.error("未登录，请重新登录后在操作！");
        return false;
    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {

    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {

    }
}
