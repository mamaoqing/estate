package com.estate.sdzy.tariff.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.estate.common.entity.SUser;
import com.estate.common.exception.OrderException;
import com.estate.common.util.OrderExceptionEnum;
import com.estate.sdzy.asstes.entity.ROwner;
import com.estate.sdzy.asstes.entity.ROwnerProperty;
import com.estate.sdzy.asstes.entity.RParkingSpace;
import com.estate.sdzy.asstes.entity.RRoom;
import com.estate.sdzy.asstes.mapper.ROwnerMapper;
import com.estate.sdzy.asstes.mapper.ROwnerPropertyMapper;
import com.estate.sdzy.asstes.mapper.RParkingSpaceMapper;
import com.estate.sdzy.asstes.mapper.RRoomMapper;
import com.estate.sdzy.tariff.entity.FBill;
import com.estate.sdzy.tariff.entity.FBillDate;
import com.estate.sdzy.tariff.mapper.FBillDateMapper;
import com.estate.sdzy.tariff.mapper.FBillMapper;
import com.estate.sdzy.tariff.service.FBillService;
import com.estate.timedtask.costrule.CrontabCostRule;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.math.BigDecimal;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicReference;

/**
 * <p>
 * 服务实现类
 * </p>
 *
 * @author mq
 * @since 2020-08-27
 */
@Service
@RequiredArgsConstructor(onConstructor = @_(@Autowired))
public class FBillServiceImpl extends ServiceImpl<FBillMapper, FBill> implements FBillService {
//com.estate.sdzy.tariff.mapper.FBillMapper
    private final FBillMapper billMapper;
    private final RedisTemplate redisTemplate;
    private final RRoomMapper rRoomMapper;
    private final RParkingSpaceMapper parkingSpaceMapper;
    private final FBillDateMapper billDateMapper;
    private final ROwnerMapper rOwnerMapper;
    private final ROwnerPropertyMapper ownerPropertyMapper;

    @Override
    public Page<FBill> listBill(Map<String, String> map, String token) {
        List<Long> rooms = new ArrayList<>();
        if (!StringUtils.isEmpty(map.get("type"))) {
            String type = map.get("type");
            if ("room".equals(type)) {
                String no = map.get("no");
                if (!StringUtils.isEmpty(no)) {
                    QueryWrapper<RRoom> queryWrapper = new QueryWrapper<>();
                    queryWrapper.eq("room_no", no);
                    List<RRoom> rRooms = rRoomMapper.selectList(queryWrapper);
                    rRooms.forEach(res -> {
                        rooms.add(res.getId());
                    });
                }

            }
            if ("park".equals(type)) {
                String no = map.get("no");
                if (!StringUtils.isEmpty(no)) {
                    QueryWrapper<RParkingSpace> queryWrapper = new QueryWrapper<>();
                    queryWrapper.eq("no", no);
                    List<RParkingSpace> rRooms = parkingSpaceMapper.selectList(queryWrapper);
                    rRooms.forEach(res -> {
                        rooms.add(res.getId());
                    });
                }

            }
        }
        List<Long> propertyIdList = new ArrayList<>();
        if(!StringUtils.isEmpty(map.get("owners"))){
        List<Long> ownerIds = new ArrayList<>();
            String ownerName = map.get("owners");
            QueryWrapper<ROwner> ownerQueryWrapper = new QueryWrapper<>();
            ownerQueryWrapper.eq("name",ownerName);
            List<ROwner> rOwners = rOwnerMapper.selectList(ownerQueryWrapper);
            for (ROwner rOwner : rOwners) {
                ownerIds.add(rOwner.getId());
            }
            QueryWrapper<ROwnerProperty> ownerPropertyQueryWrapper = new QueryWrapper<>();
            ownerPropertyQueryWrapper.in(!ownerIds.isEmpty(), "owner_id", ownerIds);
            List<ROwnerProperty> rOwnerProperties = ownerPropertyMapper.selectList(ownerPropertyQueryWrapper);
            rOwnerProperties.forEach(res->{
                propertyIdList.add(res.getPropertyId());
            });
        }
        if (StringUtils.isEmpty(map.get("pageNo"))) {
            log.error("参数错误，请输入页码");
            throw new OrderException(OrderExceptionEnum.PARAMS_MISS_ERROR);
        }
        SUser user = getUserByToken(token);
        QueryWrapper<FBill> queryWrapper = new QueryWrapper<>();
        if ("超级管理员".equals(user.getType())) {
            queryWrapper.eq(!StringUtils.isEmpty(map.get("aa.compId")), "comp_id", map.get("compId"));
        } else {
            queryWrapper.eq("aa.comp_id", user.getCompId());
        }
        queryWrapper.eq(!StringUtils.isEmpty(map.get("isPayment")), "is_payment", map.get("isPayment"))
                .eq(!StringUtils.isEmpty(map.get("isOverdue")), "is_overdue", map.get("isOverdue"))
                .eq(!StringUtils.isEmpty(map.get("isPrint")), "is_print", map.get("isPrint"))
                .eq(!StringUtils.isEmpty(map.get("commId")), "aa.comm_id", map.get("commId"))
                .eq(!StringUtils.isEmpty(map.get("type")), "property_type", map.get("type"))
                .eq(!StringUtils.isEmpty(map.get("costRuleId")), "cost_rule_id", map.get("costRuleId"))
                .in(!rooms.isEmpty(), "property_id", rooms)
                .in(!propertyIdList.isEmpty(), "property_id", propertyIdList)
                .eq(!StringUtils.isEmpty(map.get("isInvoice")), "is_invoice", map.get("isInvoice"));
        Integer pageNo = Integer.valueOf(map.get("pageNo"));
        Integer size = StringUtils.isEmpty(map.get("size")) ? 10 : Integer.valueOf(map.get("size"));
        Page<FBill> page = new Page<>(pageNo, size);
        return billMapper.listBill(page,queryWrapper);
//        return billMapper.selectPage(page, queryWrapper);
    }

    @Override
    @Transactional
    public boolean resetBill(Long id) {
        if (StringUtils.isEmpty(id)) {
            throw new OrderException(OrderExceptionEnum.PARAMS_MISS_ERROR);
        }
        FBill bill = billMapper.selectById(id);
        int i = bill.getPayPrice().compareTo(new BigDecimal(0));
        // 判断账单是否付款，并且付款金额大于0
        if ("否".equals(bill.getIsPayment()) && i <= 0) {
            try {
                billMapper.deleteById(id);
                CrontabCostRule.execute(bill.getCostRuleId().intValue(), bill.getPropertyType(), bill.getPropertyId() + "", bill.getAccountPeriod());
                return true;
            } catch (SQLException sqlException) {
                sqlException.printStackTrace();
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }
            throw new OrderException(OrderExceptionEnum.SYSTEM_INSERT_ERROR);
        }
        throw new OrderException(500, "该账单已经付款，不能重新生成！");
    }

    @Override
    @Transactional
    public boolean resetBillAll(Map<String, Object> map) {
        Object ruleId = map.get("ruleId");
        if (StringUtils.isEmpty(ruleId)) {
            throw new OrderException(OrderExceptionEnum.PARAMS_MISS_ERROR);
        }
        FBillDate fBillDate = billDateMapper.billDateByRuleId(Long.valueOf(ruleId.toString()));
        String accountPeriod = fBillDate.getAccountPeriod();
        if (!StringUtils.isEmpty(accountPeriod)) {
            QueryWrapper<FBill> billQueryWrapper = new QueryWrapper<>();
            billQueryWrapper.eq("cost_rule_id", ruleId).eq("account_period", accountPeriod);
            List<FBill> fBills = billMapper.selectList(billQueryWrapper);
            StringBuilder sb = new StringBuilder("");
            // 如果查询结果是空的，表示还没有生成账单。可以手动生成。
           if(!fBills.isEmpty()){
               for (FBill res : fBills) {
                   String types = "";
                   // 区分车位房产等
                   if ("room".equals(res.getPropertyType())) {
                       types = "room";
                   }
                   if ("park".equals(res.getPropertyType())) {
                       types = "park";
                   }
                   if ("an".equals(res.getPropertyType())) {
                       types = "an";
                   }
                   if ("rq".equals(res.getPropertyType())) {
                       types = "rq";
                   }
                   if ("water".equals(res.getPropertyType())) {
                       types = "water";
                   }
                   int i = res.getPayPrice().compareTo(new BigDecimal(0));
                   if ("否".equals(res.getIsPayment()) && i <= 0) {
                       // 符合条件的先执行删除，在重新生成。
                       sb.append(res.getId()).append(",");
                       if (!StringUtils.isEmpty(res.getPropertyId())) {
                           try {
                               billMapper.deleteById(res.getId());
                               CrontabCostRule.execute(Integer.valueOf(ruleId.toString()), types, res.getPropertyId() + "", res.getAccountPeriod());
                               return true;
                           } catch (Exception sqlException) {
                               throw new OrderException(500,"批量重新生成账单异常");
                           }
                       }
                   }
               }
           }else{
               try {
                   CrontabCostRule.execute(Integer.valueOf(ruleId.toString()), null, null,accountPeriod);
                   return true;
               } catch (SQLException sqlException) {
                   sqlException.printStackTrace();
               } catch (ClassNotFoundException e) {
                   e.printStackTrace();
               }
           }
        }
        return false;
    }

    @Override
    public List<ROwner> listOwner(String token) {
        SUser user = getUserByToken(token);
        QueryWrapper<ROwner> queryWrapper = new QueryWrapper<>();
        queryWrapper.select("DISTINCT name").eq("comp_id",user.getCompId());
        List<ROwner> rOwners = rOwnerMapper.selectList(queryWrapper);
        return rOwners;
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
