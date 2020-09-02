package com.estate.timedtask.costrule.util;

import java.math.BigDecimal;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;

public class BaseUtil {

    public static void say(Integer res, StringBuilder sb, String type,int thisMonth) {
        if ("room".equals(type)) {
            BaseUtil.room(res, sb,thisMonth);
        } else if ("park".equals(type)) {
            BaseUtil.park(res, sb, thisMonth);
        }
    }

    /**
     *
     * @param res
     * @param sb
     * @param thisMonth
     */
    public static void room(Integer res, StringBuilder sb,int thisMonth) {
        String roomDetail = "select aa.room_no,aa.building_area,bb.name,cc.name,dd.name,ee.name from r_room aa,r_comm_area bb," +
                "r_community cc,r_building dd,r_unit ee where aa.comm_area_id = bb.id and aa.comm_id = cc.id and aa.building_id = dd.id and aa.unit_id = ee.id and  aa.id = " + res;

        try {
            ResultSet resultSet = ConnectUtil.executeQuery(roomDetail);
            while (resultSet.next()) {
                String roomNo = resultSet.getString(1);
                BigDecimal buildArea = resultSet.getBigDecimal(2);
                String commAreaName = resultSet.getString(3);
                String commName = resultSet.getString(4);
                String buildName = resultSet.getString(5);
                String unitName = resultSet.getString(6);
                sb.append("尊敬的业主，您好,您在").append(commName).append("社区，").append(commAreaName).append("分区。").append(buildName).append(unitName).append("房间号:").append(roomNo)
                        .append("的建筑面积为：").append(buildArea).append("需要交纳").append(thisMonth).append("月份的物业费用为:");
            }
        } catch (SQLException sqlException) {
            sqlException.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    public static void park(Integer res, StringBuilder sb,int thisMonth) {
        String roomDetail = "select aa.use_property,aa.no,aa.size,bb.name commName,cc.name commAreaName " +
                "from r_parking_space aa,r_community bb,r_comm_area cc where aa.comm_id = bb.id and aa.comm_area_id=cc.id and aa.id=" + res;

        try {
            ResultSet resultSet = ConnectUtil.executeQuery(roomDetail);
            while (resultSet.next()) {
                String use = resultSet.getString(1);
                String no = resultSet.getString(2);
                String size = resultSet.getString(3);
                String commName = resultSet.getString(4);
                String commAreaName = resultSet.getString(5);
                sb.append("尊敬的业主，您好,您在").append(commName).append("社区，").append(commAreaName).append("分区").append("的").append(size).append("车位编号：").append(no).append("。需交纳").append(thisMonth)
                    .append("月份的停车费，共：");
            }
        } catch (SQLException sqlException) {
            sqlException.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }
}