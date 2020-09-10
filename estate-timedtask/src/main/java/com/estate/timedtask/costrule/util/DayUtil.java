package com.estate.timedtask.costrule.util;

import com.estate.common.constant.BillintMethod;
import com.estate.timedtask.costrule.excute.ExcuteSql;

import java.math.BigDecimal;
import java.sql.SQLException;
import java.util.Date;

/**
 * @author mq
 * @description: TODO
 * @title: DayUtil
 * @projectName estate-parent
 * @date 2020/9/914:17
 */
public class DayUtil {

    /**
     * @param comp_id                   公司id
     * @param liquidated_damages_method 违约金计算方式
     * @param date                      最晚付款时间
     * @param price                     价格
     * @param billing_method            计费方式
     * @param type                      房产停车位等
     * @param thisMonth                 账单月
     * @param cost_rule_id              费用标注id
     * @param compId                    公司id
     * @param comm_id                   社区id
     */
    public static void dayBill(String comp_id,
                               String liquidated_damages_method, Date date, BigDecimal price, String billing_method, String type,
                               String thisMonth, long cost_rule_id, int compId, int comm_id){

        // 定额
        if (BillintMethod.FIXED.equals(billing_method)) {
            DayUtil.dayBillEstate(comp_id,price,compId,comm_id,type,liquidated_damages_method,date,thisMonth,cost_rule_id,billing_method);
        }
        // 临时收费
        if (BillintMethod.TEMPORARY.equals(billing_method)) {
            DayUtil.dayBillEstate(comp_id,price,compId,comm_id,type,liquidated_damages_method,date,thisMonth,cost_rule_id,billing_method);
        }
    }

    public static void dayBillEstate(String comp_id, BigDecimal price, int compId, int commId, String type, String overdue_rule, Date payEndTime, String bill,long cost_rule_id,String billMethod){
        Object[] objs = {CalendarUtil.getTimeMillis(new Date()) + comp_id, "否", "否", overdue_rule, price, 1, payEndTime, bill, compId, commId, "定时任务",type, cost_rule_id};
        try {
            ExcuteSql.executeSql(objs, billMethod);
        } catch (SQLException sqlException) {
            sqlException.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }
}
