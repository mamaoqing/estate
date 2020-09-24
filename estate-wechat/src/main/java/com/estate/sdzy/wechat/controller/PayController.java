package com.estate.sdzy.wechat.controller;

import com.estate.common.util.ConnectUtil;
import com.estate.common.util.TransactionConnUtil;
import com.estate.sdzy.wechat.service.PayService;
import com.lly835.bestpay.model.PayResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;
import java.util.UUID;

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
        BigDecimal pay = new BigDecimal(0.01);
        String payment_method = "微信支付";
        Date created_at = new Date();
        Integer ownerId = null;
        BigDecimal aaa = new BigDecimal(0);
        String ownerSql = "select id from r_owner where wx_openid = ? and comp_id = ?";
        Connection connection = null;
        log.info("接收到的参数有 \tlistBillId:{},commId:{},compId:{},payPrice:{},openid:{}", listBillId, commId, compId, payPrice, openid);
        String billSql = "select bb.*,price+sale_price-pay_price+overdue_cost aaa from   f_bill bb where   bb.id in (" + listBillId + ")   ";
        String insertf_finance_record_draftSql = "insert into f_finance_record_draft (comp_id,comm_id,no,oper_type,cost,owner_id,payment_method,created_at) values(?,?,?,?,?,?,?,?)";
        String insertf_finance_bill_record_draftSql = "insert into f_finance_bill_record_draft (comp_id,comm_id,finance_record_id,bill_id,cost,created_at) (?,?,?,?,?,now())";
        try {
            ResultSet resultSet = ConnectUtil.executeQuery(ownerSql, new Object[]{openid, compId});
            if (resultSet.next()) {
                ownerId = resultSet.getInt("id");
            }
            Object[] objects = {compId, commId, no, oper_type, payPrice, ownerId, payment_method, created_at};
            ResultSet result = TransactionConnUtil.executeQuery(billSql );
            connection = TransactionConnUtil.getConnection();
            Integer integer = TransactionConnUtil.executeUpdate(insertf_finance_record_draftSql, objects, true);
            log.info("执行的sql添加语句:{},传入的参数是：{}", insertf_finance_bill_record_draftSql, integer);
            boolean b =true;
            while(result.next()&&b) {
                aaa = result.getBigDecimal("aaa");
                if(pay.compareTo(aaa)==1){
                    aaa=aaa.subtract(pay);
                    b =true;
                }else {
                    pay=aaa;
                    aaa=new BigDecimal(0);
                    b=false;
                }
                Object[] objectb = {compId, commId, integer, result.getBigDecimal("id"), pay};
                TransactionConnUtil.executeUpdate(insertf_finance_bill_record_draftSql, objectb);

            }
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
}
