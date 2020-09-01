package com.estate.timedtask.costrule.util;

import com.estate.timedtask.costrule.excute.ExcuteSql;

import java.math.BigDecimal;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class MonthUtil {


    public static void monthBill(List<Integer> room, String comp_id, String liquidated_damages_method, Date date, BigDecimal price, String billing_method) {
        // 物业费
        if (BillintMethod.BUILDAREA.equals(billing_method)) {
            MonthUtil.monthBillEstate(room, comp_id, liquidated_damages_method, date, price);
        }
        // 水表
        if (BillintMethod.WATERMETER.equals(billing_method)) {
            MonthUtil.monthBillWater(room, comp_id, liquidated_damages_method, date, price, "水表");
        }
        // 电表
        if (BillintMethod.AMMETER.equals(billing_method)) {
            MonthUtil.monthBillWater(room, comp_id, liquidated_damages_method, date, price, "电表");
        }
        // 煤气表
        if (BillintMethod.GASMETER.equals(billing_method)) {
            MonthUtil.monthBillWater(room, comp_id, liquidated_damages_method, date, price, "煤气表");
        }
        // 定额
        if (BillintMethod.FIXED.equals(billing_method)) {
            MonthUtil.monthBillFixed(room, comp_id, liquidated_damages_method, date, price);
        }
        // 临时收费
        if (BillintMethod.TEMPORARY.equals(billing_method)) {
            MonthUtil.monthBillFixed(room, comp_id, liquidated_damages_method, date, price);
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
    public static void monthBillEstate(List<Integer> room, String comp_id, String liquidated_damages_method, Date date, BigDecimal price) {
        if (room != null && !room.isEmpty()) {
            for (Integer res:room) {
                String roomDetail = "select aa.room_no,aa.building_area,bb.name,cc.name,dd.name,ee.name from r_room aa,r_comm_area bb,r_community cc,r_building dd,r_unit ee where aa.comm_area_id = bb.id and aa.comm_id = cc.id and aa.building_id = dd.id and aa.unit_id = ee.id and  aa.id = "+ res;

                String billNo = CalendarUtil.getTimeMillis(new Date()) + comp_id;
                BigDecimal area = new BigDecimal(0);
                String roomSql = "select building_area from r_room where id=" + res;
                try {
                    ResultSet resultSet = ConnectUtil.executeQuery(roomDetail);
                    StringBuilder sb = new StringBuilder();
                    while (resultSet.next()){
                        String roomNo = resultSet.getString(1);
                        BigDecimal buildArea = resultSet.getBigDecimal(2);
                        String commAreaName = resultSet.getString(3);
                        String commName = resultSet.getString(4);
                        String buildName = resultSet.getString(5);
                        String unitName = resultSet.getString(6);
                        sb.append("尊敬的业主，您好,您在").append(commName).append(commAreaName).append(buildName).append(unitName).append("房间号:").append(roomNo)
                                .append("的建筑面积为：").append(buildArea).append("需要交纳的物业费用为:");
                    }
                    ResultSet roomResult = ConnectUtil.executeQuery(roomSql);
                    while (roomResult.next()) {
                        area = roomResult.getBigDecimal("building_area");
                    }
                    // 计算总价格
                    BigDecimal allPrice = MonthUtil.totalPrice(price,area);
                    sb.append(allPrice).append("。请在").append(MonthUtil.dataToString(date,"yyyy-MM-dd")).append("之前交纳。谢谢！");
                    Object[] bill = {billNo, res, "room", new Date(), "否", "否", 0, liquidated_damages_method, allPrice, 0, 0, "否", "否", date};
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
    public static void monthBillWater(List<Integer> room, String comp_id, String liquidated_damages_method, Date date, BigDecimal price, String tableName) {
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
                    Object[] bill = {billNo, res, "room", new Date(), "否", "否", 0, liquidated_damages_method, allPrice, 0, 0, "否", "否", date};
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

    public static void monthBillFixed(List<Integer> room, String comp_id, String liquidated_damages_method, Date date, BigDecimal price) {
        if (room != null && !room.isEmpty()) {
            for (Integer res:room) {
                String billNo = CalendarUtil.getTimeMillis(new Date()) + comp_id;

                try {
                    // 计算总价格
                    BigDecimal allPrice = MonthUtil.totalPrice(price, new BigDecimal(1));
                    Object[] bill = {billNo, res, "room", new Date(), "否", "否", 0, liquidated_damages_method, allPrice, 0, 0, "否", "否", date};
                    // 返回添加的id;
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
