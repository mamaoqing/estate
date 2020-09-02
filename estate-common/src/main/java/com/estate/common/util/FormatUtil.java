package com.estate.common.util;

import com.estate.common.constant.BillCycle;
import com.estate.common.exception.OrderException;
import org.springframework.util.StringUtils;

import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * @author mq
 * @description: 格式转换
 * @title: FormatUtil
 * @projectName estate-parent
 * @date 2020/9/29:21
 */
public class FormatUtil {
    private static SimpleDateFormat sdf;
    /**
     * 英文简写（默认）如：2010-12-01
     */
    public static String FORMAT_SHORT = "yyyy-MM-dd";
    /**
     * 英文全称  如：2010-12-01 23:15:06
     */
    public static String FORMAT_LONG = "yyyy-MM-dd HH:mm:ss";
    /**
     * 精确到毫秒的完整时间    如：yyyy-MM-dd HH:mm:ss.S
     */
    public static String FORMAT_FULL = "yyyy-MM-dd HH:mm:ss.S";


    public static SimpleDateFormat getSdf(String patton) {
        return new SimpleDateFormat(patton);
    }

    /**
     * 日期格式转字符串
     *
     * @param date
     * @param patton
     * @return
     */
    public static String dateToString(Date date, String patton) {
        if (!StringUtils.isEmpty(patton)) {
            patton = FormatUtil.FORMAT_LONG;
        }
        if (date != null) {
            SimpleDateFormat sdf = FormatUtil.getSdf(patton);
            String format = sdf.format(date);
            return format;
        }
        return "";
    }

    /**
     * 字符串转时间
     *
     * @param date
     * @param patton
     * @return
     * @throws ParseException
     */
    public static Date stringToDate(String date, String patton) throws ParseException {
        if (!StringUtils.isEmpty(patton)) {
            patton = FormatUtil.FORMAT_LONG;
        }

        if (!StringUtils.isEmpty(date)) {
            SimpleDateFormat sdf = FormatUtil.getSdf(patton);
            Date parse = sdf.parse(date);
            return parse;
        }
        return null;
    }
}
