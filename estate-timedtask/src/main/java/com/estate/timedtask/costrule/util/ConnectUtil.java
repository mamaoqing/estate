package com.estate.timedtask.costrule.util;

import java.sql.*;
import java.util.Map;
import java.util.Properties;
import java.util.concurrent.Executor;

public class ConnectUtil {

    private static Connection connection = null;
    private static PreparedStatement preparedStatement = null;
    private static ResultSet resultSet = null;

    public static Connection getConnection() throws ClassNotFoundException, SQLException {
        if (connection == null) {
            Class.forName("org.mariadb.jdbc.Driver");
            connection = DriverManager.getConnection("jdbc:mysql://192.168.0.114:3306/wygl", "root", "sdzy@123");
        }
        return connection;
    }

    public static Integer executeUpdate(String sql, String[] objects) throws SQLException, ClassNotFoundException {
        preparedStatement = getConnection().prepareStatement(sql);
        for (int i = 0; i < objects.length; i++) {
            preparedStatement.setString(i + 1, objects[i]);
        }
        return preparedStatement.executeUpdate();
    }

    public static ResultSet executeQuery(String sql, String[] objects) throws SQLException, ClassNotFoundException {
        preparedStatement = getConnection().prepareStatement(sql);
        for (int i = 0; i < objects.length; i++) {
            preparedStatement.setString(i + 1, objects[i]);
        }
        resultSet = preparedStatement.executeQuery();
        return resultSet;
    }

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
