package com.estate.sdzy.wechat.service;

import com.lly835.bestpay.model.PayResponse;

import javax.servlet.http.HttpServletRequest;

/**
 * @author mq
 * @description: TODO
 * @title: PayService
 * @projectName estate-parent
 * @date 2020/9/1816:17
 */
public interface PayService {

    PayResponse create(HttpServletRequest request,  String orderName,String oper_type,String accountName,Integer commId,Integer accountId);

    String notify(String data);

}
