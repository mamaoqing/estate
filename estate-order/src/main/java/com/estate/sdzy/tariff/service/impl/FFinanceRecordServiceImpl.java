package com.estate.sdzy.tariff.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.estate.common.entity.SUser;
import com.estate.common.exception.BillException;
import com.estate.common.exception.OrderException;
import com.estate.common.util.BillExceptionEnum;
import com.estate.common.util.OrderExceptionEnum;
import com.estate.sdzy.asstes.entity.ROwner;
import com.estate.sdzy.asstes.entity.ROwnerProperty;
import com.estate.sdzy.asstes.mapper.ROwnerMapper;
import com.estate.sdzy.asstes.mapper.ROwnerPropertyMapper;
import com.estate.sdzy.asstes.service.ROwnerPropertyService;
import com.estate.sdzy.tariff.entity.FAccount;
import com.estate.sdzy.tariff.entity.FBill;
import com.estate.sdzy.tariff.entity.FFinanceBillRecord;
import com.estate.sdzy.tariff.entity.FFinanceRecord;
import com.estate.sdzy.tariff.mapper.FAccountMapper;
import com.estate.sdzy.tariff.mapper.FBillMapper;
import com.estate.sdzy.tariff.mapper.FFinanceRecordMapper;
import com.estate.sdzy.tariff.service.FFinanceBillRecordService;
import com.estate.sdzy.tariff.service.FFinanceRecordService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * 财务流水 服务实现类
 * </p>
 *
 * @author mq
 * @since 2020-09-08
 */
@Slf4j
@Service
public class FFinanceRecordServiceImpl extends ServiceImpl<FFinanceRecordMapper, FFinanceRecord> implements FFinanceRecordService {

    @Autowired
    private RedisTemplate redisTemplate;
    @Autowired
    private FFinanceRecordMapper mapper;
    @Autowired
    private ROwnerMapper ownerMapper;
    @Autowired
    private ROwnerPropertyService ownerPropertyService;
    @Autowired
    private ROwnerPropertyMapper ownerPropertyMapper;
    @Autowired
    private FBillMapper billMapper;
    @Autowired
    private FFinanceBillRecordService fFinanceBillRecordService;
    @Autowired
    private FAccountMapper accountMapper;
    @Autowired
    private FFinanceRecordMapper financeRecordMapper;


    @Override
    public boolean insert(Map<String, String> map, String token) {
        SUser user = getUserByToken(token);
        if (StringUtils.isEmpty(map)) {
            throw new OrderException(OrderExceptionEnum.PARAMS_MISS_ERROR);
        }
        FFinanceRecord record = new FFinanceRecord();
        record.setCompId(user.getCompId());
        record.setNo(map.get("billNo"));
        record.setOperType(map.get("operType"));
        record.setPaymentMethod(map.get("paymentMethod"));
        record.setRemark(map.get("remark"));
        record.setCost(new BigDecimal(map.get("cost")));
        record.setCreatedBy(user.getId());
        record.setCreatedName(user.getUserName());
        int insert = mapper.insert(record);
        if (insert > 0) {
            log.info("财务流水信息添加成功，添加人={}", user.getUserName());
            return true;
        }
        throw new BillException(BillExceptionEnum.SYSTEM_INSERT_ERROR);
    }

    @Override
    public boolean save(FFinanceRecord financeRecord, String token){
        SUser user = getUserByToken(token);
        financeRecord.setCreatedBy(user.getId());
        financeRecord.setCreatedName(user.getName());
        int insert = mapper.insert(financeRecord);
        if(insert > 0){
            log.info("财务流水信息添加成功，添加人:{}",user.getUserName());
            return true;
        }
        throw new OrderException(OrderExceptionEnum.SYSTEM_INSERT_ERROR);
    }

    @Override
    public Page<FFinanceRecord> getFinanceRecords(Map<String, String> map,Integer pageNo, Integer size) {
        if (StringUtils.isEmpty(pageNo)) {
            throw new BillException(BillExceptionEnum.PAGENO_MISS_ERROR);
        }
        if (!StringUtils.isEmpty(map.get("size"))) {
            size = Integer.valueOf(map.get("size"));
        }
        Page<FFinanceRecord> page = new Page<>(pageNo,size);
        return financeRecordMapper.getFinanceRecords(page,map);
    }

    @Override
    public boolean payPrice(Map<String,String> map, String token) {
        SUser user = getUserByToken(token);
        if (StringUtils.isEmpty(map)) {
            throw new OrderException(OrderExceptionEnum.PARAMS_MISS_ERROR);
        }
        QueryWrapper<ROwnerProperty> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("is_delete",0);
        queryWrapper.eq("owner_id",map.get("ownerId"));
        List<ROwnerProperty> rOwnerProperties = ownerPropertyMapper.selectList(queryWrapper);
        Long compId = rOwnerProperties.get(0).getCompId();
        Long commId = rOwnerProperties.get(0).getCommId();
        BigDecimal payPrice = new BigDecimal(map.get("fukuan"));
        BigDecimal cost = new BigDecimal(map.get("cost"));
        FFinanceRecord record = new FFinanceRecord();
        record.setCompId(compId);
        record.setCommId(commId);
        record.setCreatedBy(user.getId());
        record.setCreatedName(user.getUserName());
        record.setPaymentMethod(map.get("paymentMethod"));
        record.setOwnerId(Long.parseLong(map.get("ownerId")));
        record.setRemark(map.get("remark"));

        if(!StringUtils.isEmpty(map.get("isYc"))&&!StringUtils.isEmpty(map.get("ycje"))&&map.get("isYc").equals("true")){
            record.setOperType("预存");
            record.setAccountId(Long.parseLong(map.get("accountId")));
            record.setCost(new BigDecimal(map.get("ycje")));
            mapper.insert(record);
            FAccount account = accountMapper.selectById(map.get("accountId"));
            account.setFee(account.getFee().add(payPrice));
            accountMapper.updateById(account);
        }
        record.setId(null);
        record.setOperType("支付");
        record.setCost(cost);
        mapper.insert(record);

        String[] split = map.get("payIds").split(",");
        QueryWrapper<FBill> wrapper = new QueryWrapper<>();

        wrapper.in("id",split);
        wrapper.orderByDesc("pay_end_time");
        List<FBill> fBills = billMapper.selectList(wrapper);
        List<FFinanceBillRecord> fFinanceBillRecords = new ArrayList<>();

        for (FBill bill:fBills){
            if (StringUtils.isEmpty(bill.getSalePrice())){
                bill.setSalePrice(new BigDecimal("0"));
            }
            if (StringUtils.isEmpty(bill.getOverdueCost())){
                bill.setOverdueCost(new BigDecimal("0"));
            }
            if (StringUtils.isEmpty(bill.getPayPrice())){
                bill.setPayPrice(new BigDecimal("0"));
            }
            BigDecimal payNumber = bill.getPrice().subtract(bill.getPayPrice().subtract(bill.getSalePrice()).add(bill.getOverdueCost()));
            if (payNumber.equals("0")){
                continue;
            }

            if (payPrice.compareTo(payNumber)==1||payPrice.compareTo(payNumber)==0){
                bill.setState("已支付");
                bill.setIsPayment("是");
                bill.setIsPrint("否");
                bill.setIsOverdue("否");
                bill.setIsInvoice("否");
                bill.setPayPrice(bill.getPrice());
                payPrice = payPrice.subtract(bill.getPrice());
                billMapper.updateById(bill);
            }else{
                bill.setPayPrice(payPrice.add(bill.getPayPrice()));
                payPrice = new BigDecimal("0");
                bill.setIsPayment("是");
                bill.setIsPrint("否");
                bill.setIsOverdue("否");
                bill.setIsInvoice("否");
                billMapper.updateById(bill);
                break;
            }
            FFinanceBillRecord fFinanceBillRecord = new FFinanceBillRecord();
            fFinanceBillRecord.setCreatedBy(user.getId());
            fFinanceBillRecord.setCreatedName(user.getUserName());
            fFinanceBillRecord.setCompId(compId);
            fFinanceBillRecord.setCompId(compId);
            fFinanceBillRecord.setCommId(commId);
            fFinanceBillRecord.setFinanceRecordId(record.getId());
            fFinanceBillRecord.setBillId(bill.getId());
            fFinanceBillRecord.setCost(bill.getPayPrice());
            fFinanceBillRecords.add(fFinanceBillRecord);
        }

        fFinanceBillRecordService.saveBatch(fFinanceBillRecords);
        return true;

    }

    @Override
    public List<ROwner> getOwnerByName(String ownerName, String tel, String token) {
        SUser user = getUserByToken(token);
        if (StringUtils.isEmpty(ownerName)&&StringUtils.isEmpty(tel)) {
            throw new OrderException(OrderExceptionEnum.PARAMS_MISS_ERROR);
        }
        QueryWrapper<ROwner> wrapper = new QueryWrapper<>();
        if (!StringUtils.isEmpty(ownerName)){
            wrapper.eq("name", ownerName);
        }
        if (!StringUtils.isEmpty(tel)){
            wrapper.eq("tel", tel);
        }
        wrapper.eq("is_delete", 0);
        if (user.getCompId() != 0) {
            wrapper.eq("comp_id", user.getCompId());
        }
        return ownerMapper.selectList(wrapper);
    }

    @Override
    public Page<FBill> getOwnerBill(Long ownerId, Long pageNo, Long size, String token) {
        SUser user = getUserByToken(token);
        if (StringUtils.isEmpty(ownerId)) {
            throw new OrderException(OrderExceptionEnum.PARAMS_MISS_ERROR);
        }
        List<Long> ownerPropId = mapper.getOwnerPropId(ownerId);
        QueryWrapper<FBill> wrapper = new QueryWrapper<>();
        wrapper.in("property_id", ownerPropId);
        wrapper.ne("aa.state","已付款");
        Page<FBill> page = new Page<>(pageNo, size);
        return billMapper.listBill(page, wrapper);
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
