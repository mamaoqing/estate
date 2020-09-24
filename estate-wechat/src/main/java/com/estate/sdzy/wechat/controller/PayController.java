package com.estate.sdzy.wechat.controller;

import com.estate.common.util.ConnectUtil;
import com.estate.common.util.TransactionConnUtil;
import com.estate.sdzy.wechat.resource.WeChatResources;
import com.estate.sdzy.wechat.service.PayService;
import com.estate.sdzy.wechat.util.WeChatUtil;
import com.lly835.bestpay.model.PayResponse;
import lombok.extern.slf4j.Slf4j;
import net.sf.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;

/**
 * @author mq
 * @description: TODO
 * @title: PayController
 * @projectName estate-parent
 * @date 2020/9/1816:12
 */
@Controller
@RequestMapping("/pay/")
@Slf4j
public class PayController {

    @Autowired
    private PayService payService;


    @GetMapping("create")
    public ModelAndView create(HttpServletRequest request) {
        ModelAndView mv = new ModelAndView("pay");
//
        String oper_type = "支付";
        String no = UUID.randomUUID().toString().replace("-", "");
        String openid = request.getParameter("openid");
        String listBillId = request.getParameter("listBillId");
        if(!StringUtils.isEmpty(listBillId)){
            listBillId = listBillId.substring(0,listBillId.length()-1);
        }
        String commId = request.getParameter("commId");
        String compId = request.getParameter("compId");
        String payPrice = request.getParameter("payPrice");

        String payment_method = "微信支付";
        Date created_at = new Date();
        Integer ownerId = null;
        String ownerSql = "select id from r_owner where wx_openid = ? and comp_id = ?";
        Connection connection = null;
        log.info("接收到的参数有 \tlistBillId:{},commId:{},compId:{},payPrice:{},openid:{}", listBillId, commId, compId, payPrice, openid);
        String insertf_finance_record_draftSql = "insert into f_finance_record_draft (comp_id,comm_id,no,oper_type,cost,owner_id,payment_method,created_at) values(?,?,?,?,?,?,?,?)";
        String insertf_finance_bill_record_draftSql = "insert into f_finance_bill_record_draft (comp_id,comm_id,finance_record_id,bill_id,cost,created_at) select comp_id,comm_id,?,id,(price+sale_price+overdue_cost-pay_price),now() from f_bill where id in (" + listBillId + ") ";
        try {
            ResultSet resultSet = ConnectUtil.executeQuery(ownerSql, new Object[]{openid, compId});
            if (resultSet.next()) {
                ownerId = resultSet.getInt("id");
            }
            Object[] objects = {compId, commId, no, oper_type, payPrice, ownerId, payment_method, created_at};

            connection = TransactionConnUtil.getConnection();
            Integer integer = TransactionConnUtil.executeUpdate(insertf_finance_record_draftSql, objects, true);
            log.info("执行的sql添加语句:{},传入的参数是：{}", insertf_finance_bill_record_draftSql, integer);
            TransactionConnUtil.executeUpdate(insertf_finance_bill_record_draftSql, new Object[]{integer});

            PayResponse payResponse = payService.create(Double.valueOf(payPrice), openid, integer + "", "物业收款");
            request.setAttribute("res", payResponse);
            log.info("微信支付结果：{}", payResponse);
            connection.commit();
        } catch (SQLException sqlException) {
            try {
                connection.rollback();
            } catch (SQLException throwables) {
                throwables.printStackTrace();
            }
            sqlException.printStackTrace();
        } catch (ClassNotFoundException classNotFoundException) {
            classNotFoundException.printStackTrace();
        } catch (RuntimeException runtimeException) {

        }

//        payService.create(Double.valueOf(payPrice),openid,"","");
        return mv;
    }

    @PostMapping("notify")
    public void payResult(@RequestBody String notifyData) {
        System.out.println(notifyData);
        PayResponse notify = payService.notify(notifyData);

    }

    @PostMapping("doprestore")
    public ModelAndView doprestore(HttpServletRequest request){
        ModelAndView mv = new ModelAndView("pay");
        String openid = request.getParameter("openid");
        System.out.println(openid);

        return mv;
    }

    @RequestMapping("/prestore")
    public String prestore(HttpServletRequest request) {
        String code = request.getParameter("code");
        JSONObject htmlAccessToken = WeChatUtil.getHTMLAccessToken(code);
        Object access_token = htmlAccessToken.get("access_token");
        Object openid = htmlAccessToken.get("openid");

        JSONObject userInfo = WeChatUtil.getUserInfo(access_token, openid);
        String result = null;
        try {
            result = new String(userInfo.toString().getBytes("ISO-8859-1"), "UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        log.info("结果是:{}",result);
        JSONObject jsonObject = JSONObject.fromObject(result);
        request.setAttribute("openid",jsonObject.getString("openid"));
        return "account";
    }
}
