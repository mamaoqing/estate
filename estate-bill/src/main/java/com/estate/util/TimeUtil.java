package com.estate.util;

import java.util.Calendar;
import java.util.Date;

/**
 * @author mq
 * @date 2020/8/17 15:13
 * @description 时间工具
 */
public class TimeUtil {

    public static Date getBeforeDate(Integer day){
        Date date = new Date();
        Calendar calc = Calendar.getInstance();
        calc.setTime(date);
        calc.add(Calendar.DATE,day);

        return calc.getTime();
    }
}
