package com.estate.timedtask.costrule.excute;

import com.estate.timedtask.costrule.util.ConnectUtil;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ExcuteRule {

    /**
     *  获取到房产，跟停车位的id列表
     * @param id 费用标准id
     * @return 返回一个map
     * @throws SQLException sql
     * @throws ClassNotFoundException 找不到类。mariadb
     */
    public static Map<String,List<Integer>> month(Integer id) throws SQLException, ClassNotFoundException {
        String sql = "select property_type,property_id from f_cost_rule_room where 1=1 and cost_rule_id = "+id;
        ResultSet resultSet = ConnectUtil.executeQuery(sql);
        List<Integer> roomList = new ArrayList<Integer>();
        List<Integer> parkList = new ArrayList<Integer>();
        Map<String,List<Integer>> map = new HashMap<String, List<Integer>>(16);
        while (resultSet.next()){
            String property_type = resultSet.getString("property_type");
            int property_id = resultSet.getInt("property_id");
            if("room".equals(property_type)){
                roomList.add(property_id);
            }
            if("park".equals(property_type)){
                parkList.add(property_id);
            }
        }

        map.put("room",roomList);
        map.put("park",parkList);
        return map;
    }
}
