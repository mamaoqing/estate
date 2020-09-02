package com.estate.common.util;

import java.sql.*;
import java.util.Map;
import java.util.Properties;
import java.util.concurrent.Executor;

/**
* @description: 数据库连接工具类
* @author mq
*/
public class ConnectUtil {

    private static Connection connection = null;
    private static PreparedStatement preparedStatement = null;
    private static ResultSet resultSet = null;

    /**
     *
     * @return
     * @throws ClassNotFoundException
     * @throws SQLException
     */
    public static Connection getConnection() throws ClassNotFoundException, SQLException {
        if (connection == null) {
            Class.forName("org.mariadb.jdbc.Driver");
            connection = DriverManager.getConnection("jdbc:mysql://192.168.0.114:3306/wygl", "root", "sdzy@123");
        }
        return connection;
    }

    /**
     *
     * @param sql
     * @param objects
     * @return
     * @throws SQLException
     * @throws ClassNotFoundException
     */
    public static Integer executeUpdate(String sql, Object[] objects) throws SQLException, ClassNotFoundException {
        preparedStatement = getConnection().prepareStatement(sql);
        for (int i = 0; i < objects.length; i++) {
            preparedStatement.setObject(i + 1, objects[i]);
        }
        int i = preparedStatement.executeUpdate();
        return i;
    }

    /**
     *
     * @param sql
     * @param objects
     * @return
     * @throws SQLException
     * @throws ClassNotFoundException
     */
    public static ResultSet executeQuery(String sql, String[] objects) throws SQLException, ClassNotFoundException {
        preparedStatement = getConnection().prepareStatement(sql);
        for (int i = 0; i < objects.length; i++) {
            preparedStatement.setString(i + 1, objects[i]);
        }
        resultSet = preparedStatement.executeQuery();
        return resultSet;
    }
    public static ResultSet executeQuery(String sql) throws SQLException, ClassNotFoundException {
        return executeQuery(sql,new String[0]);
    }

    /**
     *
     */
    public static void close() {
        try {
            if (resultSet != null) {
                resultSet.close();
            }
            if (preparedStatement != null) {
                preparedStatement.close();
            }
            if (connection != null) {
                connection.close();
            }
        } catch (SQLException sqlException) {
            sqlException.printStackTrace();
        }
    }
}
