package com.estate.sdzy;

import com.estate.common.util.ConnectUtil;
import com.estate.common.util.TransactionConnUtil;
import lombok.extern.slf4j.Slf4j;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;

/**
 * @author mq
 * @description: TODO
 * @title: DoDebit
 * @projectName estate-parent
 * @date 2020/9/1414:30
 */
@Slf4j
public class DoDebit {

    public static void main(String[] args) {

        Date now = new Date();
        Calendar c = Calendar.getInstance();
        c.setTime(now);
        c.add(Calendar.DATE, -1);
        Date time = c.getTime();
        String sql = "select id,owner_id fee from f_account aa where aa.fee > 0";

        try {
            ResultSet resultSet = TransactionConnUtil.executeQuery(sql);
            while (resultSet.next()) {
                int account_id = resultSet.getInt("id");
                int owner_id = resultSet.getInt("owner_id");
                BigDecimal fee = resultSet.getBigDecimal("fee");
                String billSql = "select bb.*,price-sale_price-pay_price+overdue_cost aaa from f_account_cost_item aa, f_bill bb where aa.property_type = bb.property_type and aa.property_id = bb.property_id and aa.account_id =? and aa.rule_id  = bb.cost_rule_id  and bb.state ='未支付' and is_payment='否'";
                Object[] obj = {account_id};
                ResultSet result = TransactionConnUtil.executeQuery(billSql, obj);
                exeute(result, fee, account_id, now, owner_id);
            }
        } catch (SQLException sqlException) {
            sqlException.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }


    }

    public static void exeute(ResultSet result, BigDecimal fee, int account_id, Date now, int owner_id) {
        Connection connection = null;
        List<Object[]> list = new ArrayList<>();
        BigDecimal count = new BigDecimal(0);
        Object[] objs = null;
        long id,compId = 0,commId = 0L;
        String no = UUID.randomUUID().toString().replace("-", "");
        BigDecimal price = new BigDecimal(0);
        String payMent = "否";
        String operType = "支付";
        String payment_method = "自动扣费";
        try {
            connection = TransactionConnUtil.getConnection();
            while (result.next()) {
                // 账单id
                id = result.getLong("id");
                // 公司id
                compId = result.getLong("comp_id");
                // 社区id
                commId = result.getLong("comm_id");
                // 扣款省下的余额
                BigDecimal money = new BigDecimal(0);
                // 付的钱
                BigDecimal payPrice = new BigDecimal(0);
                // 是否支付
               payMent = "否";
                // 支付状态
                operType = "支付";
                // 扣费方式
                payment_method = "自动扣费";

                // 结算的钱，减去优惠加上违约金减去已经支付的钱。
                price = result.getBigDecimal("aaa");
                if (null == price) {
                    price = new BigDecimal(0);
                }
                if (null == fee) {
                    fee = new BigDecimal(0);
                }
                if (!(fee.compareTo(price) < 1)) {
                    money = fee.subtract(price);
                    payPrice = price;
                    payMent = "是";
                } else {
                    payPrice = fee;
                }
                count.add(price);
                // 账户余额不小于账单总金额
                if ((fee.compareTo(count) < 1)) {
                    break;
                }
                Object[] o = {compId, commId,id,payPrice,now,null};
                // 更新f_bill
                String updateBill = "update f_bill set pay_price = ? , is_payment = ? where id = ?";
                Object[] b = {payPrice,payMent,id};
                TransactionConnUtil.executeUpdate(updateBill, b);
                objs = new Object[]{compId, commId, no, account_id, operType, payPrice, owner_id, payment_method, now};
                list.add(o);
            }
            objs = new Object[]{compId, commId, no, account_id, operType, count, owner_id, payment_method, now};

            // 添加f_finance_record数据
            String insertFinanceFRecordSql = "insert into f_finance_record(comp_id,comm_id,no,account_id,oper_type,cost,owner_id,payment_method,created_at) " +
                    "values(?,?,?,?,?,?,?,?,?)";

            Integer integer = TransactionConnUtil.executeUpdate(insertFinanceFRecordSql, objs, true);
            String updateAccount = "update f_account set fee = ? where id = ?";
            Object[] accout = {fee.subtract(count),account_id};
            
            Integer integer2 = TransactionConnUtil.executeUpdate(updateAccount, accout);
            for (Object[] objects : list) {
                String insertFinanceBillRecord = "insert into f_finance_bill_record(comp_id,comm_id,bill_id,cost,created_at,finance_record_id)" +
                        "values(?,?,?,?,?,?)";
                objects[5] = integer;
                TransactionConnUtil.executeUpdate(insertFinanceBillRecord,objects);
            }
            connection.commit();
        } catch (SQLException | ClassNotFoundException sqlException) {
            try {
                connection.rollback();
            } catch (SQLException throwables) {
                throwables.printStackTrace();
            }
            sqlException.printStackTrace();
        }



    }

}
