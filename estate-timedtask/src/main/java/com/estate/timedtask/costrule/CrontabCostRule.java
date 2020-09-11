package com.estate.timedtask.costrule;

import com.estate.common.util.ConnectUtil;
import com.estate.common.constant.BillCycle;
import com.estate.common.util.FormatUtil;
import com.estate.common.util.TransactionConnUtil;
import com.estate.timedtask.costrule.excute.ExcuteRule;
import com.estate.timedtask.costrule.util.*;
import org.springframework.util.StringUtils;

import java.math.BigDecimal;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * @author mq
 */
public class CrontabCostRule {
    public static void main(String[] args) {
        CrontabCostRule.getCostRule();
        ConnectUtil.close();
        TransactionConnUtil.close();
    }

    public static void getCostRule() {
        String sql = "select * from f_bill_date where 1=1 and create_bill_date = ?";
        SimpleDateFormat sdf = FormatUtil.getSdf(FormatUtil.FORMAT_SHORT);
        String format = sdf.format(new Date());
        Date parse = null;
        try {
            parse = sdf.parse(format);
            Object[] obj = {parse};
            ResultSet resultSet = ConnectUtil.executeQuery(sql, obj);
            while (resultSet.next()) {
                int cost_rule_id = resultSet.getInt("cost_rule_id");
                String account_period = resultSet.getString("account_period");
                java.sql.Date create_bill_date = resultSet.getDate("create_bill_date");

                System.out.println(cost_rule_id);
                execute(cost_rule_id, null, null, account_period, false, create_bill_date);
            }
        } catch (ParseException e) {
            e.printStackTrace();
        } catch (SQLException sqlException) {
            sqlException.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    /**
     * 定时任务
     *
     * @param costRuleId     费用标准id
     * @param propertyType   物业类型
     * @param propertyIds    物业id，房产停车位id
     * @param account_period 账期
     * @param flag           手动生成标志，自动为false
     * @param billDay        自动生成账期时间，如果手动点击为null;
     * @throws SQLException           sql
     * @throws ClassNotFoundException yc
     */
    public static void execute(int costRuleId, String propertyType, String propertyIds, String account_period, boolean flag, Date billDay) throws SQLException, ClassNotFoundException {
        Date now = new Date();
        String sql = "select * from f_cost_rule where 1=1 and is_delete = 0 and id=?";
        Object[] arr = {costRuleId};
        ResultSet resultSet = ConnectUtil.executeQuery(sql, arr);
        System.out.println("------------");
        // 获取当前日月
        boolean b = CalendarUtil.compareDate(billDay);
        int thisMonth = CalendarUtil.getMonth(now);
        while (resultSet.next()) {
            Date begin_date = resultSet.getDate("begin_date");
            Date end_date = resultSet.getDate("end_date");
            // 计费方式
            String billing_method = resultSet.getString("billing_method");
            int comm_id = resultSet.getInt("comm_id");
            // 价格
            BigDecimal price = resultSet.getBigDecimal("price");
            // 账单周期
            String bill_cycle = resultSet.getString("bill_cycle");
            // 价格单位
            String price_unit = resultSet.getString("price_unit");
            // 是否有违约金
            String is_liquidated_damages = resultSet.getString("is_liquidated_damages");
            // 违约金计算方式
            String liquidated_damages_method = resultSet.getString("liquidated_damages_method");
            // 出账天
            Integer bill_day = resultSet.getInt("bill_day");
            // 最晚付款时间
            Integer pay_time = resultSet.getInt("pay_time");
            long cost_rule_id = resultSet.getLong("id");
            Date date = CalendarUtil.getDate(now, pay_time);
            // 公司id不足4位用0补齐
            DecimalFormat df = new DecimalFormat("0000");
            int comp_id1 = resultSet.getInt("comp_id");
            // 账单号
            String comp_id = df.format(comp_id1);
            // 费用标准id
            int id = resultSet.getInt("id");

            if (now.after(begin_date) && now.before(end_date)) {
                // 每天
                if (BillCycle.DAY.equals(bill_cycle) && b) {

                }
                // 每周
                if (BillCycle.WEEK.equals(bill_cycle) && b) {

                }
                // 每月
                if (BillCycle.MONTH.equals(bill_cycle)) {
                    MonthUtil.monthBill(comp_id, liquidated_damages_method, date, price, billing_method, "停车位", account_period, cost_rule_id, comp_id1, comm_id);
                    MonthUtil.monthBill(comp_id, liquidated_damages_method, date, price, billing_method, "房产", account_period, cost_rule_id, comp_id1, comm_id);
                }
                // 每季度
                if (BillCycle.QUARTER.equals(bill_cycle) && ((b && isMonth(thisMonth)) || flag)) {
                    if (price_unit.contains("季")) {
                        MonthUtil.monthBill(comp_id, liquidated_damages_method, date, price, billing_method, "房产", account_period, cost_rule_id, comp_id1, comm_id);
                        MonthUtil.monthBill(comp_id, liquidated_damages_method, date, price, billing_method, "停车位", account_period, cost_rule_id, comp_id1, comm_id);
                    }
                    if (price_unit.contains("月")) {
                        MonthUtil.monthBill(comp_id, liquidated_damages_method, date, price.multiply(new BigDecimal(3)), billing_method, "停车位", account_period, cost_rule_id, comp_id1, comm_id);
                        MonthUtil.monthBill(comp_id, liquidated_damages_method, date, price.multiply(new BigDecimal(3)), billing_method, "房产", account_period, cost_rule_id, comp_id1, comm_id);
                    }
                }
            }
            // 每半年
            if (BillCycle.HALFAYEAR.equals(bill_cycle) && (b || flag)) {
                if (price_unit.contains("季")) {
                    MonthUtil.monthBill(comp_id, liquidated_damages_method, date, price.multiply(new BigDecimal(2)), billing_method, "房产", account_period, cost_rule_id, comp_id1, comm_id);
                    MonthUtil.monthBill(comp_id, liquidated_damages_method, date, price.multiply(new BigDecimal(2)), billing_method, "停车位", account_period, cost_rule_id, comp_id1, comm_id);
                } else if (price_unit.contains("月")) {
                    MonthUtil.monthBill(comp_id, liquidated_damages_method, date, price.multiply(new BigDecimal(6)), billing_method, "停车位", account_period, cost_rule_id, comp_id1, comm_id);
                    MonthUtil.monthBill(comp_id, liquidated_damages_method, date, price.multiply(new BigDecimal(6)), billing_method, "房产", account_period, cost_rule_id, comp_id1, comm_id);
                } else {
                    MonthUtil.monthBill(comp_id, liquidated_damages_method, date, price, billing_method, "停车位", account_period, cost_rule_id, comp_id1, comm_id);
                    MonthUtil.monthBill(comp_id, liquidated_damages_method, date, price, billing_method, "房产", account_period, cost_rule_id, comp_id1, comm_id);
                }

            }
            // 每年
            if (BillCycle.YEAR.equals(bill_cycle) && (b || flag)) {
                if (price_unit.contains("季")) {
                    MonthUtil.monthBill(comp_id, liquidated_damages_method, date, price.multiply(new BigDecimal(4)), billing_method, "房产", account_period, cost_rule_id, comp_id1, comm_id);
                    MonthUtil.monthBill(comp_id, liquidated_damages_method, date, price.multiply(new BigDecimal(4)), billing_method, "停车位", account_period, cost_rule_id, comp_id1, comm_id);
                } else if (price_unit.contains("月")) {
                    MonthUtil.monthBill(comp_id, liquidated_damages_method, date, price.multiply(new BigDecimal(12)), billing_method, "停车位", account_period, cost_rule_id, comp_id1, comm_id);
                    MonthUtil.monthBill(comp_id, liquidated_damages_method, date, price.multiply(new BigDecimal(12)), billing_method, "房产", account_period, cost_rule_id, comp_id1, comm_id);
                } else {
                    MonthUtil.monthBill(comp_id, liquidated_damages_method, date, price, billing_method, "停车位", account_period, cost_rule_id, comp_id1, comm_id);
                    MonthUtil.monthBill(comp_id, liquidated_damages_method, date, price, billing_method, "房产", account_period, cost_rule_id, comp_id1, comm_id);
                }

            }
            // 每两月
            if (BillCycle.TWOYEAR.equals(bill_cycle) && (b || flag)) {
                // 价格单位没有时间周期的，是计数的费用
                if (price_unit.contains("月")) {
                    MonthUtil.monthBill(comp_id, liquidated_damages_method, date, price.multiply(new BigDecimal(2)), billing_method, "房产", account_period, cost_rule_id, comp_id1, comm_id);
                    MonthUtil.monthBill(comp_id, liquidated_damages_method, date, price.multiply(new BigDecimal(2)), billing_method, "停车位", account_period, cost_rule_id, comp_id1, comm_id);
                } else {
                    MonthUtil.monthBill(comp_id, liquidated_damages_method, date, price, billing_method, "停车位", account_period, cost_rule_id, comp_id1, comm_id);
                    MonthUtil.monthBill(comp_id, liquidated_damages_method, date, price, billing_method, "房产", account_period, cost_rule_id, comp_id1, comm_id);
                }
            }
        }
    }

    public static boolean isMonth(int month) {
        switch (month) {
            case 4:
            case 7:
            case 10:
            case 1:
                return true;
            default:
                return false;
        }
    }
}
