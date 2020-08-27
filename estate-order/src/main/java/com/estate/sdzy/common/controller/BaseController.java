package com.estate.sdzy.common.controller;

import com.estate.sdzy.system.entity.SUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Controller;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

/**
 * @author mq
 * @date 2020/7/28 13:51
 * @description controller的父类
 */
@Controller
public class BaseController {

    @Autowired
    private RedisTemplate<String, Object> redisTemplate;


    protected SUser getUserByToken(String token) {
        Object o = redisTemplate.opsForValue().get(token);
        return (SUser) o;
    }

    protected Map<String, String> getParameterMap(HttpServletRequest request) {
        // 参数Map
        Map<String, String[]> properties = request.getParameterMap();
        // 返回值Map
        Map<String, String> returnMap = new HashMap<>();
        Iterator entries = properties.entrySet().iterator();
        Map.Entry<String, Object> entry;
        String name = "";
        String value = "";
        while (entries.hasNext()) {
            entry = (Map.Entry) entries.next();
            name = (String) entry.getKey();
            Object valueObj = entry.getValue();
            if (null == valueObj) {
                value = "";
            } else if (valueObj instanceof String[]) {
                String[] values = (String[]) valueObj;
                for (int i = 0; i < values.length; i++) {
                    value = values[i] + ",";
                }
                value = value.substring(0, value.length() - 1);
            } else {
                value = valueObj.toString();
            }
            returnMap.put(name, value);
        }
        return returnMap;
    }
}
