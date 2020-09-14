package com.estate.sdzy.tariff.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.estate.common.entity.SUser;
import com.estate.common.exception.OrderException;
import com.estate.common.util.OrderExceptionEnum;
import com.estate.sdzy.tariff.entity.FAccountCostItem;
import com.estate.sdzy.tariff.mapper.FAccountCostItemMapper;
import com.estate.sdzy.tariff.service.FAccountCostItemService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 账户消费标准 服务实现类
 * </p>
 *
 * @author mzc
 * @since 2020-09-10
 */
@Service
@Slf4j
public class FAccountCostItemServiceImpl extends ServiceImpl<FAccountCostItemMapper, FAccountCostItem> implements FAccountCostItemService {

    @Autowired
    private FAccountCostItemMapper accountCostItemMapper;

    @Autowired
    private RedisTemplate redisTemplate;

    @Override
    public boolean save(FAccountCostItem item, String token) {
        SUser user = getUserByToken(token);
        item.setCreatedBy(user.getId());
        item.setCreatedName(user.getName());
        int insert = accountCostItemMapper.insert(item);
        if(insert > 0){
            log.info("账单消费标准添加成功，添加人:{}",user.getUserName());
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
