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
import com.estate.sdzy.asstes.entity.RParkingSpace;
import com.estate.sdzy.asstes.entity.RRoom;
import com.estate.sdzy.asstes.mapper.ROwnerMapper;
import com.estate.sdzy.asstes.mapper.ROwnerPropertyMapper;
import com.estate.sdzy.asstes.mapper.RParkingSpaceMapper;
import com.estate.sdzy.asstes.mapper.RRoomMapper;
import com.estate.sdzy.tariff.entity.FBill;
import com.estate.sdzy.tariff.entity.FBillDate;
import com.estate.sdzy.tariff.entity.FCostRule;
import com.estate.sdzy.tariff.mapper.FBillDateMapper;
import com.estate.sdzy.tariff.mapper.FBillMapper;
import com.estate.sdzy.tariff.mapper.FCostRuleMapper;
import com.estate.sdzy.tariff.service.FBillService;
import com.estate.timedtask.costrule.CrontabCostRule;
import com.estate.timedtask.costrule.excute.ExcuteSql;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.math.BigDecimal;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * 服务实现类
 * </p>
 *
 * @author mq
 * @since 2020-08-27
 */
@Service
@Slf4j
@RequiredArgsConstructor(onConstructor = @_(@Autowired))
public class FBillServiceImpl extends ServiceImpl<FBillMapper, FBill> implements FBillService {
    //com.estate.sdzy.tariff.mapper.FBillMapper
    private final FBillMapper billMapper;
    private final FCostRuleMapper costRuleMapper;
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
            if ("房产".equals(type)) {
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
            if ("停车位".equals(type)) {
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
        if (!StringUtils.isEmpty(map.get("owners"))) {
            List<Long> ownerIds = new ArrayList<>();
            String ownerName = map.get("owners");
            QueryWrapper<ROwner> ownerQueryWrapper = new QueryWrapper<>();
            ownerQueryWrapper.eq("name", ownerName);
            List<ROwner> rOwners = rOwnerMapper.selectList(ownerQueryWrapper);
            for (ROwner rOwner : rOwners) {
                ownerIds.add(rOwner.getId());
            }
            QueryWrapper<ROwnerProperty> ownerPropertyQueryWrapper = new QueryWrapper<>();
            ownerPropertyQueryWrapper.in(!ownerIds.isEmpty(), "owner_id", ownerIds);
            List<ROwnerProperty> rOwnerProperties = ownerPropertyMapper.selectList(ownerPropertyQueryWrapper);
            rOwnerProperties.forEach(res -> {
                propertyIdList.add(res.getPropertyId());
            });
        }

        if (StringUtils.isEmpty(map.get("pageNo"))) {
            log.error("参数错误，请输入页码");
            throw new OrderException(OrderExceptionEnum.PARAMS_MISS_ERROR);
        }
        SUser user = getUserByToken(token);
        QueryWrapper<FBill> queryWrapper = new QueryWrapper<>();
        queryWrapper.isNotNull("aa.id");
        if ("超级管理员".equals(user.getType())) {
            queryWrapper.eq(!StringUtils.isEmpty(map.get("compId")), "aa.comp_id", map.get("compId"));
        } else {
            queryWrapper.eq("aa.comp_id", user.getCompId());
        }
        queryWrapper.eq(!StringUtils.isEmpty(map.get("isPayment")), "is_payment", map.get("isPayment"))
                .eq(!StringUtils.isEmpty(map.get("isOverdue")), "is_overdue", map.get("isOverdue"))
                .eq(!StringUtils.isEmpty(map.get("isPrint")), "is_print", map.get("isPrint"))
                .eq(!StringUtils.isEmpty(map.get("commId")), "aa.comm_id", map.get("commId"))
                .eq(!StringUtils.isEmpty(map.get("type")), "property_type", map.get("type"))
                .eq(!StringUtils.isEmpty(map.get("costRuleId")), "cost_rule_id", map.get("costRuleId"))
                .eq(!StringUtils.isEmpty(map.get("billNo")), "bill_no", map.get("billNo"))
//                .eq(!StringUtils.isEmpty(map.get("aa.state")), "state", map.get("state"))
                .in(!rooms.isEmpty(), "property_id", rooms)
                .in(!propertyIdList.isEmpty(), "property_id", propertyIdList)

                .eq(!StringUtils.isEmpty(map.get("isInvoice")), "is_invoice", map.get("isInvoice"));

        //updateByMazhongcai 20200907
        queryWrapper.ne(!StringUtils.isEmpty(map.get("state")),"aa.state",map.get("state"));
        //updateByMazhongcai
        Integer pageNo = Integer.valueOf(map.get("pageNo"));
        Integer size = StringUtils.isEmpty(map.get("size")) ? 10 : Integer.valueOf(map.get("size"));
        Page<FBill> page = new Page<>(pageNo, size);
        return billMapper.listBill(page, queryWrapper);
    }

    @Override
    public List<FBill> listBillNoPage(Map<String, String> map, String token) {
        List<Long> rooms = new ArrayList<>();
        if (!StringUtils.isEmpty(map.get("type"))) {
            String type = map.get("type");
            if ("房产".equals(type)) {
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
            if ("停车位".equals(type)) {
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
        if (!StringUtils.isEmpty(map.get("owners"))) {
            List<Long> ownerIds = new ArrayList<>();
            String ownerName = map.get("owners");
            QueryWrapper<ROwner> ownerQueryWrapper = new QueryWrapper<>();
            ownerQueryWrapper.eq("name", ownerName);
            List<ROwner> rOwners = rOwnerMapper.selectList(ownerQueryWrapper);
            for (ROwner rOwner : rOwners) {
                ownerIds.add(rOwner.getId());
            }
            QueryWrapper<ROwnerProperty> ownerPropertyQueryWrapper = new QueryWrapper<>();
            ownerPropertyQueryWrapper.in(!ownerIds.isEmpty(), "owner_id", ownerIds);
            List<ROwnerProperty> rOwnerProperties = ownerPropertyMapper.selectList(ownerPropertyQueryWrapper);
            rOwnerProperties.forEach(res -> {
                propertyIdList.add(res.getPropertyId());
            });
        }

        if (!StringUtils.isEmpty(map.get("ownersId"))) {
            String ownerId = map.get("ownersId");
            QueryWrapper<ROwnerProperty> ownerPropertyQueryWrapper = new QueryWrapper<>();
            ownerPropertyQueryWrapper.eq(!StringUtils.isEmpty(ownerId), "owner_id", ownerId);
            List<ROwnerProperty> rOwnerProperties = ownerPropertyMapper.selectList(ownerPropertyQueryWrapper);
            rOwnerProperties.forEach(res -> {
                propertyIdList.add(res.getPropertyId());
            });

        }
        if (StringUtils.isEmpty(map.get("pageNo"))) {
            log.error("参数错误，请输入页码");
            throw new OrderException(OrderExceptionEnum.PARAMS_MISS_ERROR);
        }
        SUser user = getUserByToken(token);
        QueryWrapper<FBill> queryWrapper = new QueryWrapper<>();
        queryWrapper.isNotNull("aa.id");
        if ("超级管理员".equals(user.getType())) {
            queryWrapper.eq(!StringUtils.isEmpty(map.get("compId")), "aa.comp_id", map.get("compId"));
        } else {
            queryWrapper.eq("aa.comp_id", user.getCompId());
        }
        queryWrapper.eq(!StringUtils.isEmpty(map.get("isPayment")), "is_payment", map.get("isPayment"))
                .eq(!StringUtils.isEmpty(map.get("isOverdue")), "is_overdue", map.get("isOverdue"))
                .eq(!StringUtils.isEmpty(map.get("isPrint")), "is_print", map.get("isPrint"))
                .eq(!StringUtils.isEmpty(map.get("commId")), "aa.comm_id", map.get("commId"))
                .eq(!StringUtils.isEmpty(map.get("state")),"aa.state",map.get("state"))
                .eq(!StringUtils.isEmpty(map.get("type")), "property_type", map.get("type"))
                .eq(!StringUtils.isEmpty(map.get("costRuleId")), "cost_rule_id", map.get("costRuleId"))
                .eq(!StringUtils.isEmpty(map.get("billNo")), "bill_no", map.get("billNo"))
//                .eq(!StringUtils.isEmpty(map.get("aa.state")), "state", map.get("state"))


                .eq(!StringUtils.isEmpty(map.get("isInvoice")), "is_invoice", map.get("isInvoice"));
        if (!StringUtils.isEmpty(map.get("ownersId"))&&StringUtils.isEmpty(map.get("type"))) {
            queryWrapper.and(qw->qw.in(!rooms.isEmpty(), "property_id", rooms)
                    .in(!propertyIdList.isEmpty(), "property_id", propertyIdList).or(qw2->qw2.eq("property_type","其他").eq("property_id",map.get("ownersId"))));
        }else{
            queryWrapper.in(!rooms.isEmpty(), "property_id", rooms)
                    .in(!propertyIdList.isEmpty(), "property_id", propertyIdList);
        }
        return billMapper.listBillNoPage(queryWrapper);
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
                CrontabCostRule.execute(bill.getCostRuleId().intValue(), bill.getPropertyType(), bill.getPropertyId() + "", bill.getAccountPeriod(), true, null);
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
    public boolean addBill(FBill bill, String token) {
        SUser user = (SUser) getUserByToken(token);
        if (StringUtils.isEmpty(bill)) {
            throw new OrderException(OrderExceptionEnum.PARAMS_MISS_ERROR);
        }
        if (!StringUtils.isEmpty(bill.getPropertyType())&&bill.getPropertyType().equals("其他")){
            bill.setPropertyId(bill.getOwnerId());
        }
        bill.setCreateName("临时账单");
        bill.setAccountPeriod("临时账单");
        bill.setIsInvoice("否");
        bill.setIsOverdue("否");
        bill.setIsPayment("否");
        bill.setBillTime(new Date());
        bill.setIsPrint("否");
        int insert = billMapper.insert(bill);
        if (insert>0){
            log.info("账单信息添加成功，添加人={}", user.getUserName());
            return true;
        }
        throw new BillException(BillExceptionEnum.SYSTEM_INSERT_ERROR);
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public boolean resetBillAll(Map<String, Object> map, String token) {
        SUser user = getUserByToken(token);
        Object ruleId = map.get("ruleId");
        if (StringUtils.isEmpty(ruleId)) {
            throw new OrderException(OrderExceptionEnum.PARAMS_MISS_ERROR);
        }
        FBillDate fBillDate = billDateMapper.billDateByRuleId(Long.valueOf(ruleId.toString()));
        if (null == fBillDate) {
            throw new OrderException(500, "暂时没有需要生成的账单！");
        }
        String accountPeriod = fBillDate.getAccountPeriod();
        if (!StringUtils.isEmpty(accountPeriod)) {
            QueryWrapper<FBill> billQueryWrapper = new QueryWrapper<>();
            billQueryWrapper.eq("cost_rule_id", ruleId).eq("account_period", accountPeriod);
            Integer count = billMapper.selectCount(billQueryWrapper);
            // 如果查询结果是空的，表示还没有生成账单。可以手动生成。
//                throw new OrderException(OrderExceptionEnum.SYSTEM_UPDATE_ERROR);
            if (count > 0) {
//                QueryWrapper<FBill> queryWrapper = new QueryWrapper<>();
//                queryWrapper.eq("cost_rule_id", ruleId);
                //billMapper.delete(queryWrapper);
                String sql = "delete from f_bill where cost_rule_id = ? and (is_payment = '否' or pay_price = 0) and account_period = ?";
                String resetMeter = "update f_meter aa,f_bill bb,f_cost_rule cc set aa.bill_num =bb.begin_scale where " +
                        "aa.property_id = bb.property_id and aa.property_type=bb.property_type and bb.cost_rule_id=? and bb.account_period=? " +
                        "and aa.type = cc.billing_method and bb.cost_rule_id = cc.id and (bb.is_payment ='否' or bb.pay_price =0) and cc.billing_method in('水表','电表','煤气表')";
                Object[] reset = {ruleId, accountPeriod};
                Object[] obj = {ruleId,accountPeriod};
                try {
                    String insertSql = "insert into f_bill_copy (bill_no,property_id,property_type,bill_time,is_overdue,is_payment,overdue_cost,overdue_rule,price,pay_price,sale_price,is_print,is_invoice,pay_end_time,cost_rule_id,account_period,comp_id,comm_id,begin_scale,end_scale ,create_name,delete_time,delete_user) select bill_no,property_id,property_type,bill_time,is_overdue,is_payment,overdue_cost,overdue_rule,price,pay_price,sale_price,is_print,is_invoice,pay_end_time,cost_rule_id,account_period,comp_id,comm_id,begin_scale,end_scale,create_name,?,? from f_bill where cost_rule_id = " + ruleId + " and (is_payment = '否' or pay_price = 0) and account_period='" + accountPeriod + "'";
                    Object[] o = {new Date(), user.getUserName()};
                    String update = "update f_bill set bill_no = id";
                    ExcuteSql.executeSql(insertSql, o, null);

                    // 重新生长仪表的时候，将读数重置回生成账单之前的数据
                    ExcuteSql.executeSql(resetMeter, reset, null);

                    // 重新生成新的账单之前先将原来的账单删除
                    ExcuteSql.executeSql(sql, obj, null);

                    CrontabCostRule.execute(Integer.valueOf(ruleId.toString()), "", "", accountPeriod, true, null);

                    ExcuteSql.executeSql(update, new Object[0], null);
                } catch (Exception sqlException) {
                    sqlException.printStackTrace();
                    throw new OrderException(500, "批量重新生成账单异常");
                }
            } else {
                try {
                    CrontabCostRule.execute(Integer.valueOf(ruleId.toString()), null, null, accountPeriod, true, null);
                } catch (Exception sqlException) {
                    sqlException.printStackTrace();
                }
            }
        }
        return true;
    }

    @Override
    public List<ROwner> listOwner(String token) {
        SUser user = getUserByToken(token);
        QueryWrapper<ROwner> queryWrapper = new QueryWrapper<>();
        queryWrapper.select("DISTINCT name,id").eq("comp_id", user.getCompId());
        List<ROwner> rOwners = rOwnerMapper.selectList(queryWrapper);
        return rOwners;
    }

    @Override
    public boolean doPay(Map<String, Object> map, String token) {
        SUser user = getUserByToken(token);
        Object id = map.get("id");
        Object price = map.get("price");
        if (StringUtils.isEmpty(id) || StringUtils.isEmpty(price)) {
            throw new OrderException(OrderExceptionEnum.PARAMS_MISS_ERROR);
        }
        BigDecimal b = new BigDecimal(price.toString());
        FBill fBill = billMapper.selectById(Long.valueOf(id.toString()));
        fBill.setIsPayment("是");
        fBill.setPayPrice(b.add(fBill.getPayPrice()));
        fBill.setState("已支付");
        int i = billMapper.updateById(fBill);
        if (i > 0) {
            log.info("收款成功，收款人：{}。收款金额：", user.getUserName(), b);
            return true;
        }
        return false;
    }

    @Override
    public List<Map<String, Object>> getOwners(Map<String, Object> map) {
        List<Map<String, Object>> list = new ArrayList<>();
                Object propertyType = map.get("propertyType");
        Object propertyId = map.get("propertyId");
        if (StringUtils.isEmpty(propertyId) || StringUtils.isEmpty(propertyType)){
            throw new OrderException(OrderExceptionEnum.PARAMS_MISS_ERROR);
        }
        String type= propertyType.toString();
        if("房产".equals(type)){
            list = billMapper.listOwnerRoom(Long.valueOf(propertyId.toString()));
        }else if("停车位".equals(type)){
            list = billMapper.listOwnerPark(Long.valueOf(propertyId.toString()));
        }
        return list;
    }

    @Override
    public boolean save(FBill bill, String token) {
        SUser user = getUserByToken(token);
        if(null == bill){
            throw new OrderException(OrderExceptionEnum.PARAMS_MISS_ERROR);
        }
        Long costRuleId = bill.getCostRuleId();
        FCostRule fCostRule = costRuleMapper.selectById(costRuleId);
        BigDecimal price = fCostRule.getPrice();
        BigDecimal count = new BigDecimal(bill.getCount());
        bill.setPrice(price.multiply(count));
        bill.setCreateName(user.getUserName());
        bill.setIsOverdue("否");
        if(null==bill.getPayPrice() || "否".equals(bill.getIsPayment())){
            bill.setPayPrice(new BigDecimal(0));
        }
        bill.setOverdueRule(fCostRule.getLiquidatedDamagesMethod());
        bill.setBillTime(new Date());
        int insert = billMapper.insert(bill);
        bill.setBillNo(bill.getId()+"");
        billMapper.updateById(bill);
        if(insert > 0){
            log.info("账单信息添加成功,添加人:{}",user.getUserName());
            return true;
        }
        throw new OrderException(OrderExceptionEnum.SYSTEM_INSERT_ERROR);
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
