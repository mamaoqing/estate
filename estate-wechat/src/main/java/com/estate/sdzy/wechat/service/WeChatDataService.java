package com.estate.sdzy.wechat.service;

import net.sf.json.JSONObject;

import java.util.Map;

/**
 * @author mq
 * @description: TODO
 * @title: WeChatDataService
 * @projectName estate-parent
 * @date 2020/10/2216:07
 */
public interface WeChatDataService {

    /**
     * 获取社区列表
     * @param openid
     * @return
     */
    JSONObject commList(String openid);

    /**
     * 账单详情
     * @param openid
     * @return
     */
    JSONObject billDetail(String openid);

    /**
     * 绑定车位
     * @param map
     * @return
     */
    JSONObject park(Map<String,String> map);

    /**
     * 绑定房产
     * @param map
     * @return
     */
    JSONObject estate(Map<String,String> map);
}
