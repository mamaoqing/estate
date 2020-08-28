package com.estate.timedtask.costrule;

import com.estate.timedtask.costrule.util.ConnectUtil;

import java.sql.ResultSet;
import java.sql.SQLException;

public class CrontabCostRule {
    public static void main(String[] args) throws SQLException, ClassNotFoundException {
//        String sql = "select * from s_user where 1=1";
//        String[] arr = {};
//        ResultSet resultSet = ConnectUtil.executeQuery(sql, arr);
//        while(resultSet.next()){
//            String user_name = resultSet.getString("user_name");
//            System.out.println(user_name);
//        }
//        System.out.println(resultSet);

        String sql = "delete from s_user where id = ?";
        String [] arr = {"111"};
        Integer integer = ConnectUtil.executeUpdate(sql, arr);
        System.out.println(integer);
    }
}
