package com.estate.timedtask.costrule.excute;

import com.estate.common.constant.BillintMethod;
import com.estate.common.util.ConnectUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.StringUtils;

import java.sql.SQLException;

@Slf4j
public class ExcuteSql {

    // 账单号，物业类型，是否逾期，是否付款，逾期费用计费规则，单价，付款结束时间，账期，公司id，社区id，开始刻度，结束刻度，创建人，物业类型 费用标注id
   private static String sqlBuildArea ="INSERT INTO f_bill (bill_no,property_id,property_type,bill_time,is_overdue,is_payment,overdue_cost,overdue_rule,price,pay_price,sale_price,is_print,is_invoice,pay_end_time,cost_rule_id,account_period,comp_id,comm_id,begin_scale,end_scale,create_name ) SELECT ?,aa.property_id,?,now(),?,?,0,?,? * bb.building_area,0,0,'否','否',?,aa.cost_rule_id,?,?,?,?,?,? FROM f_cost_rule_room aa,r_room bb WHERE aa.property_id = bb.id and aa.property_type=? and aa.property_id not in(SELECT property_id from f_bill where cost_rule_id = ? and property_type=? and property_id = aa.property_id and account_period = ? and is_payment = '是' and pay_price <> 0) and aa.cost_rule_id = ?";

    private static String sqlUseArea ="INSERT INTO f_bill (bill_no,property_id,property_type,bill_time,is_overdue,is_payment,overdue_cost,overdue_rule,price,pay_price,sale_price,is_print,is_invoice,pay_end_time,cost_rule_id,account_period,comp_id,comm_id,begin_scale,end_scale,create_name ) SELECT ?,aa.property_id,?,now(),?,?,0,?,? * bb.building_area,0,0,'否','否',?,aa.cost_rule_id,?,?,?,?,?,? FROM f_cost_rule_room aa,r_room bb WHERE aa.property_id = bb.id and aa.property_type=? and aa.property_id not in(SELECT property_id from f_bill where cost_rule_id = ? and property_type=? and property_id = aa.property_id and account_period = ? and is_payment = '是' and pay_price <> 0)  AND aa.cost_rule_id = ?";
    // 账单号，物业类型，是否逾期，是否付款，逾期费用计费规则，单价，付款结束时间，账期，公司id，社区id，创建人， 物业类型，水表电表燃气表，费用标注id
    private static String sqlMeter = "INSERT INTO f_bill (bill_no,property_id,property_type,bill_time,is_overdue,is_payment,overdue_cost,overdue_rule,price,pay_price,sale_price,is_print,is_invoice,pay_end_time,cost_rule_id,account_period,comp_id,comm_id,begin_scale,end_scale,create_name) SELECT ?,aa.property_id,?,now(),?,?,0,?,? * (bb.new_num-bb.bill_num),0,0,'否','否',?,aa.cost_rule_id,?,?,?,bill_num,new_num,? FROM f_cost_rule_room aa,f_meter bb WHERE aa.property_id = bb.property_id AND aa.property_type =? and bb.type=? and aa.property_id not in(SELECT property_id from f_bill where cost_rule_id = ? and property_type=? and property_id = aa.property_id and account_period = ? and is_payment = '是' and pay_price <> 0) AND aa.cost_rule_id = ?";
    // 账单号,是否逾期，是否付款，逾期费用计费规则,单价，周期，付款结束时间，账期，公司id，社区id，创建人 物业类型 费用标注id
    private static String sqlFixed ="INSERT INTO f_bill (bill_no,property_id,property_type,bill_time,is_overdue,is_payment,overdue_cost,overdue_rule,price,pay_price,sale_price,is_print,is_invoice,pay_end_time,cost_rule_id,account_period,comp_id,comm_id,create_name ) SELECT ?,aa.property_id,aa.property_type,now(),?,?,0,?,? * ?,0,0,'否','否',?,aa.cost_rule_id,?,?,?,? FROM f_cost_rule_room aa WHERE aa.property_type=? and aa.property_id not in(SELECT property_id from f_bill where cost_rule_id = ? and property_type=? and property_id = aa.property_id and account_period = ? and is_payment = '是' and pay_price <> 0) and aa.cost_rule_id = ?";


    //private static String sql = "insert into f_bill (bill_no,property_id,property_type,bill_time,is_overdue,is_payment,overdue_cost,overdue_rule,price,pay_price,sale_price,is_print,is_invoice,pay_end_time,cost_rule_id,account_period,comp_id,comm_id,begin_scale,end_scale,create_name) values(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,'定时任务')";

    public static Integer executeSql(String sql, Object[] objects,String type) throws SQLException, ClassNotFoundException {

        if (StringUtils.isEmpty(sql) && BillintMethod.BUILDAREA.equals(type)) {
            sql = ExcuteSql.sqlBuildArea;
        }
        if (StringUtils.isEmpty(sql) && BillintMethod.USEAREA.equals(type)) {
            sql = ExcuteSql.sqlUseArea;
        }
        if (StringUtils.isEmpty(sql) && (BillintMethod.WATERMETER.equals(type) || BillintMethod.AMMETER.equals(type)||BillintMethod.GASMETER.equals(type)||BillintMethod.WATERMETER.equals(type))) {
            sql = ExcuteSql.sqlMeter;
        }
        if (StringUtils.isEmpty(sql) && BillintMethod.FIXED.equals(type)) {
            sql = ExcuteSql.sqlFixed;
        }
        Integer integer = ConnectUtil.executeUpdate(sql, objects);
        log.info("执行的添加账单sql:{}",sql);
        String update = "update f_bill set bill_no = id";
        ConnectUtil.executeUpdate(update,new Object[0]);
        log.info("执行的更新账单号sql:{}",update);
        return integer;
    }

    public static Integer executeSql(Object[] objects,String type) throws SQLException, ClassNotFoundException {
        return ExcuteSql.executeSql(null, objects,type);
    }


}
