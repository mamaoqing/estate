package com.estate.sdzy.wechat.entity;

import lombok.Data;
import net.sf.json.JSONObject;

/**
 * @author mq
 * @description: TODO
 * @title: Inform
 * @projectName estate-parent
 * @date 2020/9/228:55
 */
@Data
public class Inform {

    private String returnUrl;
    private JSONObject data;
}
