package com.estate.sdzy;

import com.estate.common.util.ConnectUtil;
import com.estate.common.util.TransactionConnUtil;
import lombok.extern.slf4j.Slf4j;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Calendar;
import java.util.Date;
import java.util.UUID;

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
        Connection connection = null;
        Date now = new Date();
        Calendar c = Calendar.getInstance();
        c.setTime(now);
        c.add(Calendar.DATE, -1);
        Date time = c.getTime();
        System.out.println(time);
        String sql = "select aa.id account_id,aa.owner_id ,fee,cc.* from f_account aa,f_account_cost_item bb,f_bill cc where aa.id = bb.account_id and bb.property_id = cc.property_id and bb.property_type = cc.property_type and bb.rule_id = cc.cost_rule_id and cc.bill_time > ?";
        Object[] objects = {time};
        ResultSet resultSet = null;
        try {
            resultSet = ConnectUtil.executeQuery(sql, objects);
            while (resultSet.next()) {
                long owner_id = resultSet.getLong("owner_id");
                long account_id = resultSet.getLong("account_id");
                // billId
                long id = resultSet.getLong("id");
                long compId = resultSet.getLong("comp_id");
                long commId = resultSet.getLong("comm_id");
                String billNo = resultSet.getString("bill_no");
                String sqls = "select fee from f_account where id = "+account_id;
                ResultSet set = ConnectUtil.executeQuery(sqls);
                BigDecimal fee = new BigDecimal(0);
                while (set.next()){
                    fee = set.getBigDecimal(1);
                }
                System.out.println(fee+"<=--==-=-=-=-=>");
                BigDecimal price = resultSet.getBigDecimal("price");
                BigDecimal sale_price = resultSet.getBigDecimal("sale_price");
                // 扣款省下的余额
                BigDecimal money = new BigDecimal(0);
                // 付的钱
                BigDecimal payPrice = new BigDecimal(0);
                // 是否支付
                String payMent = "否";
                String operType = "支付";
                String state = "未支付";
                String payment_method = "自动扣费";
                String no = UUID.randomUUID().toString().replace("-", "");
                // 保证总金额和余额不为空
                if (null == price) {
                    price = new BigDecimal(0);
                }
                if (null == sale_price) {
                    sale_price = new BigDecimal(0);
                }
                price = price.subtract(sale_price);
                if (null == fee) {
                    fee = new BigDecimal(0);
                }
                // 账户余额不小于账单总金额
                if (!(fee.compareTo(price) < 1)) {
                    money = fee.subtract(price);
                    payPrice = price;
                    payMent = "是";
                    state = "已支付";
                } else {
                    payPrice = fee;
                }
                connection = TransactionConnUtil.getConnection();
                // 添加f_finance_record数据
                String insertFinanceFRecordSql = "insert into f_finance_record(comp_id,comm_id,no,account_id,oper_type,cost,owner_id,payment_method,created_at) " +
                        "values(?,?,?,?,?,?,?,?,?)";
                Object[] objs = {compId, commId, no, account_id, operType, payPrice, owner_id, payment_method,now };
                Integer integer = TransactionConnUtil.executeUpdate(insertFinanceFRecordSql, objs, true);

                // 添加f_finance_bill_record数据
                String insertFinanceBillRecord = "insert into f_finance_bill_record(comp_id,comm_id,finance_record_id,bill_id,cost,created_at)" +
                                                                            "values(?,?,?,?,?,?)";
                Object[] o = {compId, commId,integer,id,payPrice,now};
                TransactionConnUtil.executeUpdate(insertFinanceBillRecord,o);
                // 更新f_account
                String updateAccount = "update f_account set fee = ? where id = ?";
                Object[] accout = {money,account_id};
                Integer integer2 = TransactionConnUtil.executeUpdate(updateAccount, accout);
                System.out.println(integer2+"||--------------------||"+payPrice);

                // 更新f_bill
                String updateBill = "update f_bill set pay_price = ? , is_payment = ? ,state = ? where id = ?";
                Object[] b = {payPrice,payMent,state,id};
                Integer integer1 = TransactionConnUtil.executeUpdate(updateBill, b);

                // 提交事务。
                connection.commit();
            }
        } catch (SQLException sqlException) {
            try {
                connection.rollback();
            } catch (SQLException throwables) {
                throwables.printStackTrace();
            }
            sqlException.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }

    }

}
