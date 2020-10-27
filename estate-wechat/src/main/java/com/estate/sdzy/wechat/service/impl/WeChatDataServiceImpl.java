package com.estate.sdzy.wechat.service.impl;

import com.estate.common.util.ConnectUtil;
import com.estate.common.util.Result;
import com.estate.sdzy.wechat.resource.WeChatResources;
import com.estate.sdzy.wechat.service.WeChatDataService;
import lombok.extern.slf4j.Slf4j;
import net.sf.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.math.BigDecimal;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.SQLNonTransientConnectionException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author mq
 * @description: TODO
 * @title: WeChatDataServiceImpl
 * @projectName estate-parent
 * @date 2020/10/2216:07
 */
@Service
@Slf4j
public class WeChatDataServiceImpl implements WeChatDataService {

    @Autowired
    private RestTemplate restTemplate;

    @Override
    public JSONObject commList(String openid) {
        JSONObject results = new JSONObject();

        String sql = "select DISTINCT bb.name,bb.id from r_community bb ,r_owner cc,r_owner_property dd where dd.comm_id = bb.id and cc.id = dd.owner_id and cc.wx_openid= ? and bb.comp_id= ? ";

        List<Map<String, Object>> list = new ArrayList<>();
        System.out.println(sql);
        System.out.println("--------------"+openid);
        try {
            ResultSet resultSet = ConnectUtil.executeQuery(sql, new Object[]{openid, WeChatResources.COMP_ID});
            while (resultSet.next()) {
                System.out.println(123);
                Map<String, Object> map = new HashMap<>(16);
                String name = resultSet.getString("name");
                int id = resultSet.getInt("id");
                map.put("name", name);
                map.put("id", id);
                list.add(map);
            }
            log.info("list:{}",list);
            results.put("commList",list);
        } catch (SQLNonTransientConnectionException sqlException) {
            sqlException.printStackTrace();
        }catch (SQLException sqlException) {
            sqlException.printStackTrace();
        } catch (ClassNotFoundException classNotFoundException) {
            classNotFoundException.printStackTrace();
        }

        return results;
    }

    @Override
    public JSONObject billDetail(String openid) {
        JSONObject results = new JSONObject();
        String sql = "select aa.account_period,aa.id,aa.property_type,(aa.price + aa.overdue_cost + aa.sale_price-aa.pay_price) price,dd.name costName,aa.is_payment,aa.state, case aa.property_type when '房产' then (select roomNo from v_room_roomNo where id = aa.property_id) else '' end  room,aa.comp_id ,aa.comm_id from f_bill aa,r_owner bb ,r_owner_property cc,f_cost_rule dd where  aa.property_type = cc.property_type and aa.property_id = cc.property_id and cc.owner_id = bb.id and aa.cost_rule_id = dd.id and bb.wx_openid='" + openid + "' and aa.is_payment ='否' and aa.state='未支付'";

        String sumSql = "select sum(aa.price + aa.overdue_cost + aa.sale_price-aa.pay_price) price from f_bill aa,r_owner bb ,r_owner_property cc,f_cost_rule dd where  aa.property_type = cc.property_type and aa.property_id = cc.property_id and cc.owner_id = bb.id and aa.cost_rule_id = dd.id and bb.wx_openid='" + openid + "' and aa.is_payment ='否' and aa.state='未支付'";
        List<Map<String, Object>> list = new ArrayList<>();
        try {
            ResultSet result = ConnectUtil.executeQuery(sql);
            StringBuffer listBillId = new StringBuffer();
            int comp_id = 0, comm_id = 0;
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
            results.put("comp_id", comp_id);
            results.put("comm_id", comm_id);
            results.put("list", list);
            results.put("listBillId", listBillId.toString());
            ResultSet sum = ConnectUtil.executeQuery(sumSql);
            while (sum.next()) {
                BigDecimal price = sum.getBigDecimal("price");
                results.put("sumPrice", price);
            }
        } catch (SQLException sqlException) {
            sqlException.printStackTrace();
        } catch (ClassNotFoundException classNotFoundException) {
            classNotFoundException.printStackTrace();
        }
        return results;
    }

    @Override
    public JSONObject park(Map<String,String> map) {
        String openid = map.get("openid");

        JSONObject results = new JSONObject();
        Map<String, String> user = new HashMap<>(16);

        results.put("openid",openid);
        Result dist = restTemplate.getForObject("http://estate-bill/sdzy/rProvince/get", Result.class);
        System.out.println(dist);
//        JSONObject dist = JSONObject.fromObject(forObject);
        System.out.println("=========================");
        results.put("dist", dist.getData());
        String sql = "select * from s_company where is_delete = 0";
        String propertyNo = "select aa.id aid,bb.property_type,bb.id,case bb.property_type when '房产' then (select CONCAT(mm.name,bu.name,'-',uu.name,'-',room_no)  from r_room room,r_unit uu,r_building bu,r_comm_area ca,r_community mm where room.unit_id = uu.id and uu.building_id = bu.id and bu.comm_area_id = ca.id and ca.comm_id = mm.id  and  room.id = bb.property_id) else (select CONCAT(mm.name,'-',ps.no,'') from r_parking_space ps,r_community mm where  ps.comm_id = mm.id and ps.id=bb.property_id) end property_No from r_owner aa,r_owner_property bb where aa.id = bb.owner_id and  aa.wx_openid=? and bb.property_type='停车位'";
        List<Map<String, Object>> stringObjectMap = new ArrayList<>();
        List<Map<String, Object>> noMap = new ArrayList<>();
        try {
            ResultSet resultSet = ConnectUtil.executeQuery(sql);
//            stringObjectMap = ResultSetToMap.resultSetToMap(resultSet);
            ResultSet no = ConnectUtil.executeQuery(propertyNo, new Object[]{openid});
            while (no.next()) {
                Map<String, Object> maps = new HashMap<>(16);
                String property_type = no.getString("property_type");
                String property_No = no.getString("property_No");
                String id = no.getString("id");
                maps.put("property_type", property_type);
                maps.put("property_No", property_No);
                maps.put("id", id);
                noMap.add(maps);
            }
            while (resultSet.next()) {
                Map<String, Object> maps = new HashMap<>(16);
                maps.put("id", resultSet.getInt("id"));
                maps.put("name", resultSet.getString("name"));
                stringObjectMap.add(maps);
            }
        } catch (SQLException sqlException) {
            sqlException.printStackTrace();
        } catch (ClassNotFoundException classNotFoundException) {
            classNotFoundException.printStackTrace();
        }

        results.put("list", stringObjectMap);
        results.put("map", noMap);
        if(!noMap.isEmpty()){
            results.put("map", noMap);
        }
        log.info("最后的结果是：{}",results);
        return results;
    }

    @Override
    public JSONObject estate(Map<String, String> map) {
        String openid = map.get("openid");

        JSONObject results = new JSONObject();
        Map<String, String> user = new HashMap<>(16);

        user.put("openid", openid);
        Result dist = restTemplate.getForObject("http://estate-bill/sdzy/rProvince/get", Result.class);
        results.put("dist", dist.getData());
        results.put("user", user);
        String sql = "select * from s_company where is_delete = 0";
        String propertyNo = "select aa.id aid,bb.property_type,bb.id,case bb.property_type when '房产' then (select CONCAT(mm.name,bu.name,uu.name,'-',room_no)  from r_room room,r_unit uu,r_building bu,r_comm_area ca,r_community mm where room.unit_id = uu.id and uu.building_id = bu.id and bu.comm_area_id = ca.id and ca.comm_id = mm.id and  room.id = bb.property_id) else (select CONCAT(mm.name,ca.name,ps.no,'') from r_parking_space ps,r_comm_area ca,r_community mm where ps.comm_area_id = ca.id and ps.comm_id = mm.id and ps.id=bb.property_id) end property_No from r_owner aa,r_owner_property bb where aa.id = bb.owner_id and  aa.wx_openid=? and bb.property_type='房产'";
        List<Map<String, Object>> stringObjectMap = new ArrayList<>();
        List<Map<String, Object>> noMap = new ArrayList<>();
        try {
            ResultSet resultSet = ConnectUtil.executeQuery(sql);
//            stringObjectMap = ResultSetToMap.resultSetToMap(resultSet);
            ResultSet no = ConnectUtil.executeQuery(propertyNo, new Object[]{openid});
            while (no.next()) {
                Map<String, Object> maps = new HashMap<>(16);
                String property_type = no.getString("property_type");
                String property_No = no.getString("property_No");
                String id = no.getString("id");
                maps.put("property_type", property_type);
                maps.put("property_No", property_No);
                maps.put("id", id);
                noMap.add(maps);
            }
            while (resultSet.next()) {
                Map<String, Object> maps = new HashMap<>(16);
                maps.put("id", resultSet.getInt("id"));
                maps.put("name", resultSet.getString("name"));
                stringObjectMap.add(maps);
            }
        } catch (SQLException sqlException) {
            sqlException.printStackTrace();
        } catch (ClassNotFoundException classNotFoundException) {
            classNotFoundException.printStackTrace();
        }


//        log.info("用户信息：{}", jsonObject);
        results.put("list", stringObjectMap);
        results.put("map", noMap);
        return results;
    }
}
