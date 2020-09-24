package com.estate.timedtask.costrule.util;

import com.estate.common.util.ConnectUtil;
import com.estate.common.constant.BillintMethod;
import com.estate.common.util.TransactionConnUtil;
import com.estate.timedtask.costrule.excute.ExcuteSql;
import lombok.extern.slf4j.Slf4j;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

/**
 * 　　* @description: 账单周期是月的
 *
 * @author mmq
 * 　　* @date 2020/9/2 9:10
 */
@Slf4j
public class MonthUtil {


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
    public static void monthBill( String comp_id,
                                 String liquidated_damages_method, Date date, BigDecimal price, String billing_method, String type,
                                 String thisMonth, long cost_rule_id, int compId, int comm_id) {
        // 物业费、建筑面积
        if (BillintMethod.BUILDAREA.equals(billing_method)) {
            MonthUtil.monthBillEstate(comp_id, liquidated_damages_method, date, price, type, thisMonth, cost_rule_id, compId, comm_id, billing_method);
        }
        // 暖气费用 、使用面积
        if (BillintMethod.USEAREA.equals(billing_method)) {
            MonthUtil.monthBillHot(comp_id, liquidated_damages_method, date, price, type, thisMonth, cost_rule_id, compId, comm_id, billing_method);
        }
        // 水表
        if (BillintMethod.WATERMETER.equals(billing_method)) {
            MonthUtil.monthBillWater(comp_id, price, compId, comm_id, type, liquidated_damages_method, date, thisMonth, type, cost_rule_id, BillintMethod.WATERMETER, BillintMethod.WATERMETER);
        }
        // 电表
        if (BillintMethod.AMMETER.equals(billing_method)) {
            MonthUtil.monthBillWater(comp_id, price, compId, comm_id, type, liquidated_damages_method, date, thisMonth, type, cost_rule_id, BillintMethod.AMMETER, BillintMethod.AMMETER);
        }
        // 煤气表
        if (BillintMethod.GASMETER.equals(billing_method)) {
            MonthUtil.monthBillWater(comp_id, price, compId, comm_id, type, liquidated_damages_method, date, thisMonth, type, cost_rule_id, BillintMethod.GASMETER, BillintMethod.GASMETER);
        }
        // 定额
        if (BillintMethod.FIXED.equals(billing_method)) {
            MonthUtil.monthBillFixed(comp_id,liquidated_damages_method,price,thisMonth,cost_rule_id,compId,comm_id,date,billing_method,type);
        }
        // 临时收费
        if (BillintMethod.TEMPORARY.equals(billing_method)) {
            MonthUtil.monthBillFixed(comp_id,liquidated_damages_method,price,thisMonth,cost_rule_id,compId,comm_id,date,billing_method,type);
        }
    }


    /**
     *
     * @param comp_id 公司id 0001 格式
     * @param liquidated_damages_method 违约金计算方式
     * @param date 最后付款时间
     * @param price 单价
     * @param type 物业类型
     * @param thisMonth 账期
     * @param cost_rule_id 费用标注 id
     * @param compId 公司id
     * @param commId 社区id
     * @param billMethod 计费方式
     */
    public static void monthBillEstate(String comp_id, String liquidated_damages_method,
                                       Date date, BigDecimal price, String type, String thisMonth, long cost_rule_id, int compId, int commId, String billMethod) {

        Object[] obj = MonthUtil.getObj(CalendarUtil.getTimeMillis(new Date()) + comp_id, type, "否", "否", liquidated_damages_method, price, date, thisMonth, compId, commId, null, null, "定时任务",  cost_rule_id);
        try {
            ExcuteSql.executeSql(obj, billMethod);
        } catch (SQLException sqlException) {
            sqlException.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    /**
     *
     * @param comp_id 公司id 0001 格式
     * @param liquidated_damages_method 违约金计算方式
     * @param date 最后付款时间
     * @param price 单价
     * @param type 物业类型
     * @param thisMonth 账期
     * @param cost_rule_id 费用标注 id
     * @param compId 公司id
     * @param commId 社区id
     * @param billMethod 计费方式
     */
    public static void monthBillHot(String comp_id, String liquidated_damages_method,
                                       Date date, BigDecimal price, String type, String thisMonth, long cost_rule_id, int compId, int commId, String billMethod) {

        Object[] obj = MonthUtil.getObj(CalendarUtil.getTimeMillis(new Date()) + comp_id, type, "否", "否", liquidated_damages_method, price, date, thisMonth, compId, commId, null, null, "定时任务",  cost_rule_id);
        try {
            ExcuteSql.executeSql(obj, billMethod);
        } catch (SQLException sqlException) {
            sqlException.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }


    /**
     * @param comp_id      公司id 0001 格式
     * @param price        价格 单价
     * @param compId       公司id
     * @param commId       社区id
     * @param type         物业类型
     * @param overdue_rule 逾期规则
     * @param payEndTime   结束付款时间
     * @param bill         账期
     * @param types        物业类型 room park
     * @param cost_rule_id 费用标准id
     * @param meterType    水表电表燃气表
     * @param billMethod   计费方式
     */
    public static void monthBillWater(String comp_id, BigDecimal price, int compId, int commId, String type, String overdue_rule, Date payEndTime, String bill, String types, long cost_rule_id, String meterType, String billMethod) {
        // 账单号，物业类型，是否逾期，是否付款，逾期费用计费规则，单价，付款结束时间，账期，公司id，社区id，创建人， 物业类型，水表电表燃气表，费用标注id
        Object[] obj = {CalendarUtil.getTimeMillis(new Date()) + comp_id, type, "否", "否", overdue_rule, price, payEndTime, bill, compId, commId, "定时任务", types, meterType, cost_rule_id};
            Connection connection = null;
        try {
            connection = TransactionConnUtil.getConnection();
            String sqlMeter = "INSERT INTO f_bill (bill_no,property_id,property_type,bill_time,is_overdue,is_payment,overdue_cost,overdue_rule,price,pay_price,sale_price,is_print,is_invoice,pay_end_time,cost_rule_id,account_period,comp_id,comm_id,begin_scale,end_scale,create_name,state) SELECT ?,aa.property_id,?,now(),?,?,0,?,? * (bb.new_num-bb.bill_num),0,0,'否','否',?,aa.cost_rule_id,?,?,?,bill_num,new_num,?,'未支付' FROM f_cost_rule_room aa,f_meter bb WHERE aa.property_id = bb.property_id AND aa.property_type =? and aa.property_type=bb.property_type and bb.type=? AND aa.cost_rule_id = ?";
            TransactionConnUtil.executeUpdate(sqlMeter, obj);
            String sql = "update f_meter aa,f_cost_rule_room bb,f_bill cc set aa.bill_num=aa.new_num where aa.property_id=bb.property_id and bb.cost_rule_id=? and aa.type=? and (cc.is_payment = '否' or cc.pay_price = 0)  and cc.property_id=aa.property_id and cc.property_type=aa.property_type and cc.property_type=?";
            Object[] update = {cost_rule_id,meterType,types};
            TransactionConnUtil.executeUpdate(sql,update);
            String updateBill = "update f_bill set bill_no = id";
            TransactionConnUtil.executeUpdate(updateBill,new Object[0]);
            connection.commit();
        } catch (SQLException sqlException) {
            try {
                connection.rollback();
            } catch (SQLException throwables) {
                throwables.printStackTrace();
            }
            sqlException.printStackTrace();
        } catch (ClassNotFoundException e) {
            try {
                connection.rollback();
            } catch (SQLException sqlException) {
                sqlException.printStackTrace();
            }
            e.printStackTrace();
        }
    }

    /**
     *
     * @param comp_id 公司id 0001 格式
     * @param liquidated_damages_method  违约金计算方式
     * @param price 单价
     * @param thisMonth 账期
     * @param cost_rule_id 费用标准id
     * @param compId 公司id
     * @param commId 社区id
     * @param payEndTime 结束付款时间
     * @param billMethod 计费方式
     */
    public static void monthBillFixed(String comp_id, String liquidated_damages_method, BigDecimal price, String thisMonth, long cost_rule_id, int compId, int commId, Date payEndTime, String billMethod,String type) {
        // 账单号,是否逾期，是否付款，逾期费用计费规则,单价，周期，付款结束时间，账期，公司id，社区id，创建人 物业类型 费用标注id
        Object[] obj = {CalendarUtil.getTimeMillis(new Date()) + comp_id, "否", "否", liquidated_damages_method, price, 1, payEndTime, thisMonth, compId, commId, "定时任务",type, cost_rule_id,type,thisMonth,cost_rule_id};
        try {
            ExcuteSql.executeSql(obj, billMethod);
        } catch (SQLException sqlException) {
            sqlException.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    /**
     * 计算总价格
     *
     * @param price 单价
     * @param eff   有效的使用或者固定数值
     * @return 返回一个BigDecimal
     */
    public static BigDecimal totalPrice(BigDecimal price, BigDecimal eff) {
        return price.multiply(eff);
    }

    public static String dataToString(Date date, String fmt) {
        SimpleDateFormat sdf = new SimpleDateFormat(fmt);
        String format = sdf.format(date);

        return format;
    }
    // 账单号，物业类型，是否逾期，是否付款，逾期费用计费规则，单价，付款结束时间，账期，公司id，社区id，开始刻度，结束刻度，创建人，物业类型 费用标注id

    /**
     * @param billNo       账单号
     * @param type         物业类型
     * @param is_overdue   是否逾期
     * @param isPay        是否付款
     * @param overdue_rule 逾期费用计费规则
     * @param price        单价
     * @param payEndTime   付款结束时间
     * @param bill         账期
     * @param compId       公司id
     * @param commId       社区id
     * @param begin_scale  开始刻度
     * @param end_scale    结束刻度
     * @param createName   创建人
     * @param costRuleId   费用标注id
     * @return
     */
    private static Object[] getObj(String billNo, String type, String is_overdue, String isPay, String overdue_rule, BigDecimal price, Date payEndTime, String bill, int compId, int commId, Long begin_scale, Long end_scale, String createName,  Long costRuleId) {
        Object[] obj = {billNo, type, is_overdue, isPay, overdue_rule, price, payEndTime, bill, compId, commId, begin_scale, end_scale, createName,type, costRuleId,type,bill, costRuleId};
        return obj;
    }
}
