package com.estate.sdzy.wechat.controller;

import com.estate.common.util.ConnectUtil;
import com.estate.common.util.Result;
import com.estate.common.util.ResultUtil;
import com.estate.sdzy.wechat.entity.TextMessage;
import com.estate.sdzy.wechat.resource.WeChatResources;
import com.estate.sdzy.wechat.util.CheckUtil;
import com.estate.sdzy.wechat.util.MessageUtil;
import com.estate.sdzy.wechat.util.WeChatUtil;
import lombok.extern.slf4j.Slf4j;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import org.dom4j.DocumentException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
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
//        log.info("进入微信后台了！！");
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
//                log.info("关注消息推送");
                String event = map.get("Event");
                if (MessageUtil.MESSAGE_EVENT_SUBSCRIBE.equals(event)) {
//                    log.info("关注消息！！！");
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
        JSONObject jsonObject = JSONObject.fromObject(result);
        request.setAttribute("user", jsonObject);

        return "index_skip";
    }

    @GetMapping("/bindEstate")
    public String bindEstate(HttpServletRequest request){
        String openid = request.getParameter("openid");
        String sex = request.getParameter("sex");
        String city = request.getParameter("city");
        String nickname = request.getParameter("nickname");
        String country = request.getParameter("country");
        String province = request.getParameter("province");
        String headimgurl = request.getParameter("headimgurl");
        Map<String,String> user = new HashMap<>(16);
        user.put("openid",openid);
        user.put("sex",sex);
        user.put("city",city);
        user.put("city",city);
        user.put("nickname",nickname);
        user.put("country",country);
        user.put("province",province);
        user.put("headimgurl",headimgurl);
        String forObject = restTemplate.getForObject("http://estate-bill/sdzy/rProvince/get", String.class);
        JSONObject dist = JSONObject.fromObject(forObject);
        request.setAttribute("dist",dist);
        request.setAttribute("user",user);
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



//        log.info("用户信息：{}", jsonObject);
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
            int comp_id = 0,comm_id = 0;
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
                comp_id = result.getInt("comp_id");
                comm_id = result.getInt("comm_id");
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
            request.setAttribute("comp_id",comp_id);
            request.setAttribute("comm_id",comm_id);
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

    @GetMapping("/page/{page}")
    public String route(@PathVariable ("page") String page,HttpServletRequest request){
        String openid = request.getParameter("openid");
        request.setAttribute("openid",openid);
        System.out.println(openid+"=========");
        System.out.println(page+"=====================");
        return "model/"+page;
    }

    @GetMapping("/page/account")
    public String account(HttpServletRequest request){
        String openid = request.getParameter("openid");
        request.setAttribute("openid",openid);
        System.out.println(openid+"=========");
        String sql  ="select DISTINCT bb.name,bb.id from r_community bb ,r_owner cc,r_owner_property dd where dd.comm_id = bb.id and cc.id = dd.owner_id and cc.wx_openid= ? and bb.comp_id= ? ";

        List<Map<String,Object>> list = new ArrayList<>();
        try {
            ResultSet resultSet = ConnectUtil.executeQuery(sql, new Object[]{openid, WeChatResources.COMP_ID});
            while (resultSet.next()){
                Map<String,Object> map = new HashMap<>(16);
                String name = resultSet.getString("name");
                int id = resultSet.getInt("id");
                map.put("name",name);
                map.put("id",id);
                list.add(map);
            }
        } catch (SQLException sqlException) {
            sqlException.printStackTrace();
        } catch (ClassNotFoundException classNotFoundException) {
            classNotFoundException.printStackTrace();
        }
        String result = null;
        System.out.println(list);
        log.info("结果是：{}",list);
        JSONObject jsonObject = JSONObject.fromObject(result);
        request.setAttribute("openid", openid);
        request.setAttribute("commList", list);
        return "model/account";
    }


    @GetMapping("/getBill")
    @ResponseBody
    public Result getBill(HttpServletRequest request) throws SQLException, ClassNotFoundException {
        log.info("开始时间：{}",new Date());
        Integer pageNo = 0;
        Integer size = 10;

        String openid = request.getParameter("openid");
        String no = request.getParameter("pageNo");
        if(!StringUtils.isEmpty(no)){
            pageNo = (Integer.valueOf(no)-1)*size;
        }
        String state = request.getParameter("state");
        String costName = request.getParameter("costName");
        String isPayment = request.getParameter("isPayment");
        StringBuffer sql = new StringBuffer();
        sql.append("select ee.property_No,ee.property_type,dd.name,bb.bill_no,bb.bill_time,bb.is_overdue,bb.overdue_rule,bb.overdue_cost,bb.is_payment,bb.price+sale_price-pay_price+overdue_cost price,bb.pay_price,bb.sale_price,bb.account_period,bb.state,bb.begin_scale,bb.end_scale from r_owner aa,f_bill bb,r_owner_property cc,f_cost_rule dd,v_r_owner_property ee  where aa.id = cc.owner_id and bb.property_id = cc.property_id and bb.property_type = cc.property_type and bb.cost_rule_id = dd.id and bb.property_id = ee.property_id and bb.property_type = ee.property_type and aa.wx_openid= ? ");
        if(!StringUtils.isEmpty(state)){
            sql.append(" and bb.state = '").append(state).append("'");
        }
        if(!StringUtils.isEmpty(costName)){
            sql.append(" and dd.costName = '").append(costName).append("'");
        }
        if(!StringUtils.isEmpty(isPayment)){
            sql.append(" and bb.isPayment = '").append(isPayment).append("'");
        }

        sql.append(" limit ?,?");
        System.out.println(sql.toString());
        ResultSet resultSet = ConnectUtil.executeQuery(sql.toString(), new Object[]{openid,pageNo,size});
        List<Map<String,Object>> list = new ArrayList<>();
        while(resultSet.next()){
            Map<String,Object> map = new HashMap<>(16);
            String name = resultSet.getString("name");
            String billNo = resultSet.getString("bill_no");
            Date billTime = resultSet.getDate("bill_time");
            String is_overdue = resultSet.getString("is_overdue");
            String overdue_cost = resultSet.getString("overdue_cost");
            String account_period = resultSet.getString("account_period");
            String resultState = resultSet.getString("state");
            String begin_scale = resultSet.getString("begin_scale");
            String end_scale = resultSet.getString("end_scale");
            String is_payment = resultSet.getString("is_payment");
            String property_No = resultSet.getString("property_No");
            String property_type = resultSet.getString("property_type");
            BigDecimal price = resultSet.getBigDecimal("price");
            BigDecimal pay_price = resultSet.getBigDecimal("pay_price");
            BigDecimal sale_price = resultSet.getBigDecimal("sale_price");

            map.put("property_No",property_No);
            map.put("property_type",property_type);
            map.put("name",name);
            map.put("billNo",billNo);
            map.put("billTime",billTime);
            map.put("is_overdue",is_overdue);
            map.put("overdue_cost",overdue_cost);
            map.put("account_period",account_period);
            map.put("resultState",resultState);
            map.put("begin_scale",begin_scale);
            map.put("end_scale",end_scale);
            map.put("is_payment",is_payment);
            map.put("price",price);
            map.put("pay_price",pay_price);
            map.put("sale_price",sale_price);
            list.add(map);
        }
        log.info("结束时间:{}",new Date());
        return ResultUtil.success(list);
    }
}
