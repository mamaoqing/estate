package com.estate.sdzy.wechat.util;

import com.estate.sdzy.wechat.entity.Menu;
import com.estate.sdzy.wechat.resource.WeChatResources;
import net.sf.json.JSONObject;
import org.springframework.util.StringUtils;

/**
 * @author mq
 * @description: TODO
 * @title: MenuUtil
 * @projectName estate-parent
 * @date 2020/9/1813:59
 */
public class MenuUtil {

    public static void setMenu(String token){
        String replace = WeChatResources.CREATE_MENU.replace("ACCESS_TOKEN", token);
        Menu menu = WeChatUtil.initMenu();
        String jsonObject = JSONObject.fromObject(menu).toString();
        JSONObject jsonObject1 = HttpUtil.doPost(replace, jsonObject);
        System.out.println(jsonObject1);
    }
    public static void deleteMenu(String token){
        String url = WeChatResources.DELETE_MENU.replace("ACCESS_TOKEN", token);
        HttpUtil.doGet(url);
    }

    public static void main(String[] args) {
        String accessToken = AccessTokenUtil.getToken();
        System.out.println(accessToken);
        MenuUtil.setMenu(accessToken);
//        MenuUtil.deleteMenu(accessToken);
    }
}
