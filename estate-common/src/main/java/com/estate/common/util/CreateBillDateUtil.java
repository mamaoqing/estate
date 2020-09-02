package com.estate.common.util;

import com.estate.common.constant.BillCycle;

import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * @author mq
 * @description:
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
        // 每周
        if (BillCycle.WEEK.equals(billCycle)) {
            CreateBillDateUtil.createWeekBillDate(begin, end, day, costRuleId);
        }

    }

    /**
     * 每月生成一次账单
     *
     * @param begin      开始时间
     * @param end        结束时间
     * @param day        账单日
     * @param costRuleId 费用标准的id
     */
    public static void createMonthBillDate(Date begin, Date end, Integer day, Long costRuleId) {
        Calendar calendar = Calendar.getInstance();
        // 判断时间是否正确
        if (begin.before(end)) {
            calendar.setTime(begin);
            calendar.set(Calendar.DAY_OF_MONTH, day);
            Object[] obj = {costRuleId, calendar.getTime()};
            CreateBillDateUtil.executeSql(obj);
            // 下一个月
            int next = calendar.getActualMaximum(Calendar.DAY_OF_MONTH);
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
            Object[] obj = {costRuleId, calendar.getTime()};
            CreateBillDateUtil.executeSql(obj);
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
                    CreateBillDateUtil.doSwitch(sql, costRuleId, calendar);
                    break;
                default:
                    break;
            }
            int next = calendar.getActualMaximum(Calendar.DAY_OF_MONTH);
            calendar.add(Calendar.DAY_OF_MONTH, next);
            calendar.set(Calendar.DAY_OF_MONTH, 1);
            CreateBillDateUtil.createQuarterBillDate(calendar.getTime(), end, day, costRuleId);

        }
    }

    /**
     * 每年生成一次
     * @param begin 开始时间
     * @param end 结束时间
     * @param day 天数
     * @param costRuleId 费用标准id
     */
    public static void createYearBillDate(Date begin, Date end, Integer day, Long costRuleId) {
        Calendar calendar = Calendar.getInstance();
        if (begin.before(end)) {
            try {
                calendar.setTime(begin);
                // 本年第一天
                calendar.setTime(CreateBillDateUtil.getYear(calendar));
                calendar.add(Calendar.DATE, day);
                Object[] obj = {costRuleId, calendar.getTime()};
                CreateBillDateUtil.executeSql(obj);
            } catch (ParseException e) {
                e.printStackTrace();
            }
            calendar.add(Calendar.YEAR, 1);
            createYearBillDate(calendar.getTime(), end, day, costRuleId);
        }
    }

    /**
     * 每周执行一次
     * @param begin 开始时间
     * @param end 结束时间
     * @param day 天数
     * @param costRuleId 费用标准id
     */
    public static void createWeekBillDate(Date begin, Date end, Integer day, Long costRuleId){
        Calendar calendar = Calendar.getInstance();
        if (begin.before(end)) {
            calendar.setTime(begin);
            Date nextWeekMonday = getNextWeekMonday(calendar.getTime());
            calendar.setTime(nextWeekMonday);
            calendar.add(Calendar.DATE, day);
            Object[] obj = {costRuleId, calendar.getTime()};
            CreateBillDateUtil.executeSql(obj);
            CreateBillDateUtil.createWeekBillDate(calendar.getTime(),end,day,costRuleId);
        }
    }

    /**
     * 获取当前周的周一的日期
     * @param date 传入当前日期
     * @return 返回当前周一日期
     */
    public static Date getThisWeekMonday(Date date) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        // 获得当前日期是一个星期的第几天
        int dayWeek = cal.get(Calendar.DAY_OF_WEEK);
        if (1 == dayWeek) {
            cal.add(Calendar.DAY_OF_MONTH, -1);
        }
        // 设置一个星期的第一天，按中国的习惯一个星期的第一天是星期一
        cal.setFirstDayOfWeek(Calendar.MONDAY);
        // 获得当前日期是一个星期的第几天
        int day = cal.get(Calendar.DAY_OF_WEEK);
        cal.add(Calendar.DATE, cal.getFirstDayOfWeek() - day);
        return cal.getTime();
    }

    /**
     * 获取下周一的日期
     * @param date 时间
     * @return 返回日期下周一
     */
    public static Date getNextWeekMonday(Date date) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(getThisWeekMonday(date));
        cal.add(Calendar.DATE, 7);
        return cal.getTime();
    }

    public static void doSwitch(String sql, Long costRuleId, Calendar calendar) {
        Object[] obj = new Object[]{costRuleId, calendar.getTime()};
        try {
            ConnectUtil.executeUpdate(sql, obj);
        } catch (SQLException sqlException) {
            sqlException.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    /**
     * 获取时间年的一月一号
     * @param calendar 时间
     * @return 返回一月一号
     * @throws ParseException 异常
     */
    public static Date getYear(Calendar calendar) throws ParseException {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        return sdf.parse(new SimpleDateFormat("yyyy").format(calendar.getTime()) + "-01-01");
    }

    private static void executeSql(Object[] obj){
        String sql = "insert into f_bill_date(cost_rule_id,create_bill_date) values(?,?)";
        try {
            ConnectUtil.executeUpdate(sql, obj);
        } catch (SQLException sqlException) {
            sqlException.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }
}
