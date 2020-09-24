package com.estate.sdzy.send;

import com.estate.common.util.ConnectUtil;
import com.estate.common.util.FormatUtil;
import com.estate.sdzy.wechat.entity.Inform;
import com.estate.sdzy.wechat.resource.WeChatResources;
import com.estate.sdzy.wechat.util.SendMessage;
import net.sf.json.JSONObject;
import org.springframework.util.StringUtils;


import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Calendar;
import java.util.Date;

/**
 * @author mq
 * @description: TODO
 * @title: SendApplication
 * @projectName estate-parent
 * @date 2020/9/229:14
 */
public class SendApplication {
    public static void main(String[] args) {
        SendApplication.findComm();
    }

    public static void findComm() {
        String sql = "select id from r_community where is_delete = 0";

        try {
            ResultSet resultSet = ConnectUtil.executeQuery(sql);
            while (resultSet.next()) {
                int id = resultSet.getInt("id");
                findBill(id);
            }
        } catch (SQLException sqlException) {
            sqlException.printStackTrace();
        } catch (ClassNotFoundException classNotFoundException) {
            classNotFoundException.printStackTrace();
        }
    }

    public static void findBill(Integer commId) {
        String sql = "select bb.name,aa.account_period,cc.name commName,cc.id from f_bill aa ,f_cost_rule bb,r_community cc where aa.cost_rule_id = bb.id and aa.comm_id = cc.id and cc.id = ? and aa.is_payment='否' and aa.state='未支付' and bill_time > '2020-08-21' GROUP BY bb.name,aa.account_period,cc.name,cc.id";

        String userSql = "select DISTINCT aa.wx_openid from r_owner aa, r_owner_property bb,f_bill cc  where  aa.id = bb.owner_id and bb.comm_id= ?  and cc.is_payment='否' and cc.state='未支付' and aa.wx_openid is not null  and bill_time > ?";
        StringBuffer title = new StringBuffer();
        StringBuffer content = new StringBuffer();
        String comm = "";
        try {
            ResultSet resultSet = ConnectUtil.executeQuery(sql, new Object[]{commId});
            while (resultSet.next()) {
                String name = resultSet.getString("name");
                String account_period = resultSet.getString("account_period");
                comm = resultSet.getString("commName");
                title.append(name).append(",");
                content.append(account_period).append(title.toString()).append(",");

            }
            String s = title.toString();
            String s1 = content.toString();

            String tit = s;
            String cont = s1;
            tit = tit + "收费通知";
            cont = cont + "收费通知，请大家及时缴费。";
            Date date = new Date();
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(date);
            calendar.add(Calendar.DATE, -1);
            Date time = calendar.getTime();
            System.out.println(FormatUtil.dateToString(time, FormatUtil.FORMAT_LONG));
            ResultSet resultSet1 = ConnectUtil.executeQuery(userSql, new Object[]{commId, time});
            while (resultSet1.next()) {
                String wx_openid = resultSet1.getString("wx_openid");
                if (!StringUtils.isEmpty(wx_openid)) {
                    String uri = "http://qjwsg.free.idcfengye.com/weChat/billDetail?openid=" + wx_openid;
                    JSONObject jsonObject = SendMessage.sendMessage(wx_openid, WeChatResources.TEMPLATE_WYGL_ID, tit, cont, uri, "", comm + "社区物业提醒您及时查看服务信息！");
                }

            }
        } catch (SQLException sqlException) {
            sqlException.printStackTrace();
        } catch (ClassNotFoundException classNotFoundException) {
            classNotFoundException.printStackTrace();
        }

    }
}
