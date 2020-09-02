package com.estate.common.util;

import com.estate.common.constant.BillCycle;
import com.estate.common.exception.OrderException;

import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * @author mq
 * @description: TODO
 * @title: CreateBillDateUtil
 * @projectName estate-parent
 * @date 2020/9/210:52
 */
public class CreateBillDateUtil {
    /**
     * 生成账单周期时间
     *
     * @param begin 开始时间
     * @param end   结束时间
     * @param day   账单日
     */
    public static void getBillDate(Date begin, Date end, Integer day, Long costRuleId, String billCycle) {
        // 每月
        if (BillCycle.MONTH.equals(billCycle)) {
            CreateBillDateUtil.createMonthBillDate(begin, end, day, costRuleId);
        }
        // 每天
        if (BillCycle.DAY.equals(billCycle)) {
            CreateBillDateUtil.createDayBillDate(begin, end, costRuleId);
        }
        // 每季度
        if (BillCycle.QUARTER.equals(billCycle)) {
            CreateBillDateUtil.createQuarterBillDate(begin, end, day, costRuleId);
        }
        // 每年
        if (BillCycle.YEAR.equals(billCycle)) {
            CreateBillDateUtil.createYearBillDate(begin, end, day, costRuleId);
        }

    }

    /**
     * 每月生成一次账单
     *
     * @param begin      开始时间
     * @param end        结束时间
     * @param day        账单日
     * @param costRuleId 费用标准的id
     * @throws ClassNotFoundException yichang
     */
    public static void createMonthBillDate(Date begin, Date end, Integer day, Long costRuleId) {
        Calendar calendar = Calendar.getInstance();
        // 判断时间是否正确
        if (begin.before(end)) {
            calendar.setTime(begin);
            calendar.set(Calendar.DAY_OF_MONTH, day);
            String sql = "insert into f_bill_date(cost_rule_id,create_bill_date) values(?,?)";
            Object[] obj = {costRuleId, calendar.getTime()};
            try {
                ConnectUtil.executeUpdate(sql, obj);
            } catch (SQLException sqlException) {
                sqlException.printStackTrace();
                throw new OrderException(OrderExceptionEnum.INSERT_BILL_CREATE_DATE_ERROR);
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }
            // 下一个月
            Integer next = calendar.getActualMaximum(Calendar.DAY_OF_MONTH);
            calendar.add(Calendar.DAY_OF_MONTH, next);
            calendar.set(Calendar.DAY_OF_MONTH, day);
            Date time = calendar.getTime();
            CreateBillDateUtil.createMonthBillDate(time, end, day, costRuleId);
        }
    }

    /**
     * 每天生成一次账单
     *
     * @param begin      开始时间
     * @param end        结束时间
     * @param costRuleId 费用标准id
     */
    public static void createDayBillDate(Date begin, Date end, Long costRuleId) {
        Calendar calendar = Calendar.getInstance();
        if (begin.before(end)) {
            calendar.setTime(begin);
            String sql = "insert into f_bill_date(cost_rule_id,create_bill_date) values(?,?)";
            Object[] obj = {costRuleId, calendar.getTime()};
            try {
                ConnectUtil.executeUpdate(sql, obj);
            } catch (SQLException sqlException) {
                sqlException.printStackTrace();
                throw new OrderException(OrderExceptionEnum.INSERT_BILL_CREATE_DATE_ERROR);
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }
            calendar.add(Calendar.DATE, 1);
            Date time = calendar.getTime();
            CreateBillDateUtil.createDayBillDate(time, end, costRuleId);

        }
    }

    /**
     * 每季度生成一次账单
     *
     * @param begin      开始时间
     * @param end        结束时间
     * @param day        天数
     * @param costRuleId 费用标准id
     */
    public static void createQuarterBillDate(Date begin, Date end, Integer day, Long costRuleId) {
        String sql = "insert into f_bill_date(cost_rule_id,create_bill_date) values(?,?)";
        Object[] obj = {};
        Calendar calendar = Calendar.getInstance();
        if (begin.before(end)) {
            calendar.setTime(begin);
            // 获取月份,如果月份是1 4 7 10，表示是季度开始的月份，否则，就将当前月份+1在次执行
            int currentMonth = calendar.get(Calendar.MONTH) + 1;
            switch (currentMonth) {
                case 1:
                case 4:
                case 7:
                case 10:
                    calendar.set(Calendar.DAY_OF_MONTH, day);
                    CreateBillDateUtil.doSwitch(obj, sql, costRuleId, calendar);
                    break;
                default:
                    break;
            }
            Integer next = calendar.getActualMaximum(Calendar.DAY_OF_MONTH);
            calendar.add(Calendar.DAY_OF_MONTH, next);
            calendar.set(Calendar.DAY_OF_MONTH, 1);
            CreateBillDateUtil.createQuarterBillDate(calendar.getTime(), end, day, costRuleId);

        }
    }

    public static void createYearBillDate(Date begin, Date end, Integer day, Long costRuleId) {
        String sql = "insert into f_bill_date(cost_rule_id,create_bill_date) values(?,?)";
        Calendar calendar = Calendar.getInstance();
        if (begin.before(end)) {
            try {
                calendar.setTime(begin);
                // 本年第一天
                calendar.setTime(CreateBillDateUtil.getYear(calendar));
                calendar.add(Calendar.DATE, day);
                Object[] obj = {costRuleId, calendar.getTime()};
                ConnectUtil.executeUpdate(sql, obj);
            } catch (SQLException sqlException) {
                sqlException.printStackTrace();
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            } catch (ParseException e) {
                e.printStackTrace();
            }
            calendar.add(Calendar.YEAR, 1);
            createYearBillDate(calendar.getTime(), end, day, costRuleId);
        }
    }

    public static void doSwitch(Object[] obj, String sql, Long costRuleId, Calendar calendar) {
        obj = new Object[]{costRuleId, calendar.getTime()};
        try {
            ConnectUtil.executeUpdate(sql, obj);
        } catch (SQLException sqlException) {
            sqlException.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    public static Date getYear(Calendar calendar) throws ParseException {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        StringBuilder sb = new StringBuilder();
        sb.append(new SimpleDateFormat("yyyy").format(calendar.getTime())).append("-01-01");
        Date parse = sdf.parse(sb.toString());
        return parse;
    }
}
