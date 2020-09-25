package com.estate.sdzy.wechat.service.impl;

import com.estate.common.util.ConnectUtil;
import com.estate.common.util.TransactionConnUtil;
import com.estate.sdzy.wechat.resource.WeChatResources;
import com.estate.sdzy.wechat.service.PayService;
import com.estate.sdzy.wechat.util.SendMessage;
import com.lly835.bestpay.enums.BestPayTypeEnum;
import com.lly835.bestpay.model.PayRequest;
import com.lly835.bestpay.model.PayResponse;
import com.lly835.bestpay.service.impl.BestPayServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import javax.servlet.http.HttpServletRequest;
import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.*;

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
    public PayResponse create(HttpServletRequest request, String orderName, String oper_type,String accountName,Integer commIds,Integer accountId) {
        Integer account_id = null;
        Date created_at = new Date();
        String payPrice = request.getParameter("payPrice");
        String no = UUID.randomUUID().toString().replace("-", "");
        String openid = request.getParameter("openid");
        if ("预存".equals(oper_type)) {
            Connection connection = null;
            String selectSql = "select aa.id accountId,bb.id ownerId from f_account aa ,r_owner bb where aa.owner_id = bb.id and  aa.id='" + accountId + "'";
            String selectOwnerSql = "select id from r_owner  where wx_openid='" + openid + "'";
            try {
                ResultSet resultSet = TransactionConnUtil.executeQuery(selectSql);
                ResultSet owner = TransactionConnUtil.executeQuery(selectOwnerSql);
                // 有值，需要更新，否则就添加

                if (resultSet.next()) {
                    account_id = resultSet.getInt("accountId");
                } else {
                    connection = TransactionConnUtil.getConnection();
                    while (owner.next()) {
                        String insertOwner = "insert into f_account (comp_id,comm_id,owner_id,name,type,no,fee,remark,created_at,modified_at) values(?,?,?,?,?,?,?,?,?,?)";
                        account_id = TransactionConnUtil.executeUpdate(insertOwner, new Object[]{WeChatResources.COMP_ID, commIds, owner.getInt("id"), accountName, "预存账户", "", 0, "", created_at, created_at},true);
                    }
                    connection.commit();
                }
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
        }
        PayRequest payRequest = new PayRequest();
        String listBillId = request.getParameter("listBillId");
        if (!StringUtils.isEmpty(listBillId)) {
            listBillId = listBillId.substring(0, listBillId.length() - 1);
        }
        String commId = request.getParameter("commId");
        Integer compId = WeChatResources.COMP_ID;
        BigDecimal pay = new BigDecimal(payPrice);
        String payment_method = "微信支付";

        Integer ownerId = null;
        BigDecimal aaa = new BigDecimal(0);
        String ownerSql = "select id from r_owner where wx_openid = ? and comp_id = ?";
        Connection connection = null;
        String billSql = "select bb.*,price+sale_price-pay_price+overdue_cost aaa from   f_bill bb where   bb.id in (" + listBillId + ")   ";
        String insertf_finance_record_draftSql = "insert into f_finance_record_draft (comp_id,comm_id,no,oper_type,cost,owner_id,payment_method,created_at,account_id) values(?,?,?,?,?,?,?,?,?)";
        String insertf_finance_bill_record_draftSql = "insert into f_finance_bill_record_draft (comp_id,comm_id,finance_record_id,bill_id,cost,created_at)values (?,?,?,?,?,now())";

        try {
            ResultSet resultSet = ConnectUtil.executeQuery(ownerSql, new Object[]{openid, compId});
            if (resultSet.next()) {
                ownerId = resultSet.getInt("id");
            }
            Object[] objects = {compId, commId, no, oper_type, pay, ownerId, payment_method, created_at, account_id};
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

            connection.commit();
            payRequest.setOpenid(openid);
            payRequest.setOrderAmount(0.01);
//            payRequest.setOrderAmount(pay.doubleValue());
            payRequest.setOrderId(integer + "");
            payRequest.setOrderName(orderName);

            payRequest.setPayTypeEnum(BestPayTypeEnum.WXPAY_MP);

            PayResponse payResponse = bestPayService.pay(payRequest);
            log.info("pay:{}", payResponse);
            return payResponse;
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
        return null;
    }

    @Override
    public String notify(String data) {
        PayResponse notify = bestPayService.asyncNotify(data);
        log.info("返回结果是：{}", notify);
        String result = "";
        BigDecimal ycPrice = new BigDecimal(0);
        Double orderAmount = notify.getOrderAmount();
        String orderId = notify.getOrderId();
        String sqs = "select * from f_finance_record_draft where id = ?";
        String orderName = "";
        try {
            ResultSet resultSet1 = ConnectUtil.executeQuery(sqs, new Object[]{orderId});
            if (resultSet1.next()) {
                orderName = resultSet1.getString("oper_type");
                ycPrice = resultSet1.getBigDecimal("cost");
            }
        } catch (SQLException sqlException) {
            sqlException.printStackTrace();
        } catch (ClassNotFoundException classNotFoundException) {
            classNotFoundException.printStackTrace();
        }
        String insertf_finance_bill_recordsql = "insert into f_finance_bill_record (comp_id,comm_id,finance_record_id,bill_id,cost,created_at,created_by,created_name) select comp_id,comm_id,?,bill_id,cost,created_at,created_by,created_name from f_finance_bill_record_draft where finance_record_id = ?";
        String deletef_finance_bill_record_draftSql = "delete from f_finance_bill_record_draft where finance_record_id = ?";

        String insertf_finance_recordSql = "insert into f_finance_record (comp_id,comm_id,no,account_id,oper_type,cost,owner_id,payment_method,remark,created_at,created_by,created_name) select comp_id,comm_id,no,account_id,oper_type,cost,owner_id,payment_method,remark,created_at,created_by,created_name from f_finance_record_draft where id = ? ";
        String deletef_finance_record_draftSql = "delete from f_finance_record_draft where id = ?";
        String sql = "select aa.bill_id,aa.cost,price+sale_price-pay_price+overdue_cost aaa,bb.state,bb.is_payment, pay_price  from f_finance_bill_record_draft aa,f_bill bb  where aa.bill_id = bb.id and finance_record_id = " + orderId;
        Connection connection = null;
        try {
            connection = TransactionConnUtil.getConnection();
                Object[] objects = new Object[]{orderId};
            if (orderName.contains("支付")) {
                if (orderAmount > 0) {
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
                    result = WeChatResources.PAY_SUCCESS_RESULT;
                } else {
                    result = WeChatResources.PAY_ERROR_RESULT;
                }
            } else if (orderName.contains("预存")) {
                log.info("预存金额id:{},name:{}",orderId,orderName);
                TransactionConnUtil.executeUpdate(insertf_finance_recordSql, new Object[]{orderId});
                String selectsql = "select * from f_finance_record_draft where id = ?";
                String updatesql = "update f_account set fee = fee+? where id = ?";
                ResultSet resultSet = TransactionConnUtil.executeQuery(selectsql, objects);
                while (resultSet.next()){
                    int account_id = resultSet.getInt("account_id");
                    TransactionConnUtil.executeUpdate(updatesql,new Object[]{orderAmount,account_id});
                }
                result = WeChatResources.PAY_SUCCESS_RESULT;
            }
                TransactionConnUtil.executeUpdate(deletef_finance_bill_record_draftSql, objects);
                TransactionConnUtil.executeUpdate(deletef_finance_record_draftSql, objects);

                connection.commit();
        } catch (SQLException sqlException) {
            result = WeChatResources.PAY_ERROR_RESULT;
            try {
                connection.rollback();
            } catch (SQLException throwables) {
                throwables.printStackTrace();
            }
            sqlException.printStackTrace();
        } catch (ClassNotFoundException classNotFoundException) {
            classNotFoundException.printStackTrace();
        }

        return result;
    }
}
