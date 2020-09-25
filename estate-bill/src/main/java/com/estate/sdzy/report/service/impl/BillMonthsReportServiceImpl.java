package com.estate.sdzy.report.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.estate.common.entity.SUser;
import com.estate.common.exception.BillException;
import com.estate.common.util.BillExceptionEnum;
import com.estate.sdzy.asstes.entity.RCommunity;
import com.estate.sdzy.asstes.mapper.RCommunityMapper;
import com.estate.sdzy.report.entity.BillMonthsReport;
import com.estate.sdzy.report.mapper.BillMonthsReportMapper;
import com.estate.sdzy.report.mapper.FinanceMonthsReportMapper;
import com.estate.sdzy.report.service.BillMonthsReportService;
import com.estate.sdzy.system.service.SUserCommService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.*;

@Service
@Slf4j
public class BillMonthsReportServiceImpl extends ServiceImpl<BillMonthsReportMapper, BillMonthsReport> implements BillMonthsReportService {

    @Autowired
    private BillMonthsReportMapper billMonthsReportMapper;

    @Autowired
    private FinanceMonthsReportMapper financeMonthsReportMapper;

    @Autowired
    private RedisTemplate<String, Object> redisTemplate;

    @Autowired
    private RCommunityMapper rCommunityMapper;

    @Autowired
    private SUserCommService sUserCommService;

    @Override
    public List<Map<String,Object>> listBillMonthsReport(Map<String, String> map, String token) {
        List<Map<String,Object>> fList = new ArrayList<>();
        SUser user = getUserByToken(token);
        List<RCommunity> rCommunities = null;
        if(user.getCompId()==0){//开发公司获取全部的社区.
            QueryWrapper<RCommunity> queryWrapper= new QueryWrapper<>();
            if(!StringUtils.isEmpty(map.get("compId"))){
                queryWrapper.eq("comp_id",map.get("compId"));
            }
            if(!StringUtils.isEmpty(map.get("commId"))){
                queryWrapper.eq("id",map.get("commId"));
            }
            rCommunities = rCommunityMapper.selectList(queryWrapper);
        }else{//获取有数据权限的社区列表
            if(!StringUtils.isEmpty(map.get("commId"))){
                QueryWrapper<RCommunity> queryWrapper= new QueryWrapper<>();
                if(!StringUtils.isEmpty(map.get("commId"))){
                    queryWrapper.eq("id",map.get("commId"));
                }
                rCommunities = rCommunityMapper.selectList(queryWrapper);
            }else{
                rCommunities = sUserCommService.getUserComm(user.getId(),user.getCompId());
            }
        }
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM");
        List<Date> date = null;
        String formatFirstDay = null;
        if(("true").equals(map.get("more"))) {
            date = getDate();
        }else{
            Date firstDay = getFirstDay(null);
            formatFirstDay = sdf.format(firstDay);
        }
        if(!StringUtils.isEmpty(map.get("ruleId"))){
            Map<String,Object> resultMapTotal = new LinkedHashMap<>();
            resultMapTotal.put("费用标准","合计");
            for (RCommunity comm : rCommunities) {
                List<Map<String,Object>> rules = new ArrayList<>();
                Map<String,Object> mapAdd = new HashMap<>();
                mapAdd.put("id",Long.valueOf(map.get("ruleId")));
                mapAdd.put("name",map.get("ruleName"));
                rules.add(mapAdd);
                if(rCommunities.size()>0){
                    Map<String,List<Map<String,Object>>> billRuleAll = new LinkedHashMap<>();
                    if(("true").equals(map.get("more"))){
                        for (Date d: date) {
                            String format = sdf.format(d);
                            List<Map<String, Object>> billRule = billMonthsReportMapper.getBillMonthsReportGbRule(getFirstDay(d), getLastDay(d), comm.getId(),map.get("ruleId"));
                            billRuleAll.put(format,billRule);
                        }
                        setCommRuleTotal(map.get("ruleName"),comm.getName(),rules,billRuleAll,fList);
                    }else{
                        Date firstDay = getFirstDay(null);
                        Date lastDay = getLastDay(null);
                        List<Map<String, Object>> billRule = billMonthsReportMapper.getBillMonthsReportGbRule(firstDay, lastDay, comm.getId(),map.get("ruleId"));
                        billRuleAll.put(formatFirstDay,billRule);
                        setCommRuleTotal(map.get("ruleName"),comm.getName(),rules,billRuleAll,fList);
                    }
                }
            }
            if(rCommunities.size()>1){
                if(("true").equals(map.get("more"))) {//查询12个月的数据
                    for (Date d : date) {
                        String format = sdf.format(d);
                        Map<String, Object> mapTotal = billMonthsReportMapper.selectBillMonthsReport(getFirstDay(d), getLastDay(d), null,map.get("ruleId"));
                        setCommTotalAll(mapTotal, format, resultMapTotal);//保存一行社区总数
                    }
                }else{
                    Date firstDay = getFirstDay(null);
                    Date lastDay = getLastDay(null);
                    Map<String, Object> mapTotal = billMonthsReportMapper.selectBillMonthsReport(firstDay, lastDay, null,map.get("ruleId"));
                    setCommTotalAll(mapTotal, formatFirstDay, resultMapTotal);//保存一行社区总数
                }
                fList.add(resultMapTotal);
            }
            return fList;
        }else{
            Map<String,Object> resultMapTotal = new LinkedHashMap<>();
            resultMapTotal.put("费用标准","合计");
            for (RCommunity comm : rCommunities) {
                List<Map<String,Object>> rules = financeMonthsReportMapper.selectRule(comm.getId());
                Map<String,Object> resultMap = new LinkedHashMap<>();
                List<Map<String,Object>> resultMapList = null;
                resultMap.put("社区",comm.getName());
                resultMap.put("费用标准","合计");
                Map<String,List<Map<String,Object>>> billRuleAll = new HashMap<>();
                if(("true").equals(map.get("more"))){//查询12个月的数据
                    for (Date d: date) {
                        String format = sdf.format(d);
                        Map<String, Object> mapTotal = billMonthsReportMapper.selectBillMonthsReport(getFirstDay(d), getLastDay(d), comm.getId(),null);
                        setCommTotal(mapTotal,format,resultMap);//保存一行社区总数
                        List<Map<String, Object>> billRule = billMonthsReportMapper.getBillMonthsReportGbRule(getFirstDay(d), getLastDay(d), comm.getId(),null);
                        billRuleAll.put(format,billRule);
                    }
                    fList.add(resultMap);
                    setCommRuleTotal(null,null,rules,billRuleAll,fList);
                }else{
                    Date firstDay = getFirstDay(null);
                    Date lastDay = getLastDay(null);
                    Map<String, Object> mapTotal = billMonthsReportMapper.selectBillMonthsReport(firstDay, lastDay, comm.getId(),null);
                    setCommTotal(mapTotal,formatFirstDay,resultMap);
                    fList.add(resultMap);
                    List<Map<String, Object>> billRule = billMonthsReportMapper.getBillMonthsReportGbRule(firstDay, lastDay, comm.getId(),null);
                    billRuleAll.put(formatFirstDay,billRule);
                    setCommRuleTotal(null,null,rules,billRuleAll,fList);
                }

            }
            if(rCommunities.size()>1){//sql获取总数
                if(("true").equals(map.get("more"))) {//查询12个月的数据
                    for (Date d : date) {
                        String format = sdf.format(d);
                        Map<String, Object> mapTotal = billMonthsReportMapper.selectBillMonthsReport(getFirstDay(d), getLastDay(d), null,null);
                        setCommTotalAll(mapTotal, format, resultMapTotal);//保存一行社区总数
                    }
                }else{
                    Date firstDay = getFirstDay(null);
                    Date lastDay = getLastDay(null);
                    Map<String, Object> mapTotal = billMonthsReportMapper.selectBillMonthsReport(firstDay, lastDay, null,null);
                    setCommTotalAll(mapTotal, formatFirstDay, resultMapTotal);//保存一行社区总数
                }
                fList.add(resultMapTotal);
            }
            return fList;
        }

    }

    private void setCommRuleTotal(String ruleName,String commName,List<Map<String, Object>> rules,Map<String,List<Map<String,Object>>> billRuleAll,List<Map<String,Object>> fList){
        for (Map<String, Object> rule : rules) {
            Map<String,Object> resultMap = new LinkedHashMap<>();
            setValue(ruleName,commName,billRuleAll,rule,resultMap);
            fList.add(resultMap);
        }
    }

    private void setCommTotal(Map<String, Object> mapTotal,String format,Map<String,Object> resultMap){
        resultMap.put("费用标准","合计");
        if(mapTotal == null){
            resultMap.put(format+"账单金额",new BigDecimal(0));
            resultMap.put(format+"违约金",new BigDecimal(0));
            resultMap.put(format+"调整金额",new BigDecimal(0));
            resultMap.put(format+"收款金额",new BigDecimal(0));
            resultMap.put(format+"欠款金额",new BigDecimal(0));
        }else{
            if(mapTotal.get("price")==null){
                resultMap.put(format+"账单金额",new BigDecimal(0));
            }else{
                resultMap.put(format+"账单金额",mapTotal.get("price"));
            }
            if(mapTotal.get("overdueCost")==null){
                resultMap.put(format+"违约金",new BigDecimal(0));
            }else{
                resultMap.put(format+"违约金",mapTotal.get("overdueCost"));
            }
            if(mapTotal.get("salePrice")==null){
                resultMap.put(format+"调整金额",new BigDecimal(0));
            }else{
                resultMap.put(format+"调整金额",mapTotal.get("salePrice"));
            }
            if(mapTotal.get("payPrice")==null){
                resultMap.put(format+"收款金额",new BigDecimal(0));
            }else{
                resultMap.put(format+"收款金额",mapTotal.get("payPrice"));
            }
            if(mapTotal.get("owed")==null){
                resultMap.put(format+"欠款金额",new BigDecimal(0));
            }else{
                resultMap.put(format+"欠款金额",mapTotal.get("owed"));
            }
        }
    }

    private void setCommTotalAll(Map<String, Object> mapTotal,String format,Map<String,Object> resultMap){
        resultMap.put("费用标准","合计");
        if(mapTotal == null){
            if(StringUtils.isEmpty(resultMap.get(format+"账单金额"))){
                resultMap.put(format+"账单金额",new BigDecimal(0));
            }
            if(StringUtils.isEmpty(resultMap.get(format+"违约金"))) {
                resultMap.put(format + "违约金", new BigDecimal(0));
            }
            if(StringUtils.isEmpty(resultMap.get(format+"调整金额"))) {
                resultMap.put(format + "调整金额", new BigDecimal(0));
            }
            if(StringUtils.isEmpty(resultMap.get(format+"收款金额"))) {
                resultMap.put(format + "收款金额", new BigDecimal(0));
            }
            if(StringUtils.isEmpty(resultMap.get(format+"欠款金额"))) {
                resultMap.put(format + "欠款金额", new BigDecimal(0));
            }
        }else{
            if(resultMap.get(format+"账单金额")!=null){
                if(mapTotal.get("price")!=null){
                    resultMap.put(format+"账单金额",((BigDecimal)resultMap.get(format+"账单金额")).add((BigDecimal)mapTotal.get("price")));
                }
            }else{
                if(mapTotal.get("price")!=null){
                    resultMap.put(format+"账单金额",mapTotal.get("price"));
                }
            }
            if(mapTotal.get("overdueCost")!=null){
                if(resultMap.get(format+"违约金")!=null){
                    resultMap.put(format+"违约金",((BigDecimal)resultMap.get(format+"违约金")).add((BigDecimal)mapTotal.get("overdueCost")));
                }else{
                    resultMap.put(format+"违约金",mapTotal.get("overdueCost"));
                }
            }
            if(mapTotal.get("salePrice")!=null){
                if(resultMap.get(format+"调整金额")!=null){
                    resultMap.put(format+"调整金额",((BigDecimal)resultMap.get(format+"调整金额")).add((BigDecimal)mapTotal.get("salePrice")));
                }else{
                    resultMap.put(format+"调整金额",mapTotal.get("salePrice"));
                }
            }
            if(mapTotal.get("payPrice")!=null){
                if(resultMap.get(format+"收款金额")!=null){
                    resultMap.put(format+"收款金额",((BigDecimal)resultMap.get(format+"收款金额")).add((BigDecimal)mapTotal.get("payPrice")));
                }else{
                    resultMap.put(format+"收款金额",mapTotal.get("payPrice"));
                }
            }
            if(mapTotal.get("owed")!=null){
                if(resultMap.get(format+"欠款金额")!=null){
                    resultMap.put(format+"欠款金额",((BigDecimal)resultMap.get(format+"欠款金额")).add((BigDecimal)mapTotal.get("owed")));
                }else{
                    resultMap.put(format+"欠款金额",mapTotal.get("owed"));
                }
            }
        }
    }

    private void setValue(String ruleName,String commName,Map<String,List<Map<String,Object>>> billRuleAll,Map<String, Object> rule,Map<String,Object> resultMap){
        for (Map.Entry<String, List<Map<String, Object>>> stringListEntry : billRuleAll.entrySet()) {
            String key = stringListEntry.getKey();
            List<Map<String, Object>> value = stringListEntry.getValue();
            if(value.size()>0){
                for (Map<String, Object> map : value) {
                    if(rule.get("name").equals(map.get("name"))){
                        if(!StringUtils.isEmpty(commName)){
                            resultMap.put("社区",commName);
                        }
                        resultMap.put("费用标准",map.get("name"));
                        resultMap.put(key+"账单金额",map.get("price"));
                        resultMap.put(key+"违约金",map.get("overdueCost"));
                        resultMap.put(key+"调整金额",map.get("salePrice"));
                        resultMap.put(key+"收款金额",map.get("payPrice"));
                        resultMap.put(key+"欠款金额",map.get("owed"));
                    }
                }
            }else{
                if(!StringUtils.isEmpty(commName)){
                    resultMap.put("社区",commName);
                }
                if(!StringUtils.isEmpty(ruleName)){
                    resultMap.put("费用标准",ruleName);
                }
                resultMap.put(key+"账单金额",null);
                resultMap.put(key+"违约金",null);
                resultMap.put(key+"调整金额",null);
                resultMap.put(key+"收款金额",null);
                resultMap.put(key+"欠款金额",null);
            }
        }
    }

    /**
     * 获取本月第一天
     * @return
     */
    public Date getFirstDay(Date date){
        Calendar calendar = Calendar.getInstance();
        if(!StringUtils.isEmpty(date)){
            calendar.setTime(date);
        }else{
            calendar.setTime(new Date());
        }
        calendar.set(Calendar.DAY_OF_MONTH,calendar.getActualMinimum(Calendar.DAY_OF_MONTH));
        return calendar.getTime();
    }

    /**
     * 获取本月最后一天
     * @return
     */
    public Date getLastDay(Date date){
        Calendar calendar2 = Calendar.getInstance();
        if(!StringUtils.isEmpty(date)){
            calendar2.setTime(date);
        }else{
            calendar2.setTime(new Date());
        }
        calendar2.set(Calendar.DAY_OF_MONTH, calendar2.getActualMaximum(Calendar.DAY_OF_MONTH));
        return calendar2.getTime();
    }

    public List<Date> getDate(){
        List<Date> dates = new ArrayList<>();
        Calendar calendar = Calendar.getInstance();
        for (int i = 0; i < 12; i++) {
            calendar.setTime(new Date());
            calendar.add(Calendar.MONTH,-i);
            dates.add(calendar.getTime());
        }
        return dates;
    }

    private SUser getUserByToken(String token) {
        Object o = redisTemplate.opsForValue().get(token);
        if (null == o) {
            log.error("登录失效，请重新登录。");
            throw new BillException(BillExceptionEnum.LOGIN_TIME_OUT);
        }
        return (SUser) o;
    }
}
