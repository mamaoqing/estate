package com.estate.sdzy.system.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.estate.common.entity.SUser;
import com.estate.common.exception.BillException;
import com.estate.common.util.BillExceptionEnum;
import com.estate.sdzy.system.entity.SBank;
import com.estate.sdzy.system.mapper.SBankMapper;
import com.estate.sdzy.system.service.SBankService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * <p>
 * 银行 服务实现类
 * </p>
 *
 * @author mq
 * @since 2020-08-04
 */
@Service
public class SBankServiceImpl extends ServiceImpl<SBankMapper, SBank> implements SBankService {

    @Autowired
    private RedisTemplate<String, Object> redisTemplate;
    @Autowired
    private SBankMapper mapper;

    public List<SBank> getAllBank(String token) {
        getUserByToken(token);
        QueryWrapper<SBank> wrapper = new QueryWrapper<>();
        wrapper.eq("is_delete", 0);
        return mapper.selectList(wrapper);
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
