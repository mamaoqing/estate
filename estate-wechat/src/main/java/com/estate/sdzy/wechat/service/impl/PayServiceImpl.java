package com.estate.sdzy.wechat.service.impl;

import com.estate.sdzy.wechat.resource.WeChatResources;
import com.estate.sdzy.wechat.service.PayService;
import com.lly835.bestpay.enums.BestPayTypeEnum;
import com.lly835.bestpay.model.PayRequest;
import com.lly835.bestpay.model.PayResponse;
import com.lly835.bestpay.service.impl.BestPayServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author mq
 * @description: TODO
 * @title: PayServiceImpl
 * @projectName estate-parent
 * @date 2020/9/1816:17
 */
@Service
@Slf4j
public class PayServiceImpl implements PayService {

    @Autowired
    private BestPayServiceImpl bestPayService;



    @Override
    public PayResponse create(Double price,String openid,String orderId,String orderName) {
        PayRequest payRequest = new PayRequest();
        payRequest.setOpenid(openid);
        payRequest.setOrderAmount(price);
        payRequest.setOrderId(orderId);
        payRequest.setOrderName(orderName);
        payRequest.setPayTypeEnum(BestPayTypeEnum.WXPAY_MP);

        PayResponse pay = bestPayService.pay(payRequest);
        log.info("pay:{}",pay);

        return pay;
    }

    @Override
    public PayResponse notify(String data) {
        PayResponse payResponse = bestPayService.asyncNotify(data);
        log.info("支付返回结果是：{}",payResponse);
        return payResponse;
    }
}
