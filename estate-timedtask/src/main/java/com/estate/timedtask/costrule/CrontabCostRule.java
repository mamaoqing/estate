package com.estate.timedtask.costrule;

import com.estate.common.util.ConnectUtil;
import com.estate.common.constant.BillCycle;
import com.estate.common.util.FormatUtil;
import com.estate.timedtask.costrule.excute.ExcuteRule;
import com.estate.timedtask.costrule.util.*;

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
            while (resultSet.next()){
                int cost_rule_id = resultSet.getInt("cost_rule_id");
                System.out.println(cost_rule_id);
                execute(cost_rule_id);
            }
        } catch (ParseException e) {
            e.printStackTrace();
        } catch (SQLException sqlException) {
            sqlException.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    public static void execute(int costRuleId) throws SQLException, ClassNotFoundException {
        Date now = new Date();
        String sql = "select * from f_cost_rule where 1=1 and is_delete = 0 and id=?";
        Object[] arr = {costRuleId};
        ResultSet resultSet = ConnectUtil.executeQuery(sql, arr);

        // 获取当前日月
        int day = CalendarUtil.getDay(now);
        int thisMonth = CalendarUtil.getMonth(now);
        while (resultSet.next()) {
            Date begin_date = resultSet.getDate("begin_date");
            Date end_date = resultSet.getDate("end_date");
            // 计费方式
            String billing_method = resultSet.getString("billing_method");
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

            // 判断当前时间是否在开始结束时间之间,如果在时间段之内，就需要将该
            if (now.after(begin_date) && now.before(end_date)) {
                // 每天
                if (BillCycle.DAY.equals(bill_cycle) && day == bill_day) {
                    System.out.println(id + "<====>" + BillCycle.DAY);
                }
                // 每周
                if (BillCycle.WEEK.equals(bill_cycle) && day == bill_day) {
                    System.out.println(id + "<====>" + BillCycle.WEEK);
                }
                // 每月
                if (BillCycle.MONTH.equals(bill_cycle) && day == bill_day) {

                    Map<String, List<Integer>> month = ExcuteRule.month(id);
                    List<Integer> room = month.get("room");
                    MonthUtil.monthBill(room, comp_id, liquidated_damages_method, date, price, billing_method, "room", thisMonth, cost_rule_id,comp_id1);

                    List<Integer> park = month.get("park");
                    MonthUtil.monthBill(park, comp_id, liquidated_damages_method, date, price, billing_method, "park", thisMonth, cost_rule_id,comp_id1);

                }
                // 每季度
                if (BillCycle.QUARTER.equals(bill_cycle) && day == bill_day && isMonth(thisMonth)) {
                    System.out.println(id);
                }
                // 每半年
                if (BillCycle.HALFAYEAR.equals(bill_cycle) && day == bill_day) {
                    System.out.println(id);
                }
                // 每年
                if (BillCycle.YEAR.equals(bill_cycle) && day == bill_day) {
                    System.out.println(id);
                }
            }
        }
    }

    public static boolean isMonth(int month) {
        switch (month) {
            case 4:
                return true;
            case 8:
                return true;
            case 10:
                return true;
            case 1:
                return true;
            default:
                return false;
        }
    }
}
