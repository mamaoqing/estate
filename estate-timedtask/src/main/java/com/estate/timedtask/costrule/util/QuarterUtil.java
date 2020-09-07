package com.estate.timedtask.costrule.util;

import com.estate.common.util.ConnectUtil;
import com.estate.common.constant.BillintMethod;
import com.estate.timedtask.costrule.excute.ExcuteSql;

import java.math.BigDecimal;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;
import java.util.List;

public class QuarterUtil {

    /**
     * 物业费类型的，不需要按照使用量来计算价格。
     *
     * @param room                      房间列表
     * @param comp_id                   公司di
     * @param liquidated_damages_method 违约金计算方式
     * @param date                      最晚付款时间，超出该时间之后，需要按照违约金计算方式来计算利息。
     * @param price                     单价
     */
    public static void quarterBill(List<Integer> room, String comp_id, String liquidated_damages_method,
                                   Date date, BigDecimal price, String tableName,int compId,int commId,String billing_method) {
        // 物业费
        if (BillintMethod.BUILDAREA.equals(billing_method)) {
            QuarterUtil.quarterBillEstate(room, comp_id, liquidated_damages_method, date, price, billing_method);
        }
    }

    /**
     * @param room                      房间列表
     * @param comp_id                   公司id
     * @param liquidated_damages_method 违约金计算方式
     * @param date                      最晚付款时间，超出该时间之后，需要按照违约金计算方式来计算利息。
     * @param price                     单价
     */
    public static void quarterBillEstate(List<Integer> room, String comp_id, String liquidated_damages_method, Date date, BigDecimal price, String billing_method) {
        if (room != null && !room.isEmpty()) {
            for (Integer res : room) {
                String roomDetail = "select aa.room_no,aa.building_area,bb.name,cc.name,dd.name,ee.name from r_room aa,r_comm_area bb,r_community cc,r_building dd,r_unit ee where aa.comm_area_id = bb.id and aa.comm_id = cc.id and aa.building_id = dd.id and aa.unit_id = ee.id and  aa.id = " + res;

                String billNo = CalendarUtil.getTimeMillis(new Date()) + comp_id;
                try {
                    ResultSet resultSet = ConnectUtil.executeQuery(roomDetail);
                    StringBuilder sb = new StringBuilder();
                    while (resultSet.next()) {
                        String roomNo = resultSet.getString(1);
                        BigDecimal buildArea = resultSet.getBigDecimal(2);
                        String commAreaName = resultSet.getString(3);
                        String commName = resultSet.getString(4);
                        String buildName = resultSet.getString(5);
                        String unitName = resultSet.getString(6);
                        sb.append("尊敬的业主，您好,您在").append(commName).append(commAreaName).append(buildName).append(unitName).append("房间号:").append(roomNo)
                                .append("的建筑面积为：").append(buildArea).append("需要交纳的物业费用为:");
                    }

                    // 计算总价格,一季度是三个月
                    BigDecimal area = new BigDecimal(3);
                    BigDecimal allPrice = MonthUtil.totalPrice(price, area);
                    sb.append(allPrice).append("。").append("单价：").append(price).append("。请在").append(MonthUtil.dataToString(date, "yyyy-MM-dd")).append("之前交纳。谢谢！");
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
}
