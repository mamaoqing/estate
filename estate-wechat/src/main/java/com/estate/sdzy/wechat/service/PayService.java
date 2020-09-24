package com.estate.sdzy.wechat.service;

import com.lly835.bestpay.model.PayResponse;

/**
 * @author mq
 * @description: TODO
 * @title: PayService
 * @projectName estate-parent
 * @date 2020/9/1816:17
 */
public interface PayService {

    PayResponse create(Double price,String openid,String orderId,String orderName);
}
