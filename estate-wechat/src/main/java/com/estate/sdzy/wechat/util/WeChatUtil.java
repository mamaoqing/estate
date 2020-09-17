package com.estate.sdzy.wechat.util;

import com.estate.sdzy.wechat.entity.BaseButton;
import com.estate.sdzy.wechat.entity.ClickButton;
import com.estate.sdzy.wechat.entity.Menu;
import com.estate.sdzy.wechat.entity.ViewButton;

/**
 * @author mq
 * @description: TODO
 * @title: WeChatUtil
 * @projectName estate-parent
 * @date 2020/9/1716:02
 */
public class WeChatUtil {
    public static Menu initMenu(){
        Menu menu = new Menu();
        ClickButton clickButton = new ClickButton();
        clickButton.setName("click菜单");
        clickButton.setType("click");
        clickButton.setKey("1");
        ViewButton viewButton = new ViewButton();
        viewButton.setName("view菜单");
        viewButton.setType("view");
        viewButton.setUrl("http://www.qjwsg.com");

        ClickButton clickButton1 = new ClickButton();

        clickButton1.setName("扫码");
        clickButton1.setType("scancode_push");
        clickButton1.setKey("2");

        BaseButton[] baseButtons = new BaseButton[]{clickButton,viewButton,clickButton1};
        menu.setButton(baseButtons);
        return menu;
    }
}
