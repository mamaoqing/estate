package com.estate.sdzy.tariff.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.estate.common.entity.SUser;
import com.estate.common.exception.OrderException;
import com.estate.common.util.OrderExceptionEnum;
import com.estate.sdzy.tariff.entity.FFinanceRecord;
import com.estate.sdzy.tariff.mapper.FFinanceRecordMapper;
import com.estate.sdzy.tariff.service.FFinanceRecordService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 财务流水 服务实现类
 * </p>
 *
 * @author mq
 * @since 2020-09-08
 */
@Service
@Slf4j
public class FFinanceRecordServiceImpl extends ServiceImpl<FFinanceRecordMapper, FFinanceRecord> implements FFinanceRecordService {

    @Autowired
    FFinanceRecordMapper financeRecordMapper;

    @Autowired
    private RedisTemplate redisTemplate;

    @Override
    public boolean save(FFinanceRecord financeRecord, String token){
        SUser user = getUserByToken(token);
        financeRecord.setCreatedBy(user.getId());
        financeRecord.setCreatedName(user.getName());
        int insert = financeRecordMapper.insert(financeRecord);
        if(insert > 0){
            log.info("费用项目添加成功，添加人:{}",user.getUserName());
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
