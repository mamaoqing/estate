package com.estate.timedtask.costrule.util;

import com.estate.common.util.ConnectUtil;
import com.estate.common.constant.BillintMethod;
import com.estate.timedtask.costrule.excute.ExcuteSql;

import java.math.BigDecimal;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

 /**
 　　* @description: 账单周期是月的
    * @author mmq
 　　* @date 2020/9/2 9:10
 　　*/
public class MonthUtil {


    public static void monthBill(List<Integer> room, String comp_id, String liquidated_damages_method, Date date, BigDecimal price, String billing_method,String type,int thisMonth ,long cost_rule_id,int compId) {
        // 物业费
        if (BillintMethod.BUILDAREA.equals(billing_method)) {
            MonthUtil.monthBillEstate(room, comp_id, liquidated_damages_method, date, price,type,thisMonth,cost_rule_id,compId);
        }
        // 水表
        if (BillintMethod.WATERMETER.equals(billing_method)) {
            MonthUtil.monthBillWater(room, comp_id, liquidated_damages_method, date, price, "水表",compId);
        }
        // 电表
        if (BillintMethod.AMMETER.equals(billing_method)) {
            MonthUtil.monthBillWater(room, comp_id, liquidated_damages_method, date, price, "电表",compId);
        }
        // 煤气表
        if (BillintMethod.GASMETER.equals(billing_method)) {
            MonthUtil.monthBillWater(room, comp_id, liquidated_damages_method, date, price, "煤气表",compId);
        }
        // 定额
        if (BillintMethod.FIXED.equals(billing_method)) {
            MonthUtil.monthBillFixed(room, comp_id, liquidated_damages_method, date, price,type,thisMonth,cost_rule_id,compId);
        }
        // 临时收费
        if (BillintMethod.TEMPORARY.equals(billing_method)) {
            MonthUtil.monthBillFixed(room, comp_id, liquidated_damages_method, date, price,type,thisMonth,cost_rule_id,compId);
        }
    }


    /**
     * 物业费类型的，不需要按照使用量来计算价格。
     *
     * @param room                      房间列表
     * @param comp_id                   公司di
     * @param liquidated_damages_method 违约金计算方式
     * @param date                      最晚付款时间，超出该时间之后，需要按照违约金计算方式来计算利息。
     * @param price                     单价
     */
    public static void monthBillEstate(List<Integer> room, String comp_id, String liquidated_damages_method,
                                       Date date, BigDecimal price,String type,int thisMonth,long cost_rule_id,int compId) {
        if (room != null && !room.isEmpty()) {
            for (Integer res:room) {

                String billNo = CalendarUtil.getTimeMillis(new Date()) + comp_id;
                BigDecimal area = new BigDecimal(0);
                String roomSql = "select building_area from r_room where id=" + res;
                try {
                    StringBuilder sb = new StringBuilder();
                    BaseUtil.say(res,sb,type,thisMonth);
                    ResultSet roomResult = ConnectUtil.executeQuery(roomSql);
                    while (roomResult.next()) {
                        area = roomResult.getBigDecimal("building_area");
                    }
                    // 计算总价格
                    BigDecimal allPrice = MonthUtil.totalPrice(price,area);
                    sb.append(allPrice).append("。请在").append(MonthUtil.dataToString(date,"yyyy-MM-dd")).append("之前交纳。谢谢！");
                    Object[] bill = {billNo, res, type, new Date(), "否", "否", 0, liquidated_damages_method, allPrice, 0, 0, "否", "否", date,cost_rule_id,thisMonth,compId};
                    Integer integer = ExcuteSql.esecuteSQL(bill);
                    ConnectUtil.getConnection().commit();
                    System.out.println(sb.toString());
                } catch (SQLException sqlException) {
                    sqlException.printStackTrace();
                } catch (ClassNotFoundException e) {
                    e.printStackTrace();
                } finally {
                }

            }
        }
    }

    /**
     * 水费电费燃气费类型，需要按照使用量来计算价格。
     *
     * @param room                      房间列表
     * @param comp_id                   公司id
     * @param liquidated_damages_method 违约金计算方式
     * @param date                      最晚付款时间，超出该时间之后，需要按照违约金计算方式来计算利息。
     * @param price                     单价，单价
     */
    public static void monthBillWater(List<Integer> room, String comp_id, String liquidated_damages_method, Date date, BigDecimal price, String tableName,int compId) {
        if (room != null && !room.isEmpty()) {
            for (Integer res:room) {
                String billNo = CalendarUtil.getTimeMillis(new Date()) + comp_id;
                // 查询该房间下的水表电表燃气表使用信息：res；
                String useSql = "select new_num,bill_num from f_meter where property_type= '房产' and type = '" + tableName+"'";
                try {
                    ResultSet resultSet = ConnectUtil.executeQuery(useSql);
                    BigDecimal use = new BigDecimal(0);
                    while (resultSet.next()){
                        BigDecimal new_num = resultSet.getBigDecimal("new_num");
                        BigDecimal bill_num = resultSet.getBigDecimal("bill_num");
                        use = bill_num.subtract(new_num);

                    }
                    // 计算总价格
                    BigDecimal allPrice = MonthUtil.totalPrice(price,use);
                    Object[] bill = {billNo, res, "room", new Date(), "否", "否", 0, liquidated_damages_method, allPrice, 0, 0, "否", "否", date,"",compId};
                    Integer integer = ExcuteSql.esecuteSQL(bill);
                    ConnectUtil.getConnection().commit();
                } catch (SQLException sqlException) {
                    sqlException.printStackTrace();
                } catch (ClassNotFoundException e) {
                    e.printStackTrace();
                } finally {
                }

            }
        }
    }

    public static void monthBillFixed(List<Integer> room, String comp_id, String liquidated_damages_method,
                                      Date date, BigDecimal price,String type,int thisMonth,long cost_rule_id,int compId) {
        if (room != null && !room.isEmpty()) {
            for (Integer res:room) {
                String billNo = CalendarUtil.getTimeMillis(new Date()) + comp_id;

                try {
                    StringBuilder sb = new StringBuilder();
                    BaseUtil.say(res,sb,type,thisMonth);
                    // 计算总价格
                    BigDecimal allPrice = MonthUtil.totalPrice(price, new BigDecimal(1));
                    Object[] bill = {billNo, res, type, new Date(), "否", "否", 0, liquidated_damages_method, allPrice, 0, 0, "否", "否", date,cost_rule_id,thisMonth,compId};
                    // 返回添加的id;
                    sb.append(allPrice).append("元").append("。请在").append(MonthUtil.dataToString(date,"yyyy-MM-dd")).append("之前交纳。谢谢！");
                    System.out.println(sb.toString());
                    Integer integer = ExcuteSql.esecuteSQL(bill);
                    ConnectUtil.getConnection().commit();
                } catch (SQLException sqlException) {
                    sqlException.printStackTrace();
                } catch (ClassNotFoundException e) {
                    e.printStackTrace();
                } finally {

                }

            };
        }
    }

    /**
     * 计算总价格
     * @param price 单价
     * @param eff 有效的使用或者固定数值
     * @return 返回一个BigDecimal
     */
    public static BigDecimal totalPrice(BigDecimal price,BigDecimal eff){
        return price.multiply(eff);
    }

    public static String dataToString(Date date,String fmt){
        SimpleDateFormat sdf = new SimpleDateFormat(fmt);
        String format = sdf.format(date);

        return format;
    }
}
