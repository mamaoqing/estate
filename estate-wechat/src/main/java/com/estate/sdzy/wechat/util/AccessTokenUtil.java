package com.estate.sdzy.wechat.util;

import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.estate.common.util.ConnectUtil;
import net.sf.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * @author mq
 * @description: TODO
 * @title: AccessTokenUtil
 * @projectName estate-parent
 * @date 2020/9/1814:56
 */
public class AccessTokenUtil {

    public static String getToken() {
        String access_token = "";
        try {
            String sql = "select * from access_token where out_time > ?";
            Object[] objects = new Object[]{new Date()};
            ResultSet resultSet = ConnectUtil.executeQuery(sql, objects);
            while (resultSet.next()) {
                access_token = resultSet.getString("access_token");
            }
            if(StringUtils.isEmpty(access_token)){
                JSONObject accessToken = WeChatUtil.getAccessToken();
                String insertSql = "insert into access_token (access_token,out_time,create_time) values(?,?,?)";
                access_token = accessToken.get("access_token").toString();
                Object[] insert = new Object[]{access_token, addDateMinut(1), new Date()};
                ConnectUtil.executeUpdate(insertSql,insert);
            }

        } catch (SQLException sqlException) {
            sqlException.printStackTrace();
        } catch (ClassNotFoundException classNotFoundException) {
            classNotFoundException.printStackTrace();
        }
        return access_token;
    }

    public static String addDateMinut( int hour) {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date date = new Date();
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        cal.add(Calendar.HOUR, hour);// 24小时制
        date = cal.getTime();
        return format.format(date);
    }
}
