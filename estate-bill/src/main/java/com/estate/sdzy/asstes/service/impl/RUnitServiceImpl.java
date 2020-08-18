package com.estate.sdzy.asstes.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.estate.exception.BillException;
import com.estate.sdzy.asstes.entity.RUnit;
import com.estate.sdzy.asstes.mapper.RUnitMapper;
import com.estate.sdzy.asstes.service.RUnitService;
import com.estate.sdzy.system.entity.SUser;
import com.estate.util.BillExceptionEnum;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 单元 服务实现类
 * </p>
 *
 * @author mq
 * @since 2020-07-28
 */
@Service
@Slf4j
public class RUnitServiceImpl extends ServiceImpl<RUnitMapper, RUnit> implements RUnitService {

    @Autowired
    private RedisTemplate<String, Object> redisTemplate;

    @Autowired
    private RUnitMapper rUnitMapper;

    @Override
    public boolean save(RUnit rUnit, String token) {
        SUser user = getUserByToken(token);
        if (null == rUnit) {
            throw new BillException(BillExceptionEnum.PARAMS_MISS_ERROR);
        }
        rUnit.setCreatedBy(user.getId());
        rUnit.setCreatedName(user.getUserName());
        rUnit.setModifiedBy(user.getId());
        rUnit.setModifiedName(user.getUserName());
        int insert = rUnitMapper.insert(rUnit);
        if (insert > 0) {
            log.info("建筑添加成功，添加人={}", user.getUserName());
        } else {
            throw new BillException(BillExceptionEnum.SYSTEM_INSERT_ERROR);
        }
        return insert > 0;
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
