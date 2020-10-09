package com.estate.sdzy.wechat.controller;

import com.estate.common.util.ConnectUtil;
import com.estate.common.util.TransactionConnUtil;
import com.estate.sdzy.wechat.resource.WeChatResources;
import com.estate.sdzy.wechat.service.PayService;
import com.estate.sdzy.wechat.util.WeChatUtil;
import com.lly835.bestpay.model.PayResponse;
import lombok.extern.slf4j.Slf4j;
import net.sf.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;

/**
 * @author mq
 * @description: TODO
 * @title: PayController
 * @projectName estate-parent
 * @date 2020/9/1816:12
 */
@Controller
@RequestMapping("/pay/")
@Slf4j
public class PayController {

    @Autowired
    private PayService payService;


    @GetMapping("create")
    public ModelAndView create(HttpServletRequest request) {

        ModelAndView mv = new ModelAndView("pay");

        PayResponse payResponse = payService.create(request, "微信支付","支付","",null,null);
        request.setAttribute("res", payResponse);
        return mv;
    }

    @ResponseBody
    @PostMapping("notify")
    public String payResult(@RequestBody String notifyData) {
        String result = payService.notify(notifyData);

        return result;
    }

    @GetMapping("doprestore")
    public ModelAndView doprestore(HttpServletRequest request) {
        ModelAndView mv = new ModelAndView("pay");
        String openid = request.getParameter("openid");
        String costName = request.getParameter("costName");
        String accountName = request.getParameter("accountName");
        String commId = request.getParameter("commId");
//        String commId = request.getParameter("commId");
        Integer count = null;
        if(!StringUtils.isEmpty(accountName)){
            count = Integer.valueOf(accountName);
        }
        PayResponse payResponse = payService.create(request, "账户预存", "预存",costName, Integer.valueOf(commId),count);
        request.setAttribute("res", payResponse);
        request.setAttribute("openid", openid);
        return mv;
    }

    @RequestMapping("/prestore")
    public String prestore(HttpServletRequest request) {
//        String code = request.getParameter("code");
//        JSONObject htmlAccessToken = WeChatUtil.getHTMLAccessToken(code);
//        Object access_token = htmlAccessToken.get("access_token");
//        Object openid = htmlAccessToken.get("openid");
        String openid = request.getParameter("openid");
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
//        JSONObject userInfo = WeChatUtil.getUserInfo(access_token, openid);
        String result = null;
//        try {
//            result = new String(userInfo.toString().getBytes("ISO-8859-1"), "UTF-8");
//        } catch (UnsupportedEncodingException e) {
//            e.printStackTrace();
//        }
        System.out.println(list);
        log.info("结果是：{}",list);
        JSONObject jsonObject = JSONObject.fromObject(result);
        request.setAttribute("openid", openid);
        request.setAttribute("commList", list);
        return "account";
    }
}
