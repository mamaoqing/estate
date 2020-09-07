package com.estate.sdzy.tariff.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.estate.common.entity.SUser;
import com.estate.common.exception.OrderException;
import com.estate.common.util.OrderExceptionEnum;
import com.estate.sdzy.tariff.entity.FBillDate;
import com.estate.sdzy.tariff.mapper.FBillDateMapper;
import com.estate.sdzy.tariff.service.FBillDateService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.List;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author mq
 * @since 2020-09-02
 */
@Service
@Slf4j
@RequiredArgsConstructor(onConstructor = @_(@Autowired))
public class FBillDateServiceImpl extends ServiceImpl<FBillDateMapper, FBillDate> implements FBillDateService {

    private final FBillDateMapper billDateMapper;
    private final RedisTemplate redisTemplate;

    @Override
    public List<FBillDate> listBills(Long ruleId) {
        if(StringUtils.isEmpty(ruleId)){
            throw new OrderException(OrderExceptionEnum.PARAMS_MISS_ERROR);
        }
        QueryWrapper<FBillDate> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("cost_rule_id",ruleId);
        return billDateMapper.selectList(queryWrapper);
    }

    @Override
    public boolean saveOrUpdate(FBillDate date, String token) {
        SUser user = getUserByToken(token);
        date.setUpdateUser(user.getUserName());
        int insert = billDateMapper.updateById(date);
        if(insert > 0){
            log.info("修改创建账单时间成功，修改人：{}",user.getUserName());
            return true;
        }
        throw new OrderException(OrderExceptionEnum.SYSTEM_UPDATE_ERROR);
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
