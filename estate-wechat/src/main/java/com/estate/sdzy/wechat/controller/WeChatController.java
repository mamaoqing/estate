package com.estate.sdzy.wechat.controller;

import com.estate.common.util.ConnectUtil;
import com.estate.sdzy.wechat.entity.TextMessage;
import com.estate.sdzy.wechat.util.CheckUtil;
import com.estate.sdzy.wechat.util.MessageUtil;
import com.estate.sdzy.wechat.util.ResultSetToMap;
import com.estate.sdzy.wechat.util.WeChatUtil;
import lombok.extern.slf4j.Slf4j;
import net.sf.json.JSONObject;
import org.dom4j.DocumentException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;

/**
 * @author mq
 * @description: TODO
 * @title: WechatController
 * @projectName estate-parent
 * @date 2020/9/710:50
 */
@Controller
@Slf4j
@RequestMapping("/weChat/")
public class WeChatController {

    @Autowired
    private RestTemplate restTemplate;

    @GetMapping("/hello")
    public void cutin(HttpServletResponse response, String signature, String timestamp, String nonce, String echostr) {
        try {
            PrintWriter out = response.getWriter();
            if (CheckUtil.checkWeChat(signature, timestamp, nonce)) {
                out.println(echostr);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @PostMapping("/hello")
    public void sedMessage(HttpServletRequest request, HttpServletResponse response) {
        log.info("进入微信后台了！！");
        PrintWriter out = null;
        try {
            request.setCharacterEncoding("UTF-8");
            response.setCharacterEncoding("UTF-8");
            out = response.getWriter();
            Map<String, String> map = MessageUtil.xmlToMap(request);
            String toUserName = map.get("ToUserName");
            String fromUserName = map.get("FromUserName");
            String createTime = map.get("CreateTime");
            String content = map.get("Content");
            String msgType = map.get("MsgType");
            String msgId = map.get("MsgId");
            String m = null;
            // 文本消息
            if (MessageUtil.MESSAGE_TEXT.equals(msgType)) {
                TextMessage message = new TextMessage();
                message.setFromUserName(toUserName);
                message.setToUserName(fromUserName);
                message.setMsgType("text");
                message.setCreateTime(new Date().getTime());
                message.setContent("消息是：" + content);
                System.out.println(message.getContent());
                m = MessageUtil.initNews(toUserName, fromUserName);
                // 用户关注时推送消息
            } else if (MessageUtil.MESSAGE_EVENT.equals(msgType)) {
                log.info("关注消息推送");
                String event = map.get("Event");
                if (MessageUtil.MESSAGE_EVENT_SUBSCRIBE.equals(event)) {
                    log.info("关注消息！！！");
                    m = MessageUtil.initNews(toUserName, fromUserName);
                    // 取消关注
                } else {

                }
            }
            out.print(m);
        } catch (IOException e) {
            e.printStackTrace();
        } catch (DocumentException e) {
            e.printStackTrace();
        } finally {
            out.close();
        }

    }

    @RequestMapping("/getUser")
    public String getUser(HttpServletRequest request) {
        String code = request.getParameter("code");
        JSONObject htmlAccessToken = WeChatUtil.getHTMLAccessToken(code);
        Object access_token = htmlAccessToken.get("access_token");
        Object openid = htmlAccessToken.get("openid");

        JSONObject userInfo = WeChatUtil.getUserInfo(access_token, openid);
        String result = null;
        try {
            result = new String(userInfo.toString().getBytes("ISO-8859-1"), "UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        String forObject = restTemplate.getForObject("http://estate-bill/sdzy/rProvince/get", String.class);
        JSONObject dist = JSONObject.fromObject(forObject);
        request.setAttribute("dist",dist);
        String sql = "select * from s_company where is_delete = 0";
        List<Map<String, Object>> stringObjectMap = new ArrayList<>();
        try {
            ResultSet resultSet = ConnectUtil.executeQuery(sql);
//            stringObjectMap = ResultSetToMap.resultSetToMap(resultSet);
            while (resultSet.next()) {
                Map<String, Object> map = new HashMap<>(16);
                map.put("id", resultSet.getInt("id"));
                map.put("name", resultSet.getString("name"));
                stringObjectMap.add(map);
            }
        } catch (SQLException sqlException) {
            sqlException.printStackTrace();
        } catch (ClassNotFoundException classNotFoundException) {
            classNotFoundException.printStackTrace();
        }


        JSONObject jsonObject = JSONObject.fromObject(result);
        request.setAttribute("user", jsonObject);
        log.info("用户信息：{}", jsonObject);
        request.setAttribute("list", stringObjectMap);

        return "index";
    }


    @GetMapping("/billDetail")
    public String billDetail(HttpServletRequest request) {
        String openid = request.getParameter("openid");
        request.setAttribute("openid", openid);
        String sql = "select aa.account_period,aa.id,aa.property_type,(aa.price + aa.overdue_cost + aa.sale_price-aa.pay_price) price,dd.name costName,aa.is_payment,aa.state, case aa.property_type when '房产' then (select roomNo from v_room_roomNo where id = aa.property_id) else '' end  room,aa.comp_id ,aa.comm_id from f_bill aa,r_owner bb ,r_owner_property cc,f_cost_rule dd where  aa.property_type = cc.property_type and aa.property_id = cc.property_id and cc.owner_id = bb.id and aa.cost_rule_id = dd.id and bb.wx_openid='" + openid + "' and aa.is_payment ='否' and aa.state='未支付'";

        String sumSql = "select sum(aa.price + aa.overdue_cost + aa.sale_price-aa.pay_price) price from f_bill aa,r_owner bb ,r_owner_property cc,f_cost_rule dd where  aa.property_type = cc.property_type and aa.property_id = cc.property_id and cc.owner_id = bb.id and aa.cost_rule_id = dd.id and bb.wx_openid='" + openid + "' and aa.is_payment ='否' and aa.state='未支付'";
        List<Map<String, Object>> list = new ArrayList<>();
        try {
            ResultSet result = ConnectUtil.executeQuery(sql);
            StringBuffer listBillId = new StringBuffer();
            while (result.next()) {
                Map<String, Object> map = new HashMap<>(16);

                String bill_no = result.getString("id");
                String costName = result.getString("costName");
                String is_payment = result.getString("is_payment");
                String state = result.getString("state");
                String account_period = result.getString("account_period");
                String property_type = result.getString("property_type");
                BigDecimal price = result.getBigDecimal("price");
                String room = result.getString("room");
                int comp_id = result.getInt("comp_id");
                int comm_id = result.getInt("comm_id");
                listBillId.append(bill_no).append(",");
                map.put("bill_no", bill_no);
                map.put("costName", costName);
                map.put("is_payment", is_payment);
                map.put("state", state);
                map.put("account_period", account_period);
                map.put("property_type", property_type);
                map.put("price", price);
                map.put("room", room);
                list.add(map);
            }
            request.setAttribute("list",list);
            request.setAttribute("listBillId",listBillId.toString());
            ResultSet sum = ConnectUtil.executeQuery(sumSql);
            while(sum.next()){
                BigDecimal price = sum.getBigDecimal("price");
                request.setAttribute("sumPrice",price);
            }
        } catch (SQLException sqlException) {
            sqlException.printStackTrace();
        } catch (ClassNotFoundException classNotFoundException) {
            classNotFoundException.printStackTrace();
        }

        return "billdetail";
    }

}
