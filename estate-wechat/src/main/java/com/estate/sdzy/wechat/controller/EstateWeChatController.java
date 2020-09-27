package com.estate.sdzy.wechat.controller;

import com.estate.common.util.ConnectUtil;
import com.estate.common.util.Result;
import com.estate.common.util.ResultUtil;
import com.estate.common.util.TransactionConnUtil;
import com.estate.sdzy.wechat.resource.WeChatResources;
import com.estate.sdzy.wechat.util.ResultSetToMap;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import javax.servlet.http.HttpServletRequest;
import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;

import static jdk.nashorn.internal.runtime.regexp.joni.Config.log;

/**
 * @author mq
 * @description: TODO
 * @title: EstateWeChatController
 * @projectName estate-parent
 * @date 2020/9/2111:05
 */
@RestController
@RequestMapping("/estate/")
@Slf4j
public class EstateWeChatController {

    @Autowired
    private RestTemplate restTemplate;


    @PostMapping("setWeChatUser")
    public boolean setWeChatUser(HttpServletRequest request) {
        String compId = request.getParameter("compId");
        String commId = request.getParameter("commId");
        String areaId = request.getParameter("areaId");
        String buildId = request.getParameter("buildId");
        String roomId = request.getParameter("roomId");
        String openid = request.getParameter("openid");
        String nickname = request.getParameter("nickname");
        String sex = request.getParameter("sex");
        String province = request.getParameter("province");
        String city = request.getParameter("city");
        String country = request.getParameter("country");
        String sql = "select id from  r_owner where wx_openid = '" + openid + "' and comp_id = "+ WeChatResources.COMP_ID;
        String ownerProper = "select id from  r_owner_property where property_id = "+roomId +" and property_type = '房产' and owner_id= ?";


        Date date = new Date();
        String insertSql = "insert into r_owner (wx_openid,wx_sex,wx_province,wx_city,wx_country,created_at,modified_at,comp_id) values(?,?,?,?,?,?,?,?)";
        String ownerProperty = "insert into r_owner_property (owner_id,comp_id,comm_id,comm_area_id,property_type,property_id,building_id,type,created_at,modified_at) values(?,?,?,?,?,?,?,?,?,?)";
        Connection connection = null;
        Integer integer =0;
        try {
            ResultSet resultSet = ConnectUtil.executeQuery(sql);
            if (resultSet.next()){
                // 添加用户信息
                integer = resultSet.getInt("id");
            }else {
                integer = TransactionConnUtil.executeUpdate(insertSql, new Object[]{openid, sex, province, city, country, date, date,compId}, true);
            }


            connection = TransactionConnUtil.getConnection();
            ResultSet resultSet1 = TransactionConnUtil.executeQuery(ownerProper,new Object[]{integer});
            if(!resultSet1.next()){
            Object[] objects = {integer, compId, commId, areaId, "房产", roomId, buildId, "业主", date, date};
                TransactionConnUtil.executeUpdate(ownerProperty, objects);
            }
            connection.commit();
        } catch (ClassNotFoundException classNotFoundException) {
            try {
                connection.rollback();
            } catch (SQLException sqlException) {
                sqlException.printStackTrace();
            }
            classNotFoundException.printStackTrace();
            return false;
        } catch (SQLException sqlException) {
            try {
                connection.rollback();
            } catch (SQLException throwables) {
                throwables.printStackTrace();
            }
            sqlException.printStackTrace();
            return false;
        }

        return true;
    }

    @GetMapping("getCity")
    public Result getCity(HttpServletRequest request){
        String provinceId = request.getParameter("provinceId");
        System.out.println(provinceId+"<--->");
        String forObject = restTemplate.getForObject("http://estate-bill/sdzy/rProvince/getCityList?provinceId="+provinceId, String.class);
        System.out.println(forObject);
        return ResultUtil.success(forObject);
    }

    @GetMapping("getDist")
    public Result getDist(HttpServletRequest request){
        String provinceId = request.getParameter("provinceId");
        String forObject = restTemplate.getForObject("http://estate-bill/sdzy/rProvince/getDistList?cityId="+provinceId, String.class);
        System.out.println(forObject);
        return ResultUtil.success(forObject);
    }

    @GetMapping("getCompList")
    public Result getCompList() {
        String sql = "select * from s_company where is_delete = 0";
        List<Map<String, Object>> stringObjectMap = new ArrayList<>();
        try {
            ResultSet resultSet = ConnectUtil.executeQuery(sql);

            stringObjectMap = ResultSetToMap.resultSetToMap(resultSet);

        } catch (SQLException sqlException) {
            sqlException.printStackTrace();
        } catch (ClassNotFoundException classNotFoundException) {
            classNotFoundException.printStackTrace();
        }
        return ResultUtil.success(stringObjectMap);
    }

    @GetMapping("getCommList")
    public Result getCommList(HttpServletRequest request) {
        String id = request.getParameter("id");
        List<Map<String, Object>> stringObjectMap = new ArrayList<>();
        String sql = "select id ,name from r_community where district_id = " + id + " and is_delete=0 ";
        try {
            ResultSet resultSet = ConnectUtil.executeQuery(sql);
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
        return ResultUtil.success(stringObjectMap);
    }

    @GetMapping("getCommAreaList")
    public Result getCommAreaList(HttpServletRequest request) {
        String id = request.getParameter("id");
        List<Map<String, Object>> stringObjectMap = new ArrayList<>();
        //select * from r_comm_area where comm_id =1 and is_delete=0
        String sql = "select id ,name from r_comm_area where comm_id = " + id + " and is_delete=0 ";
        try {
            ResultSet resultSet = ConnectUtil.executeQuery(sql);
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
        return ResultUtil.success(stringObjectMap);
    }

    @GetMapping("getBuildList")
    public Result getBuildList(HttpServletRequest request) {
        String id = request.getParameter("id");
        List<Map<String, Object>> stringObjectMap = new ArrayList<>();
        //select * from r_comm_area where comm_id =1 and is_delete=0
        String sql = "select id ,name from r_building where comm_area_id = " + id + " and is_delete=0 ";
        try {
            ResultSet resultSet = ConnectUtil.executeQuery(sql);
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
        return ResultUtil.success(stringObjectMap);
    }

    @GetMapping("getUnitList")
    public Result getUnitList(HttpServletRequest request) {
        String id = request.getParameter("id");
        List<Map<String, Object>> stringObjectMap = new ArrayList<>();
        //select * from r_comm_area where comm_id =1 and is_delete=0
        String sql = "select id ,name from r_unit where building_id = " + id + " and is_delete=0 ";
        try {
            ResultSet resultSet = ConnectUtil.executeQuery(sql);
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
        return ResultUtil.success(stringObjectMap);
    }

    @GetMapping("getRoomList")
    public Result getRoomList(HttpServletRequest request) {
        String id = request.getParameter("id");
        List<Map<String, Object>> stringObjectMap = new ArrayList<>();
        //select * from r_comm_area where comm_id =1 and is_delete=0
        String sql = "select id ,name from r_room where unit_id = " + id + " and is_delete=0 ";
        try {
            ResultSet resultSet = ConnectUtil.executeQuery(sql);
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
        return ResultUtil.success(stringObjectMap);
    }

    @GetMapping("getCostRuleList")
    public Result getCostRuleList(HttpServletRequest request) throws SQLException, ClassNotFoundException {
        String commId = request.getParameter("commId");
        String openid = request.getParameter("openid");
        String sql = "select DISTINCT aa.name,cc.property_type,dd.roomNo,cc.property_id,cc.cost_rule_id,aa.comm_id,aa.comp_id from f_cost_rule aa,r_owner_property bb,f_cost_rule_room cc,v_room_roomNo dd,r_owner ee where aa.id =cc.cost_rule_id and cc.property_type=bb.property_type and cc.property_id = bb.property_id and aa.comm_id=? and bb.owner_id=ee.id and dd.id = cc.property_id and ee.wx_openid=?";
        List<Map<String, Object>> list = new ArrayList<>();
        ResultSet resultSet = ConnectUtil.executeQuery(sql, new Object[]{commId, openid});
        while (resultSet.next()){
            Map<String, Object> map = new HashMap<>(16);
            map.put("property_type", resultSet.getString("property_type"));
            map.put("roomNo", resultSet.getString("roomNo"));
            map.put("name", resultSet.getString("name"));
            map.put("params", resultSet.getString("name")+";"+resultSet.getString("comp_id")+";"+resultSet.getString("comm_id")+";"+resultSet.getString("cost_rule_id")+";"+resultSet.getString("property_type")+";"+resultSet.getString("property_id"));
            list.add(map);
        }

        String sqlf_account = "select aa.* from f_account aa, r_owner bb where aa.owner_id = bb.id and bb.wx_openid = ? and aa.comm_id = ?";
        ResultSet resultSet1 = ConnectUtil.executeQuery(sqlf_account, new Object[]{openid, commId});
        List<Map<String, Object>> nameList = new ArrayList<>();
        while (resultSet1.next()){
            Map<String, Object> map = new HashMap<>(16);
            String name = resultSet1.getString("name");
            int id = resultSet1.getInt("id");
            BigDecimal fee = resultSet1.getBigDecimal("fee");
            map.put("name",name+"\t 账户余额："+fee);
            map.put("id",id);
            nameList.add(map);
        }
        Map<String,Object> result = new HashMap<>(16);
        result.put("nameList",nameList);
        result.put("list",list);
        log.info("结果：{}",result);
        return ResultUtil.success(result);
    }



}
