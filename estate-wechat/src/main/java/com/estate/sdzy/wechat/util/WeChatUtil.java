package com.estate.sdzy.wechat.util;

import com.estate.sdzy.wechat.entity.BaseButton;
import com.estate.sdzy.wechat.entity.ClickButton;
import com.estate.sdzy.wechat.entity.Menu;
import com.estate.sdzy.wechat.entity.ViewButton;
import com.estate.sdzy.wechat.resource.WeChatResources;
import lombok.extern.slf4j.Slf4j;
import net.sf.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

/**
 * @author mq
 * @description: TODO
 * @title: WeChatUtil
 * @projectName estate-parent
 * @date 2020/9/1716:02
 */
@Slf4j
public class WeChatUtil {

    /**
     * 菜单信息
     * @return
     */
    public static Menu initMenu() {
        Menu menu = new Menu();

        ViewButton viewButton = new ViewButton();
        viewButton.setName("生活圈");
        viewButton.setType("view");
        viewButton.setUrl("http://www.baidu.com");
        ViewButton viewButton1 = new ViewButton();
        viewButton1.setName("人工智能");
        viewButton1.setType("view");
        viewButton1.setUrl("http://www.baidu.com");
        ViewButton viewButton2 = new ViewButton();
        viewButton2.setName("物业");
        viewButton2.setType("view");
        viewButton2.setUrl("http://www.baidu.com");


        BaseButton[] baseButtons = new BaseButton[]{viewButton, viewButton1, viewButton2};
        menu.setButton(baseButtons);
        return menu;
    }

    /**
     * 获取access_token
     * @param code
     * @return
     */
    public static JSONObject getHTMLAccessToken(String code) {
        String replace = WeChatResources.GET_HTML_ACCESS_TOKEN.replace("APPID", WeChatResources.APPID).replace("SECRET", WeChatResources.APPSECRET).replace("CODE", code);
        log.info("请求url:{}",replace);
        JSONObject jsonObject = HttpUtil.doGet(replace);
        return jsonObject;
    }

    /**
     * huoqu yonghu xinxi
     * @return
     */
    public static JSONObject getUserInfo(Object access_token,Object open_id){

        String replace = WeChatResources.GET_USER_INFO.replace("ACCESS_TOKEN", access_token.toString()).replace("OPENID", open_id.toString());
        JSONObject jsonObject = HttpUtil.doGet(replace);
        return jsonObject;
    }

    /**
     * 获取token
     * @return
     */
    public static JSONObject  getAccessToken(){
        String replace = WeChatResources.GET_ACCESS_TOKEN.replace("APPID", WeChatResources.APPID).replace("APPSECRET", WeChatResources.APPSECRET);
        JSONObject jsonObject = HttpUtil.doGet(replace);
        return jsonObject;
    }
}
