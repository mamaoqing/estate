package com.estate.timedtask.costrule.excute;

import com.estate.common.constant.BillintMethod;
import com.estate.common.util.ConnectUtil;
import org.springframework.util.StringUtils;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ExcuteRule {

    /**
     * 获取到房产，跟停车位的id列表
     *
     * @param id 费用标准id
     * @param propertyIds 不需要重新生成的物业id
     * @param propertyType 用户重新生成的时候，选择的物业类型。
     * @return 返回一个map
     * @throws ClassNotFoundException 找不到类。mariadb
     */
    public static Map<String, List<Integer>> month(Integer id,String propertyType,String propertyIds,String billing_method) throws ClassNotFoundException {
        Map<String, List<Integer>> map = new HashMap<String, List<Integer>>(16);
        StringBuilder sql = new StringBuilder("select property_type,property_id from f_cost_rule_room where 1=1 and cost_rule_id = " + id);
        StringBuilder meterSql = new StringBuilder("select * from f_meter where 1=1");
        if(!StringUtils.isEmpty(propertyIds) && !StringUtils.isEmpty(propertyType)){
            sql.append(" and property_type = '").append(propertyType).append("' and property_id in(").append(propertyIds).append(")");
        }
        System.out.println(sql.toString());
        ResultSet resultSet = null;
        List<Integer> roomList = new ArrayList<Integer>();
        List<Integer> parkList = new ArrayList<Integer>();
        List<Integer> water = new ArrayList<Integer>();
        List<Integer> an = new ArrayList<Integer>();
        List<Integer> rq = new ArrayList<Integer>();
        try {
            resultSet = ConnectUtil.executeQuery(sql.toString());
            while (resultSet.next()) {
                String property_type = resultSet.getString("property_type");
                int property_id = resultSet.getInt("property_id");
                if ("room".equals(property_type)) {
                    // 水表
                    if(BillintMethod.WATERMETER.equals(billing_method)){

                    }
                    roomList.add(property_id);
                }
                if ("park".equals(property_type)) {
                    parkList.add(property_id);
                }
                if ("water".equals(property_type)) {
                    parkList.add(property_id);
                }
                if ("an".equals(property_type)) {
                    parkList.add(property_id);
                }
                if ("rq".equals(property_type)) {
                    parkList.add(property_id);
                }
            }
        } catch (SQLException sqlException) {
            sqlException.printStackTrace();
        }finally {
        }


        map.put("room", roomList);
        map.put("park", parkList);
        map.put("water", water);
        map.put("an", an);
        map.put("rq", rq);
        return map;
    }
}
