package com.estate.interceptor;

import com.estate.sdzy.entity.SUser;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @author mq
 * @date 2020/7/24 16:22
 * @description 登录拦截器，验证用户是否登录
 */
@Slf4j
public class LoginInterceptor implements HandlerInterceptor {
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        log.info("验证用户是否登录");
        try {
            String token = String.valueOf(request.getParameter("token"));
            SUser user = (SUser) request.getSession().getAttribute(token);
            System.out.println("---------------=========" + token + "=========---------------");
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
