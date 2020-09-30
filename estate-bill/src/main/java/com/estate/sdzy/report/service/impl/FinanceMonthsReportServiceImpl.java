package com.estate.sdzy.report.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.estate.common.entity.SUser;
import com.estate.common.exception.BillException;
import com.estate.common.util.BillExceptionEnum;
import com.estate.sdzy.asstes.entity.RCommunity;
import com.estate.sdzy.asstes.mapper.RCommunityMapper;
import com.estate.sdzy.report.entity.FinanceMonthsReport;
import com.estate.sdzy.report.mapper.FinanceMonthsReportMapper;
import com.estate.sdzy.report.service.FinanceMonthsReportService;
import com.estate.sdzy.system.service.SUserCommService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.math.BigDecimal;
import java.util.*;

@Service
@Slf4j
public class FinanceMonthsReportServiceImpl extends ServiceImpl<FinanceMonthsReportMapper, FinanceMonthsReport> implements FinanceMonthsReportService {

    @Autowired
    private FinanceMonthsReportMapper financeMonthsReportMapper;

    @Autowired
    private RedisTemplate<String, Object> redisTemplate;

    @Autowired
    private RCommunityMapper rCommunityMapper;

    @Autowired
    private SUserCommService sUserCommService;

    @Override
    public List<FinanceMonthsReport> listFinanceMonthsReport(Map<String, String> map, String token) {
        List<FinanceMonthsReport> fList = new ArrayList<>();
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
        if(!StringUtils.isEmpty(map.get("ruleId"))){
            FinanceMonthsReport financeReportTotal = new FinanceMonthsReport();
            financeReportTotal.setRuleName("合计");
            for (RCommunity comm : rCommunities) {
                List<Map<String,Object>> rules = new ArrayList<>();
                Map<String,Object> mapAdd = new HashMap<>();
                mapAdd.put("id",Long.valueOf(map.get("ruleId")));
                mapAdd.put("name",map.get("ruleName"));
                rules.add(mapAdd);
                if(rCommunities.size()>1){
                    setFinanceMonthsReport(rules,comm.getId(),fList,comm.getName(),Long.valueOf(map.get("ruleId")),financeReportTotal,map.get("year"));
                }else{
                    setFinanceMonthsReport(rules,comm.getId(),fList,comm.getName(),Long.valueOf(map.get("ruleId")),null,map.get("year"));
                }
            }
            if(rCommunities.size()>1){
                fList.add(financeReportTotal);
            }
            return fList;
        }else{
            FinanceMonthsReport financeReportTotal = new FinanceMonthsReport();
            financeReportTotal.setRuleName("合计");
            for (RCommunity comm : rCommunities) {
                List<Map<String,Object>> rules = financeMonthsReportMapper.selectRule(comm.getId());
                FinanceMonthsReport financeReport = new FinanceMonthsReport();
                financeReport.setComm_id(comm.getId());
                financeReport.setCommName(comm.getName());
                financeReport.setRuleName("合计");
                //本月总收费额(总)
                BigDecimal totalPay = financeMonthsReportMapper.getTotalPay(getFirstDay(map.get("year")), getLastDay(map.get("year")), comm.getId());
                if(!StringUtils.isEmpty(totalPay)){
                    financeReport.setTotalPay(totalPay);
                }else{
                    financeReport.setTotalPay(new BigDecimal(0));
                }
                //本月应收额(总)
                BigDecimal receivable = financeMonthsReportMapper.getReceivable(getFirstDay(map.get("year")), getLastDay(map.get("year")), comm.getId());
                if(!StringUtils.isEmpty(receivable)){
                    financeReport.setReceivable(receivable);
                }else{
                    financeReport.setReceivable(new BigDecimal(0));
                }
                //本月欠费额
                BigDecimal owed = financeMonthsReportMapper.getOwed(getFirstDay(map.get("year")), getLastDay(map.get("year")), comm.getId());
                if(!StringUtils.isEmpty(owed)){
                    financeReport.setOwed(owed);
                }else{
                    financeReport.setOwed(new BigDecimal(0));
                }
                //本月收本月金额
                BigDecimal received = financeMonthsReportMapper.getReceived(getFirstDay(map.get("year")), getLastDay(map.get("year")), comm.getId());
                if(!StringUtils.isEmpty(received)){
                    financeReport.setReceived(received);
                }else{
                    financeReport.setReceived(new BigDecimal(0));
                }
                //本月收往期金额
                BigDecimal previousPeriodReceived = financeMonthsReportMapper.getPreviousPeriodReceived(getFirstDay(map.get("year")), getLastDay(map.get("year")), comm.getId());
                if(!StringUtils.isEmpty(previousPeriodReceived)){
                    financeReport.setPreviousPeriodReceived(previousPeriodReceived);
                }else{
                    financeReport.setPreviousPeriodReceived(new BigDecimal(0));
                }
                //往期欠费金额
                BigDecimal previousOwed = financeMonthsReportMapper.getPreviousOwed(getFirstDay(map.get("year")), getLastDay(map.get("year")), comm.getId());
                if(!StringUtils.isEmpty(previousOwed)){
                    financeReport.setPreviousOwed(previousOwed);
                }else{
                    financeReport.setPreviousOwed(new BigDecimal(0));
                }
                if(rCommunities.size()>1){
                    setTotal(financeReportTotal,financeReport);
                }
                fList.add(financeReport);
                setFinanceMonthsReport(rules,comm.getId(),fList,comm.getName(),null,null,map.get("year"));
            }
            if(rCommunities.size()>1){
                fList.add(financeReportTotal);
            }
            return fList;
        }

    }

    private void setFinanceMonthsReport(List<Map<String,Object>> rules,Long commId,List<FinanceMonthsReport> fList,String commName,Long ruleId,
                                        FinanceMonthsReport financeReportTotal,String year){
        for (Map<String, Object> mapRules : rules) {
            FinanceMonthsReport financeReport = new FinanceMonthsReport();
            if(!StringUtils.isEmpty(ruleId)){
                financeReport.setCommName(commName);
            }
            //本月总收费额(按不同的收费标准)
            Map<String, Object> totalPayByRule = financeMonthsReportMapper.getTotalPayByRule(getFirstDay(year), getLastDay(year), commId, (Long)mapRules.get("id"));
            financeReport.setRuleName((String) mapRules.get("name"));
            if (totalPayByRule.get("totalPay") == null) {
                financeReport.setTotalPay(new BigDecimal(0));
            } else {
                financeReport.setTotalPay((BigDecimal) totalPayByRule.get("totalPay"));
            }
            //本月应收额(按不同的收费标准)getReceivableByRule
            Map<String, Object> receivable = financeMonthsReportMapper.getReceivableByRule(getFirstDay(year), getLastDay(year), commId, (Long)mapRules.get("id"));
            if(receivable.get("receivable") == null){
                financeReport.setReceivable(new BigDecimal(0));
            }else{
                financeReport.setReceivable((BigDecimal) receivable.get("receivable"));
            }
            //本月欠费额(按不同的收费标准)getOwedByRule
            Map<String, Object> owed = financeMonthsReportMapper.getOwedByRule(getFirstDay(year), getLastDay(year), commId, (Long)mapRules.get("id"));
            if(owed.get("owed") == null){
                financeReport.setOwed(new BigDecimal(0));
            }else{
                financeReport.setOwed((BigDecimal) owed.get("owed"));
            }
            //本月收本月金额(按不同的收费标准)getReceivedByRule
            Map<String, Object> received = financeMonthsReportMapper.getReceivedByRule(getFirstDay(year), getLastDay(year), commId, (Long)mapRules.get("id"));
            if(received.get("received") == null){
                financeReport.setReceived(new BigDecimal(0));
            }else{
                financeReport.setReceived((BigDecimal) received.get("received"));
            }
            //本月收往期金额(按不同的收费标准)getPreviousPeriodReceivedByRule
            Map<String, Object> previousPeriodReceived = financeMonthsReportMapper.getPreviousPeriodReceivedByRule(getFirstDay(year), getLastDay(year), commId, (Long)mapRules.get("id"));
            if(previousPeriodReceived.get("previousPeriodReceived") == null){
                financeReport.setPreviousPeriodReceived(new BigDecimal(0));
            }else{
                financeReport.setPreviousPeriodReceived((BigDecimal) previousPeriodReceived.get("previousPeriodReceived"));
            }
            //往期欠费金额(按不同的收费标准)getPreviousOwedByRule
            Map<String, Object> previousOwedByRule = financeMonthsReportMapper.getPreviousOwedByRule(getFirstDay(year), getLastDay(year), commId, (Long)mapRules.get("id"));
            if(previousOwedByRule.get("previousOwedByRule") == null){
                financeReport.setPreviousOwed(new BigDecimal(0));
            }else{
                financeReport.setPreviousOwed((BigDecimal) previousOwedByRule.get("previousOwedByRule"));
            }
            fList.add(financeReport);
            if(!StringUtils.isEmpty(financeReportTotal)){
                setTotal(financeReportTotal,financeReport);
            }
        }

    }

    private void setTotal(FinanceMonthsReport financeReportTotal,FinanceMonthsReport financeReport){
        if(StringUtils.isEmpty(financeReportTotal.getTotalPay())){
            financeReportTotal.setTotalPay(financeReport.getTotalPay());
        }else{
            //if(!StringUtils.isEmpty(financeReport.getTotalPay())){
                financeReportTotal.setTotalPay(financeReportTotal.getTotalPay().add(financeReport.getTotalPay()));
            //}
        }
        if(StringUtils.isEmpty(financeReportTotal.getReceivable())){
            financeReportTotal.setReceivable(financeReport.getReceivable());
        }else{
            //if(!StringUtils.isEmpty(financeReport.getReceivable())){
                financeReportTotal.setReceivable(financeReportTotal.getReceivable().add(financeReport.getReceivable()));
            //}
        }
        if(StringUtils.isEmpty(financeReportTotal.getOwed())){
            financeReportTotal.setOwed(financeReport.getOwed());
        }else{
            //if(!StringUtils.isEmpty(financeReport.getOwed())){
                financeReportTotal.setOwed(financeReportTotal.getOwed().add(financeReport.getOwed()));
            //}
        }
        if(StringUtils.isEmpty(financeReportTotal.getReceived())){
            financeReportTotal.setReceived(financeReport.getReceived());
        }else{
            //if(!StringUtils.isEmpty(financeReport.getReceived())){
                financeReportTotal.setReceived(financeReportTotal.getReceived().add(financeReport.getReceived()));
            //}
        }
        if(StringUtils.isEmpty(financeReportTotal.getPreviousPeriodReceived())){
            financeReportTotal.setPreviousPeriodReceived(financeReport.getPreviousPeriodReceived());
        }else{
            //if(!StringUtils.isEmpty(financeReport.getPreviousPeriodReceived())){
                financeReportTotal.setPreviousPeriodReceived(financeReportTotal.getPreviousPeriodReceived().add(financeReport.getPreviousPeriodReceived()));
            //}
        }
        if(StringUtils.isEmpty(financeReportTotal.getPreviousOwed())){
            financeReportTotal.setPreviousOwed(financeReport.getPreviousOwed());
        }else{
            //if(!StringUtils.isEmpty(financeReport.getPreviousOwed())){
                financeReportTotal.setPreviousOwed(financeReportTotal.getPreviousOwed().add(financeReport.getPreviousOwed()));
            //}
        }
    }

    /**
     * 获取本月第一天
     * @return
     */
    public Date getFirstDay(String year){
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(new Date());
        if(StringUtils.isEmpty(year)){
            calendar.set(Calendar.DAY_OF_MONTH,calendar.getActualMinimum(Calendar.DAY_OF_MONTH));
        }else{
            calendar.set(Calendar.DAY_OF_YEAR,calendar.getActualMinimum(Calendar.DAY_OF_YEAR));
        }
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);
        return calendar.getTime();
    }
    /**
     * 获取本月最后一天
     * @return
     */
    public Date getLastDay(String year){
        Calendar calendar2 = Calendar.getInstance();
        calendar2.setTime(new Date());
        if(StringUtils.isEmpty(year)) {
            calendar2.set(Calendar.DAY_OF_MONTH, calendar2.getActualMaximum(Calendar.DAY_OF_MONTH));
        }else{
            calendar2.set(Calendar.DAY_OF_YEAR, calendar2.getActualMaximum(Calendar.DAY_OF_YEAR));
        }
        calendar2.set(Calendar.HOUR_OF_DAY, 23);
        calendar2.set(Calendar.MINUTE, 59);
        calendar2.set(Calendar.SECOND, 59);
        calendar2.set(Calendar.MILLISECOND, 999);
        return calendar2.getTime();
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
