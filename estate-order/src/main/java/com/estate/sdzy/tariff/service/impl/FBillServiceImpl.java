package com.estate.sdzy.tariff.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.estate.common.entity.SUser;
import com.estate.common.exception.OrderException;
import com.estate.common.util.OrderExceptionEnum;
import com.estate.sdzy.asstes.entity.RParkingSpace;
import com.estate.sdzy.asstes.entity.RRoom;
import com.estate.sdzy.asstes.mapper.RParkingSpaceMapper;
import com.estate.sdzy.asstes.mapper.RRoomMapper;
import com.estate.sdzy.tariff.entity.FBill;
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
import java.util.List;
import java.util.Map;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author mq
 * @since 2020-08-27
 */
@Service
@RequiredArgsConstructor(onConstructor = @_(@Autowired))
public class FBillServiceImpl extends ServiceImpl<FBillMapper, FBill> implements FBillService {

    private final FBillMapper billMapper;
    private final RedisTemplate redisTemplate;
    private final RRoomMapper rRoomMapper;
    private final RParkingSpaceMapper parkingSpaceMapper;

    @Override
    public Page<FBill> listBill(Map<String, String> map,String token) {
        List<Long> rooms  = new ArrayList<>();
        if(!StringUtils.isEmpty(map.get("type"))){
            String type = map.get("type");
            if("room".equals(type)){
                String no = map.get("no");
                if(!StringUtils.isEmpty(no)){
                    QueryWrapper<RRoom> queryWrapper = new QueryWrapper<>();
                    queryWrapper.eq("room_no",no);
                    List<RRoom> rRooms = rRoomMapper.selectList(queryWrapper);
                    rRooms.forEach(res->{
                        rooms.add(res.getId());
                    });
                }

            }
            if("park".equals(type)){
                String no = map.get("no");
                if(!StringUtils.isEmpty(no)){
                    QueryWrapper<RParkingSpace> queryWrapper = new QueryWrapper<>();
                    queryWrapper.eq("no",no);
                    List<RParkingSpace> rRooms = parkingSpaceMapper.selectList(queryWrapper);
                    rRooms.forEach(res->{
                        rooms.add(res.getId());
                    });
                }

            }
        }
        if (StringUtils.isEmpty(map.get("pageNo"))) {
            log.error("参数错误，请输入页码");
            throw new OrderException(OrderExceptionEnum.PARAMS_MISS_ERROR);
        }
        SUser user = getUserByToken(token);
        QueryWrapper<FBill> queryWrapper = new QueryWrapper<>();
        if("超级管理员".equals(user.getType())){
            queryWrapper.eq(!StringUtils.isEmpty(map.get("compId")),"comp_id",map.get("compId"));
        }else {
            queryWrapper.eq("comp_id",user.getCompId());
        }
        queryWrapper.eq(!StringUtils.isEmpty(map.get("isPayment")),"is_payment",map.get("isPayment"))
                .eq(!StringUtils.isEmpty(map.get("isOverdue")),"is_overdue",map.get("isOverdue"))
                .eq(!StringUtils.isEmpty(map.get("isPrint")),"is_print",map.get("isPrint"))
                .eq(!StringUtils.isEmpty(map.get("commId")),"comm_id",map.get("commId"))
                .eq(!StringUtils.isEmpty(map.get("type")),"property_type",map.get("type"))
                .in(!rooms.isEmpty(),"property_id",rooms)
                .eq(!StringUtils.isEmpty(map.get("isInvoice")),"is_invoice",map.get("isInvoice"));
        Integer pageNo = Integer.valueOf(map.get("pageNo"));
        Integer size = StringUtils.isEmpty(map.get("size")) ? 10 : Integer.valueOf(map.get("size"));
        Page<FBill> page = new Page<>(pageNo,size);
        return billMapper.selectPage(page, queryWrapper);
    }

    @Override
    @Transactional
    public boolean resetBill(Long id) {
        if(StringUtils.isEmpty(id)){
            throw new OrderException(OrderExceptionEnum.PARAMS_MISS_ERROR);
        }
        FBill bill = billMapper.selectById(id);
        int i = bill.getPayPrice().compareTo(new BigDecimal(0));
        // 判断账单是否付款，并且付款金额大于0
        if("否".equals(bill.getIsPayment()) && i<=0){
            try {
                billMapper.deleteById(id);
                CrontabCostRule.execute(bill.getCostRuleId().intValue(),bill.getPropertyType(),bill.getPropertyId()+"");
                return true;
            } catch (SQLException sqlException) {
                sqlException.printStackTrace();
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }
            throw new OrderException(OrderExceptionEnum.SYSTEM_INSERT_ERROR);
        }
        throw new OrderException(500,"该账单已经付款，不能重新生成！");
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
