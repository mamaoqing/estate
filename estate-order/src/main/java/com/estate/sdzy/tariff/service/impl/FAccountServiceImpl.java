package com.estate.sdzy.tariff.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.estate.common.entity.SUser;
import com.estate.common.exception.BillException;
import com.estate.common.exception.OrderException;
import com.estate.common.util.BillExceptionEnum;
import com.estate.common.util.OrderExceptionEnum;
import com.estate.sdzy.tariff.entity.FAccount;
import com.estate.sdzy.tariff.entity.FAccountCostItem;
import com.estate.sdzy.tariff.entity.FFinanceRecord;
import com.estate.sdzy.tariff.mapper.FAccountCostItemMapper;
import com.estate.sdzy.tariff.mapper.FAccountMapper;
import com.estate.sdzy.tariff.service.FAccountCostItemService;
import com.estate.sdzy.tariff.service.FAccountService;
import com.estate.sdzy.tariff.service.FFinanceRecordService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.List;
import java.util.Map;

/**
 * <p>
 * 费用项目 服务实现类
 * </p>
 *
 * @author mzc
 * @since 2020-09-10
 */
@Service
@Slf4j
public class FAccountServiceImpl extends ServiceImpl<FAccountMapper, FAccount> implements FAccountService {

    @Autowired
    private FAccountMapper accountMapper;

    @Autowired
    private FFinanceRecordService financeRecordService;

    @Autowired
    private FAccountCostItemService accountCostItemService;

    @Autowired
    private FAccountCostItemMapper accountCostItemMapper;

    @Autowired
    private RedisTemplate redisTemplate;

    @Override
    public Page<FAccount> listAccount(Map<String, String> map,Integer pageNo, Integer size,String token) {
        if (StringUtils.isEmpty(pageNo)) {
            throw new BillException(BillExceptionEnum.PAGENO_MISS_ERROR);
        }
        if (!StringUtils.isEmpty(map.get("size"))) {
            size = Integer.valueOf(map.get("size"));
        }
        SUser user = getUserByToken(token);
        Page<FAccount> page = new Page<>(pageNo,size);
        if(user.getCompId()==0){
            Page<FAccount> listAccount = accountMapper.listAccount(page,map,null);
            return listAccount;
        }else{
            Page<FAccount> listAccount = accountMapper.listAccount(page,map,user.getId());
            return listAccount;
        }
    }

    @Override
    public boolean save(FAccount account, String token) {
        QueryWrapper<FAccount> queryWrapper = new QueryWrapper();
        queryWrapper.eq("no",account.getNo());
        List<FAccount> fAccounts = accountMapper.selectList(queryWrapper);
        if(fAccounts.size()>0){
            return false;
        }else{
            if(!StringUtils.isEmpty(account.getId())){//执行修改操作
                saveOrUpdate(account,token);
                saveAccountCostItem(account,token);
                return true;
            }else{
                SUser user = getUserByToken(token);
                account.setCreatedBy(user.getId());
                account.setCreatedName(user.getUserName());
                account.setModifiedBy(user.getId());
                account.setModifiedName(user.getName());
                if(StringUtils.isEmpty(account.getNo())){
                    String maxNo = accountMapper.getMaxNo();
                    account.setNo(maxNo);
                }
                int insert = accountMapper.insert(account);
                if (insert > 0) {
                /*如果选出的业主与费用标准与之前的账号验证相同，其他数据将带出原账户信息，添加账户操作改修改账户操作，
                账户名称（不是手工输入，而是通过业主名称和费用标准名称组合生成。），预存账号编号（可以自动生产可以手工输入，保证同一社区的唯一性），
                输入余额、备注信息，录入人为当前登录人，录入时间为系统时间，修改人为当前登录人、修改时间为系统时间。
                增加一个账户信息的同时添加一条财务流水，操作f_account、f_finance_record两个表*/
                    log.info("费用项目添加成功，添加人:{}", user.getUserName());
                    saveFinanceRecord(account,token,false);
                    saveAccountCostItem(account,token);
                    return true;
                }
                throw new OrderException(OrderExceptionEnum.SYSTEM_INSERT_ERROR);
            }
        }
    }

    public boolean saveAccountCostItem(FAccount account,String token){
        QueryWrapper<FAccountCostItem> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("account_id",account.getId());
        queryWrapper.eq("rule_id",account.getRuleId());
        queryWrapper.eq("property_id",account.getPropertyId());
        List<FAccountCostItem> fAccountCostItems = accountCostItemMapper.selectList(queryWrapper);
        if(fAccountCostItems.size()>0){
            return true;
        }else{
            FAccountCostItem item = new FAccountCostItem();
            item.setCompId(account.getCompId());
            item.setCommId(account.getCommId());
            item.setRuleId(account.getRuleId());
            item.setAccountId(account.getId());
            item.setPropertyId(account.getPropertyId());
            item.setPropertyType(account.getPropertyType());
            boolean save = accountCostItemService.save(item, token);
            return save;
        }
    }

    public boolean saveFinanceRecord(FAccount account,String token,boolean isUpdate){
        FFinanceRecord financeRecord = new FFinanceRecord();
        financeRecord.setCompId(account.getCompId());
        financeRecord.setCommId(account.getCommId());
        financeRecord.setAccountId(account.getId());
        if(isUpdate){//修改操作
            financeRecord.setOperType("预存");
            financeRecord.setCost(account.getFeeAdd());
        }else{
            financeRecord.setOperType("取现");
            financeRecord.setCost(account.getFee());
        }
        financeRecord.setOwnerId(account.getOwnerId());
        financeRecord.setPaymentMethod(account.getPaymentMethod());
        financeRecord.setRemark(account.getRemark());
        boolean save = financeRecordService.save(financeRecord, token);
        return save;
    }

    @Override
    public boolean saveOrUpdate(FAccount account, String token) {
        SUser user = getUserByToken(token);
        if(!StringUtils.isEmpty(account.getIsReNew())){//新增不做处理
            if(account.getIsReNew()){//续费
                account.setFee(account.getFee().add(account.getFeeAdd()));
            }else{//提现
                account.setFee(account.getFee().subtract(account.getFeeAdd()));
            }
        }
        account.setModifiedBy(user.getId());
        account.setModifiedName(user.getName());
        int update = accountMapper.updateById(account);
        if(update > 0){
            if(account.getIsReNew()) {//续费
                log.info("续费成功，修改人:{}", user.getUserName());
                saveFinanceRecord(account,token,true);
            }else{
                log.info("提现成功，修改人:{}", user.getUserName());
                saveFinanceRecord(account,token,true);
            }
            return true;
        }
        throw new OrderException(OrderExceptionEnum.SYSTEM_UPDATE_ERROR);
    }

    public FAccount getAccount(Long ownerId,Long ruleId){
        FAccount account = accountMapper.getAccount(ownerId, ruleId);
        if(account!=null){
            return account;
        }else{
            return null;
        }
    }

    private SUser getUserByToken(String token) {
        Object o = redisTemplate.opsForValue().get(token);
        if (null == o) {
            log.error("登录失效，请重新登录。");
            throw new OrderException(OrderExceptionEnum.LOGIN_TIME_OUT);
        }
        return (SUser) o;
    }
}
