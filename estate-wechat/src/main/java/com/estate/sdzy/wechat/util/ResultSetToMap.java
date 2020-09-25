package com.estate.sdzy.wechat.util;

import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author mq
 * @description: TODO
 * @title: ResultSetToMap
 * @projectName estate-parent
 * @date 2020/9/2111:09
 */
public class ResultSetToMap {

    public static List<Map<String, Object>> resultSetToMap(ResultSet resultSet) {
        Map<String, Object> map = new HashMap<>(16);
        List<Map<String, Object>> list = new ArrayList<>();
        try {
        ResultSetMetaData metaData = resultSet.getMetaData();
            int columnCount = metaData.getColumnCount();
            while (resultSet.next()) {
                Map<String, Object> rowData = new HashMap<String, Object>(16);
                for (int i = 0; i < columnCount; i++) {
                    map.put(metaData.getColumnName(i), resultSet.getObject(i));
                }
                list.add(map);
            }
        } catch (SQLException sqlException) {
            sqlException.printStackTrace();
        }
        return list;
    }
}
