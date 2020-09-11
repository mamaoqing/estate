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
        // 每半年
        if (BillCycle.HALFAYEAR.equals(billCycle)) {
            CreateBillDateUtil.bannian(begin, end, day, costRuleId);
        }
        // 没两个月
        if (BillCycle.TWOYEAR.equals(billCycle)) {
            CreateBillDateUtil.createTwoMonthBillDate(begin, end, day, costRuleId);
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
//            calendar.setTime(begin);
//            calendar.add(Calendar.DATE, day);
//            Object[] obj = {costRuleId, calendar.getTime()};
//            CreateBillDateUtil.executeSql(obj);
//            // 下一个月
//            int next = calendar.getActualMaximum(Calendar.DAY_OF_MONTH);
//            calendar.add(Calendar.DAY_OF_MONTH, next);
//            calendar.add(Calendar.DATE, day);
//            Date time = calendar.getTime();
//            CreateBillDateUtil.createMonthBillDate(time, end, day, costRuleId);
            calendar.setTime(begin);
            int year = calendar.get(Calendar.YEAR);
            int month = calendar.get(Calendar.MONTH) + 1;
            String fmt = year + "年" + month;
            calendar.set(Calendar.DAY_OF_MONTH, 1);
            calendar.add(Calendar.MONTH, 1);
            Date time = calendar.getTime();
            calendar.add(Calendar.DATE, day);
            Object[] obj = {costRuleId, calendar.getTime(), fmt, time};
            CreateBillDateUtil.executeSql(obj);
            CreateBillDateUtil.createMonthBillDate(time, end, day, costRuleId);
        }
    }

    /**
     * 每2月生成一次账单
     *
     * @param begin      开始时间
     * @param end        结束时间
     * @param day        账单日
     * @param costRuleId 费用标准的id
     */
    public static void createTwoMonthBillDate(Date begin, Date end, Integer day, Long costRuleId) {
        Calendar calendar = Calendar.getInstance();
        // 判断时间是否正确
        if (begin.before(end)) {
//            calendar.setTime(begin);
//            calendar.add(Calendar.DATE, day);
//            Object[] obj = {costRuleId, calendar.getTime()};
//            CreateBillDateUtil.executeSql(obj);
//            // 下一个月
//            int next = calendar.getActualMaximum(Calendar.DAY_OF_MONTH);
//            calendar.add(Calendar.DAY_OF_MONTH, next);
//            calendar.add(Calendar.DATE, day);
//            Date time = calendar.getTime();
//            CreateBillDateUtil.createMonthBillDate(time, end, day, costRuleId);
            calendar.setTime(begin);
            int year = calendar.get(Calendar.YEAR);
            int month = calendar.get(Calendar.MONTH) + 1;
            String fmt = "";
            if (month + 1 > 12) {
                fmt = year + "年" + month +"、"+year+1+"年"+ "1月账单";
            } else {
                fmt = year + "年" + month + "、" + (month + 1) + "月账单";
            }
            calendar.set(Calendar.DAY_OF_MONTH, 1);
            calendar.add(Calendar.MONTH, 2);
            Date time = calendar.getTime();
            calendar.add(Calendar.DATE, day);
            Object[] obj = {costRuleId, calendar.getTime(), fmt, time};
            CreateBillDateUtil.executeSql(obj);
            CreateBillDateUtil.createTwoMonthBillDate(time, end, day, costRuleId);
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
            int year = calendar.get(Calendar.YEAR);
            int month = calendar.get(Calendar.MONTH) + 1;
            int day = calendar.get(Calendar.DAY_OF_MONTH);
            String fmt = year + "年" + month + "月" + day;
            calendar.add(Calendar.DATE, 1);
            Date time = calendar.getTime();
            Object[] obj = {costRuleId, calendar.getTime(), fmt, time};
            CreateBillDateUtil.executeSql(obj);
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
        String sql = "insert into f_bill_date(cost_rule_id,create_bill_date,account_period,end_time) values(?,?,?,?)";
        Calendar calendar = Calendar.getInstance();
        if (begin.before(end)) {
            calendar.setTime(begin);
            // 获取月份,如果月份是1 4 7 10，表示是季度开始的月份，否则，就将当前月份+1在次执行
            int currentMonth = calendar.get(Calendar.MONTH) + 1;
            int year = 0;
            String fmt = "";
            Date year1 = null;
            switch (currentMonth) {
                case 1:
                case 2:
                case 3:
                    year = calendar.get(Calendar.YEAR);
                    fmt = year + "年，第一季度 1，2，3月份账单";
                    calendar.set(Calendar.DAY_OF_MONTH, day);
                    try {
                        year1 = getYear(year, "-04-01");

                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                    break;
                case 4:
                case 5:
                case 6:
                    year = calendar.get(Calendar.YEAR);
                    fmt = year + "年，第二季度 4，5，6月份账单";
                    calendar.set(Calendar.DAY_OF_MONTH, day);
                    try {
                        year1 = getYear(year, "-07-01");
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                    break;
                case 7:
                case 8:
                case 9:
                    year = calendar.get(Calendar.YEAR);
                    fmt = year + "年，第三季度 7，8，9月份账单";
                    calendar.set(Calendar.DAY_OF_MONTH, day);
                    try {
                        year1 = getYear(year, "-10-01");
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                    break;
                case 10:
                case 11:
                case 12:
                    year = calendar.get(Calendar.YEAR);
                    fmt = year + "年，第四季度 10，11，12月份账单";
                    calendar.set(Calendar.DAY_OF_MONTH, day);
                    try {
                        year1 = getYear(year + 1, "-01-01");
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                    break;
                default:
                    break;
            }
            calendar.setTime(year1);
            calendar.add(Calendar.DATE, day);
            CreateBillDateUtil.doSwitch(sql, costRuleId, calendar, fmt, year1);
//            calendar.add(Calendar.MONTH, 3); // 1.9
            calendar.set(Calendar.DAY_OF_MONTH, 1); // 11
            CreateBillDateUtil.createQuarterBillDate(calendar.getTime(), end, day, costRuleId);

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
    public static void bannian(Date begin, Date end, Integer day, Long costRuleId) {
        String sql = "insert into f_bill_date(cost_rule_id,create_bill_date,account_period,end_time) values(?,?,?,?)";
        Calendar calendar = Calendar.getInstance();
        if (begin.before(end)) {
            calendar.setTime(begin);
            // 获取月份,如果月份是1 4 7 10，表示是季度开始的月份，否则，就将当前月份+1在次执行
            int currentMonth = calendar.get(Calendar.MONTH) + 1;
            int year = 0;
            String fmt = "";
            Date year1 = null;
            switch (currentMonth) {
                case 1:
                case 2:
                case 3:
                case 4:
                case 5:
                case 6:
                    year = calendar.get(Calendar.YEAR);
                    fmt = year + "年，上半年 1，2，3，4，5，6月份账单";
                    calendar.set(Calendar.DAY_OF_MONTH, day);
                    try {
                        year1 = getYear(year, "-07-01");
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                    break;
                case 7:
                case 8:
                case 9:
                case 10:
                case 11:
                case 12:
                    year = calendar.get(Calendar.YEAR);
                    fmt = year + "年，下半年 7，8，9，10，11，12月份账单";
                    calendar.set(Calendar.DAY_OF_MONTH, day);
                    try {
                        year1 = getYear(year + 1, "-01-01");
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                    break;
                default:
                    break;
            }
            calendar.setTime(year1);
            calendar.add(Calendar.DATE, day);
            CreateBillDateUtil.doSwitch(sql, costRuleId, calendar, fmt, year1);
            calendar.set(Calendar.DAY_OF_MONTH, 1);
            CreateBillDateUtil.bannian(calendar.getTime(), end, day, costRuleId);

        }
    }

    /**
     * 每年生成一次
     *
     * @param begin      开始时间
     * @param end        结束时间
     * @param day        天数
     * @param costRuleId 费用标准id
     */
    public static void createYearBillDate(Date begin, Date end, Integer day, Long costRuleId) {
        Calendar calendar = Calendar.getInstance();
        if (begin.before(end)) {
            Date year1 = null;
            try {
                calendar.setTime(begin);
                int year = calendar.get(Calendar.YEAR);
                String fmt = year + "年账单";
                year1 = getYear(year + 1, "-01-01");
                // 本年第一天
                calendar.setTime(CreateBillDateUtil.getYear(calendar));
                calendar.add(Calendar.DATE, day);
                Object[] obj = {costRuleId, calendar.getTime(), fmt, year1};
                CreateBillDateUtil.executeSql(obj);
            } catch (ParseException e) {
                e.printStackTrace();
            }
            calendar.add(Calendar.YEAR, 1);
            createYearBillDate(year1, end, day, costRuleId);
        }
    }

    /**
     * 每周执行一次
     *
     * @param begin      开始时间
     * @param end        结束时间
     * @param day        天数
     * @param costRuleId 费用标准id
     */
    public static void createWeekBillDate(Date begin, Date end, Integer day, Long costRuleId) {
        Calendar calendar = Calendar.getInstance();
        if (begin.before(end)) {
            calendar.setTime(begin);
            Date nextWeekMonday = getNextWeekMonday(calendar.getTime());
            calendar.setTime(nextWeekMonday);
            Date time = calendar.getTime();
            String fmt ="周账单";
            calendar.add(Calendar.DATE, day);
            Object[] obj = {costRuleId, calendar.getTime(),"",time};
            CreateBillDateUtil.executeSql(obj);
            CreateBillDateUtil.createWeekBillDate(time, end, day, costRuleId);
        }
    }

    /**
     * 获取当前周的周一的日期
     *
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
     *
     * @param date 时间
     * @return 返回日期下周一
     */
    public static Date getNextWeekMonday(Date date) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(getThisWeekMonday(date));
        cal.add(Calendar.DATE, 7);
        return cal.getTime();
    }

    /**
     * @param sql        需要执行的sql
     * @param costRuleId 费用标准id
     * @param calendar   时间
     * @param fmt        账期
     * @param endTime    账期结束时间
     */
    public static void doSwitch(String sql, Long costRuleId, Calendar calendar, String fmt, Date endTime) {
        Object[] obj = new Object[]{costRuleId, calendar.getTime(), fmt, endTime};
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
     *
     * @param calendar 时间
     * @return 返回一月一号
     * @throws ParseException 异常
     */
    public static Date getYear(Calendar calendar) throws ParseException {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        return sdf.parse(new SimpleDateFormat("yyyy").format(calendar.getTime()) + "-01-01");
    }

    public static Date getYear(int year, String month) throws ParseException {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        return sdf.parse(year + month);
    }

    private static void executeSql(Object[] obj) {
        String sql = "insert into f_bill_date(cost_rule_id,create_bill_date,account_period,end_time) values(?,?,?,?)";
        try {
            ConnectUtil.executeUpdate(sql, obj);
        } catch (SQLException sqlException) {
            sqlException.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }
}
