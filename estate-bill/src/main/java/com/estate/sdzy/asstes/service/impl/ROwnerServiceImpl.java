package com.estate.sdzy.asstes.service.impl;

import com.estate.exception.BillException;
import com.estate.sdzy.asstes.entity.ROwner;
import com.estate.sdzy.asstes.mapper.ROwnerMapper;
import com.estate.sdzy.asstes.service.ROwnerService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.estate.sdzy.system.entity.SUser;
import com.estate.util.BillExceptionEnum;
import com.estate.util.RedisUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

/**
 * <p>
 * 业主表 服务实现类
 * </p>
 *
 * @author mq
 * @since 2020-08-04
 */
@Service
public class ROwnerServiceImpl extends ServiceImpl<ROwnerMapper, ROwner> implements ROwnerService {

    @Autowired
    private ROwnerMapper mapper;
    @Autowired
    private RedisTemplate redisTemplate;
    @Override
    public List<ROwner> getOwenerList(Map map,String token) {
        SUser user = getUserByToken(token);
        if (null == user.getCompId()) {
            throw new BillException(BillExceptionEnum.PARAMS_MISS_ERROR);
        }
        return mapper.getOwenerList(map);
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
