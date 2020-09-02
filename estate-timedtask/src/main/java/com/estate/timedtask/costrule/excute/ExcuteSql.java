package com.estate.timedtask.costrule.excute;

import com.estate.common.util.ConnectUtil;
import org.springframework.util.StringUtils;

import java.sql.SQLException;

public class ExcuteSql {

    private static String sql = "insert into f_bill (bill_no,property_id,property_type," +
            "bill_time,is_overdue,is_payment,overdue_cost,overdue_rule,price,pay_price,sale_price,is_print,is_invoice,pay_end_time,cost_rule_id)" +
            "values(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";

    public static Integer esecuteSQL(String sql , Object[] objects) throws SQLException, ClassNotFoundException {

        if (StringUtils.isEmpty(sql)) {
            sql = ExcuteSql.sql;
        }
        Integer integer = ConnectUtil.executeUpdate(sql, objects);
        return integer;
    }

    public static Integer esecuteSQL(Object[] objects) throws SQLException, ClassNotFoundException {
        return ExcuteSql.esecuteSQL(null, objects);
    }
}
