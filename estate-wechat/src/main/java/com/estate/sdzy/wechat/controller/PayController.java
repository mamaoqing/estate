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
import org.springframework.web.bind.annotation.*;
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
        if (!StringUtils.isEmpty(listBillId)) {
            listBillId = listBillId.substring(0, listBillId.length() - 1);
        }
        String commId = request.getParameter("commId");
        String compId = request.getParameter("compId");
        String payPrice = request.getParameter("payPrice");
        BigDecimal pay = new BigDecimal(payPrice);
        String payment_method = "微信支付";
        Date created_at = new Date();
        Integer ownerId = null;
        BigDecimal aaa = new BigDecimal(0);
        String ownerSql = "select id from r_owner where wx_openid = ? and comp_id = ?";
        Connection connection = null;
        String billSql = "select bb.*,price+sale_price-pay_price+overdue_cost aaa from   f_bill bb where   bb.id in (" + listBillId + ")   ";
        String insertf_finance_record_draftSql = "insert into f_finance_record_draft (comp_id,comm_id,no,oper_type,cost,owner_id,payment_method,created_at) values(?,?,?,?,?,?,?,?)";
        String insertf_finance_bill_record_draftSql = "insert into f_finance_bill_record_draft (comp_id,comm_id,finance_record_id,bill_id,cost,created_at)values (?,?,?,?,?,now())";
        try {
            ResultSet resultSet = ConnectUtil.executeQuery(ownerSql, new Object[]{openid, compId});
            if (resultSet.next()) {
                ownerId = resultSet.getInt("id");
            }
            Object[] objects = {compId, commId, no, oper_type, pay, ownerId, payment_method, created_at};
            ResultSet result = TransactionConnUtil.executeQuery(billSql);
            connection = TransactionConnUtil.getConnection();
            Integer integer = TransactionConnUtil.executeUpdate(insertf_finance_record_draftSql, objects, true);
            boolean b = true;
            while (result.next() && b) {
                aaa = result.getBigDecimal("aaa");
                if (pay.compareTo(aaa) == 1) {
                    pay = pay.subtract(aaa);
                    b = true;
                } else {
                    aaa = pay;
                    b = false;
                }
                Object[] objectb = {compId, commId, integer, result.getBigDecimal("id"), aaa};
                TransactionConnUtil.executeUpdate(insertf_finance_bill_record_draftSql, objectb);

            }
            PayResponse payResponse = payService.create(0.01, openid, integer + "", "物业收款");
            request.setAttribute("res", payResponse);
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

        return mv;
    }

    @ResponseBody
    @PostMapping("notify")
    public String payResult(@RequestBody String notifyData) {
        String insertf_finance_bill_recordsql = "insert into f_finance_bill_record (comp_id,comm_id,finance_record_id,bill_id,cost,created_at,created_by,created_name) select comp_id,comm_id,?,bill_id,cost,created_at,created_by,created_name from f_finance_bill_record_draft where finance_record_id = ?";
        String deletef_finance_bill_record_draftSql = "delete from f_finance_bill_record_draft where finance_record_id = ?";

        String insertf_finance_recordSql = "insert into f_finance_record (comp_id,comm_id,no,account_id,oper_type,cost,owner_id,payment_method,remark,created_at,created_by,created_name) select comp_id,comm_id,no,account_id,oper_type,cost,owner_id,payment_method,remark,created_at,created_by,created_name from f_finance_record_draft where id = ? ";
        String deletef_finance_record_draftSql = "delete from f_finance_record_draft where id = ?";
        PayResponse notify = payService.notify(notifyData);
        Double orderAmount = notify.getOrderAmount();
        String orderId = notify.getOrderId();
        String sql = "select aa.bill_id,aa.cost,price+sale_price-pay_price+overdue_cost aaa,bb.state,bb.is_payment, pay_price  from f_finance_bill_record_draft aa,f_bill bb  where aa.bill_id = bb.id and finance_record_id = " + orderId;
        Connection connection = null;
        try {
            connection = TransactionConnUtil.getConnection();
            Object[] objects = new Object[]{orderId};
            if(orderAmount > 0){
               ResultSet resultSet = ConnectUtil.executeQuery(sql);
               while (resultSet.next()) {
                   int bill_id = resultSet.getInt("bill_id");
                   BigDecimal cost = resultSet.getBigDecimal("cost");
                   BigDecimal aaa = resultSet.getBigDecimal("aaa");
                   BigDecimal pay_price = resultSet.getBigDecimal("pay_price");
                   String state = resultSet.getString("state");
                   String is_payment = resultSet.getString("is_payment");
                   String updasteSql = "update f_bill set pay_price = ?, state=?,is_payment=?  where id = ?";
                   if (aaa.compareTo(cost) == 0) {
                       state = "已支付";
                       is_payment = "是";
                   }

                   TransactionConnUtil.executeUpdate(updasteSql, new Object[]{pay_price.add(cost), state, is_payment, bill_id});
               }

               Integer integer = TransactionConnUtil.executeUpdate(insertf_finance_recordSql, objects, true);
               TransactionConnUtil.executeUpdate(insertf_finance_bill_recordsql, new Object[]{integer, orderId});
           }else{
                log.error("支付失败！！！=====================================================");
            }
            TransactionConnUtil.executeUpdate(deletef_finance_bill_record_draftSql, objects);
            TransactionConnUtil.executeUpdate(deletef_finance_record_draftSql, objects);

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
        }
        System.out.println(11111);
        System.out.println(22222);
        System.out.println(orderId);
        System.out.println(11111);
        String result = "<xml>" +
                            "<return_code><![CDATA[SUCCESS]]></return_code>"+
                            "<return_msg><![CDATA[OK]]></return_msg>"+
                        "</xml>";


        return result;
    }

    @PostMapping("doprestore")
    public ModelAndView doprestore(HttpServletRequest request) {
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
        JSONObject jsonObject = JSONObject.fromObject(result);
        request.setAttribute("openid", jsonObject.getString("openid"));
        return "account";
    }
}
