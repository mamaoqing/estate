package com.estate.sdzy.wechat.controller;

import com.estate.common.util.ConnectUtil;
import com.estate.common.util.Result;
import com.estate.common.util.ResultUtil;
import com.estate.sdzy.wechat.service.WeChatDataService;
import lombok.extern.slf4j.Slf4j;
import net.sf.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.math.BigDecimal;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;

/**
 * @author mq
 * @description: TODO
 * @title: WeChatDataController
 * @projectName estate-parent
 * @date 2020/10/2215:57
 */
@RestController
@RequestMapping("/weChatData")
@Slf4j
public class WeChatDataController {

    @Autowired
    private WeChatDataService weChatDataService;

    @GetMapping("/commList")
    public Result commList(HttpServletRequest request) {
        return ResultUtil.success(weChatDataService.commList(request.getParameter("openid")));
    }

    @GetMapping("/billDetail")
    public Result billDetail(HttpServletRequest request) {
        return ResultUtil.success(weChatDataService.billDetail(request.getParameter("openid")));
    }

    @GetMapping("/bindPark")
    public String bindPark(HttpServletRequest request) {
        Map<String, String> user = new HashMap<>(16);

        user.put("openid", request.getParameter("openid"));

//        return ResultUtil.success(weChatDataService.park(user));
        return  weChatDataService.park(user).toString();
    }
    @GetMapping("/bindEstate")
    public String bindEstate(HttpServletRequest request) {
        Map<String, String> user = new HashMap<>(16);

        user.put("openid", request.getParameter("openid"));

        return weChatDataService.estate(user).toString();
    }

    @GetMapping("/getBill")
    public Result getBill(HttpServletRequest request) throws SQLException, ClassNotFoundException {
        log.info("开始时间：{}", new Date());
        Integer pageNo = 0;
        Integer size = 10;

        String openid = request.getParameter("openid");
        String no = request.getParameter("pageNo");
        if (!StringUtils.isEmpty(no)) {
            pageNo = (Integer.valueOf(no) - 1) * size;
        }
        String state = request.getParameter("state");
        String costName = request.getParameter("costName");
        String isPayment = request.getParameter("isPayment");
        StringBuffer sql = new StringBuffer();
        sql.append("select ee.property_No,ee.property_type,dd.name,bb.bill_no,bb.bill_time,bb.is_overdue,bb.overdue_rule,bb.overdue_cost,bb.is_payment,bb.price+sale_price-pay_price+overdue_cost price,bb.pay_price,bb.sale_price,bb.account_period,bb.state,bb.begin_scale,bb.end_scale from r_owner aa,f_bill bb,r_owner_property cc,f_cost_rule dd,v_r_owner_property ee  where aa.id = cc.owner_id and bb.property_id = cc.property_id and bb.property_type = cc.property_type and bb.cost_rule_id = dd.id and bb.property_id = ee.property_id and bb.property_type = ee.property_type and aa.wx_openid= ? ");
        if (!StringUtils.isEmpty(state)) {
            sql.append(" and bb.state = '").append(state).append("'");
        }
        if (!StringUtils.isEmpty(costName)) {
            sql.append(" and dd.costName = '").append(costName).append("'");
        }
        if (!StringUtils.isEmpty(isPayment)) {
            sql.append(" and bb.isPayment = '").append(isPayment).append("'");
        }

        sql.append(" limit ?,?");
        System.out.println(sql.toString());
        ResultSet resultSet = ConnectUtil.executeQuery(sql.toString(), new Object[]{openid, pageNo, size});
        List<Map<String, Object>> list = new ArrayList<>();
        while (resultSet.next()) {
            Map<String, Object> map = new HashMap<>(16);
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

            map.put("property_No", property_No);
            map.put("property_type", property_type);
            map.put("name", name);
            map.put("billNo", billNo);
            map.put("billTime", billTime);
            map.put("is_overdue", is_overdue);
            map.put("overdue_cost", overdue_cost);
            map.put("account_period", account_period);
            map.put("resultState", resultState);
            map.put("begin_scale", begin_scale);
            map.put("end_scale", end_scale);
            map.put("is_payment", is_payment);
            map.put("price", price);
            map.put("pay_price", pay_price);
            map.put("sale_price", sale_price);
            list.add(map);
        }
        log.info("结束时间:{}", new Date());
        return ResultUtil.success(list);
    }

}
