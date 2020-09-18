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
import com.estate.sdzy.tariff.entity.FCostRule;
import com.estate.sdzy.tariff.entity.FFinanceRecord;
import com.estate.sdzy.tariff.mapper.FAccountCostItemMapper;
import com.estate.sdzy.tariff.mapper.FAccountMapper;
import com.estate.sdzy.tariff.mapper.FCostRuleMapper;
import com.estate.sdzy.tariff.service.FAccountCostItemService;
import com.estate.sdzy.tariff.service.FAccountService;
import com.estate.sdzy.tariff.service.FFinanceRecordService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.util.Arrays;
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
@Service(value="accountService")
@Slf4j
public class FAccountServiceImpl extends ServiceImpl<FAccountMapper, FAccount> implements FAccountService {

    @Autowired
    private FAccountMapper fAccountMapper;

    @Autowired
    private FAccountCostItemMapper fAccountCostItemMapper;

    @Autowired
    private FFinanceRecordService financeRecordService;

    @Autowired
    private FAccountCostItemService accountCostItemService;

    @Autowired
    private FAccountCostItemMapper accountCostItemMapper;

    @Autowired
    private RedisTemplate redisTemplate;

    @Autowired
    private FCostRuleMapper fCostRuleMapper;

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
            Page<FAccount> listAccount = fAccountMapper.listAccount(page,map,null);
            return listAccount;
        }else{
            Page<FAccount> listAccount = fAccountMapper.listAccount(page,map,user.getId());
            return listAccount;
        }
    }

    @Override
    @Transactional
    public boolean saveAccount(FAccount account, String token) {
        QueryWrapper<FAccount> queryWrapper = new QueryWrapper();
        queryWrapper.eq("no",account.getNo());
        List<FAccount> fAccounts = fAccountMapper.selectList(queryWrapper);
        if(fAccounts.size()>0&&StringUtils.isEmpty(account.getId())){
            return false;
        }else{
            if(!StringUtils.isEmpty(account.getId())){//执行修改操作
                saveOrUpdate(account,token);
                //saveAccountCostItem(account,token);
                //saveFinanceRecord(account,token,true);
                return true;
            }else{
                SUser user = getUserByToken(token);
                account.setCreatedBy(user.getId());
                account.setCreatedName(user.getUserName());
                account.setModifiedBy(user.getId());
                account.setModifiedName(user.getName());
                if(StringUtils.isEmpty(account.getNo())){
                    account.setNo(String.valueOf(System.currentTimeMillis()));
                }
                int insert = fAccountMapper.insert(account);
                if (insert > 0) {
                /*如果选出的业主与费用标准与之前的账号验证相同，其他数据将带出原账户信息，添加账户操作改修改账户操作，
                账户名称（不是手工输入，而是通过业主名称和费用标准名称组合生成。），预存账号编号（可以自动生产可以手工输入，保证同一社区的唯一性），
                输入余额、备注信息，录入人为当前登录人，录入时间为系统时间，修改人为当前登录人、修改时间为系统时间。
                增加一个账户信息的同时添加一条财务流水，操作f_account、f_finance_record两个表*/
                    log.info("费用项目添加成功，添加人:{}", user.getUserName());
                    saveFinanceRecord(account,token,true);
                    saveAccountCostItem(account,token);
                    return true;
                }
                throw new OrderException(OrderExceptionEnum.SYSTEM_INSERT_ERROR);
            }
        }
    }

    public boolean saveAccountCostItem(FAccount account,String token){
        if(account.getRuleId().indexOf(",")!=-1){//多选费用标准
            String[] splits = account.getRuleId().split(",");
            String[] pn = account.getPropertyType().split(",");
            String[] pi = account.getPropertyId().split(",");
            for(int i=0;i<splits.length;i++){
                QueryWrapper<FAccountCostItem> queryWrapper = new QueryWrapper<>();
                queryWrapper.eq("account_id",account.getId());
                queryWrapper.eq("rule_id",splits[i]);

                queryWrapper.eq("property_type",pn[i]);
                queryWrapper.eq("property_id",pi[i]);
                List<FAccountCostItem> fAccountCostItems = accountCostItemMapper.selectList(queryWrapper);
                if(fAccountCostItems.size()>0){
                    throw new OrderException(OrderExceptionEnum.SYSTEM_SELECT_ERROR);
                }
                save(account,token,Long.valueOf(splits[i]),pn[i],pi[i]);
            }
            return true;
        }else{
            return save(account,token,Long.valueOf(account.getRuleId()),account.getPropertyType(),account.getPropertyId());
        }
    }

    public Boolean save(FAccount account,String token,Long ruleId,String propertyType,String propertyId){
        FAccountCostItem item = new FAccountCostItem();
        item.setCompId(account.getCompId());
        item.setCommId(account.getCommId());
        item.setRuleId(ruleId);
        item.setAccountId(account.getId());
        item.setPropertyId(Long.valueOf(propertyId));
        item.setPropertyType(propertyType);
        boolean save = accountCostItemService.save(item, token);
        return save;
    }

    public boolean saveFinanceRecord(FAccount account,String token,boolean isUpdate){
        FFinanceRecord financeRecord = new FFinanceRecord();
        financeRecord.setCompId(account.getCompId());
        financeRecord.setCommId(account.getCommId());
        financeRecord.setAccountId(account.getId());
        if(isUpdate){//修改操作
            financeRecord.setOperType("预存");
            if(StringUtils.isEmpty(account.getFeeAdd())){
                financeRecord.setCost(account.getFee());
            }else{
                financeRecord.setCost(account.getFeeAdd());
            }
        }else{
            financeRecord.setOperType("取现");
            financeRecord.setCost(account.getFeeAdd());
        }
        financeRecord.setOwnerId(account.getOwnerId());
        financeRecord.setPaymentMethod(account.getPaymentMethod());
        financeRecord.setRemark(account.getRemark());
        boolean save = financeRecordService.saveRecord(financeRecord, token);
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
        int update = fAccountMapper.updateById(account);
        if(update > 0){
            if(StringUtils.isEmpty(account.getIsReNew())){
                log.info("修改账户成功，修改人:{}", user.getUserName());
            }else{
                if(account.getIsReNew()) {//续费
                    log.info("续费成功，修改人:{}", user.getUserName());
                    saveFinanceRecord(account,token,true);
                }else {
                    log.info("提现成功，修改人:{}", user.getUserName());
                    saveFinanceRecord(account,token,false);
                }
            }
            return true;
        }
        throw new OrderException(OrderExceptionEnum.SYSTEM_UPDATE_ERROR);
    }

    @Transactional
    @Override
    public boolean doUpdate(FAccount account, String token){
        saveOrUpdate(account, token);
        FAccount findAccount = getAccount(account.getOwnerId(), account.getRuleId());
        if(findAccount!=null){//说明已经有相应的费用项目对应的账户，不可编辑
            return false;
        }else{
            QueryWrapper<FAccountCostItem> queryWrapper = new QueryWrapper<>();
            queryWrapper.eq("account_id",account.getId());
            int delete = fAccountCostItemMapper.delete(queryWrapper);
            if(delete>0){
                saveAccountCostItem(account, token);
            }else{
                throw new OrderException(OrderExceptionEnum.SYSTEM_DELETE_ERROR);
            }
        }
        return true;
    }

    @Override
    public List<FAccountCostItem> getAccountItemByAccountId(String accountId) {
        List<FAccountCostItem> accountItems = fAccountMapper.getAccountItemByAccountId(accountId);
        return accountItems;
    }

    public FAccount getAccount(Long ownerId,String ruleId){
        //判断用户是否已将建立账号
        QueryWrapper<FAccount> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("owner_id",ownerId);
        List<FAccount> fAccounts = fAccountMapper.selectList(queryWrapper);//业主所有的账号
        if(fAccounts.size()>0){//该业主已经有账号
            List<FCostRule> fCostRules = fCostRuleMapper.getRuleByOwnerId(ownerId);//与业主对应的所有的费用标准
            //根据账户查找对应的ruleIds,如果查出的ruleIds和传过来的ruleId相同，则表示已经按照对应的费用标准建立了账号
            //如果不同则表示没有建立对应标准的账号
            String[] split = ruleId.split(",");
            List<String> ruleIds = Arrays.asList(split);

            for(FAccount a:fAccounts){
                //判断每一个账号是否与
                //select distinct rule_id from f_account_cost_item where account_id in (select id from f_account where owner_id=26)
                //如果完全相同则返回该账单
                QueryWrapper<FAccountCostItem> queryWrapperItem = new QueryWrapper<>();
                queryWrapperItem.eq("account_id",a.getId());
                List<FAccountCostItem> fAccountItems = fAccountCostItemMapper.selectList(queryWrapperItem);
                if(ruleIds.size()==fAccountItems.size()&&ruleIds.size()>1){
                    boolean islike = true;
                    for(FAccountCostItem item:fAccountItems){
                        if(!ruleIds.contains(String.valueOf(item.getRuleId()))){//如果不同则返回null
                            islike = false;
                            break;
                        }
                    }
                    if(islike){
                        return a;
                    }
                }else if(ruleIds.size()==fAccountItems.size()&&ruleIds.size()==1){
                    for(FAccountCostItem item:fAccountItems){
                        if(ruleIds.contains(String.valueOf(item.getRuleId()))){//如果相同同则返回账单
                            return a;
                        }else{
                        }
                    }
                }
            }
            return null;
        }else{//该业主没有帐户
            return null;
        }/*
        //



        FAccount account = fAccountMapper.getAccount(ownerId, ruleId);
        if(account!=null){
            return account;
        }else{
            return null;
        }*/
    }

    public List<FAccount> getAccountByOwnerId(Long ownerId,String token){
        getUserByToken(token);
        if(StringUtils.isEmpty(ownerId)){
            throw new BillException(BillExceptionEnum.PARAMS_MISS_ERROR);
        }
        QueryWrapper<FAccount> w = new QueryWrapper<>();
        w.eq("owner_id",ownerId);
        return fAccountMapper.selectList(w);
    }

    @Override
    public List<Map<String,String>> listTypes(Map<String, String> map, String token) {
        if("房产".equals(map.get("propertyType"))){
            return fAccountMapper.getRoom(map);
        }else{
            return fAccountMapper.getParking(map);
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
