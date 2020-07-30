package com.estate.sdzy.service.impl;

import com.estate.exception.BillException;
import com.estate.sdzy.entity.SRole;
import com.estate.sdzy.entity.SUser;
import com.estate.sdzy.mapper.SRoleMapper;
import com.estate.sdzy.service.SRoleService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.estate.util.BillExceptionEnum;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 角色表  服务实现类
 * </p>
 *
 * @author mq
 * @since 2020-07-23
 */
@Service
@Slf4j
public class SRoleServiceImpl extends ServiceImpl<SRoleMapper, SRole> implements SRoleService {

    @Autowired
    private SRoleMapper roleMapper;

    @Autowired
    private RedisTemplate<String, Object> redisTemplate;

    @Override
    public boolean saveOrUpdate(SRole role, String token) {
        SUser user = getUserByToken(token);
        if(null == role){
            throw new BillException(BillExceptionEnum.PARAMS_MISS_ERROR);
        }
        role.setModifiedBy(user.getId());
        role.setModifiedName(user.getUserName());
        int update = roleMapper.updateById(role);
        if(update > 0){
            log.info("角色信息修改成功，修改人={}",user.getUserName());
        }
        return update > 0;
    }

    @Override
    public boolean save(SRole role, String token) {
        SUser user = getUserByToken(token);

        if(null == role){
            throw new BillException(BillExceptionEnum.PARAMS_MISS_ERROR);
        }
        role.setModifiedBy(user.getId());
        role.setModifiedName(user.getUserName());

        return false;
    }
    private SUser getUserByToken(String token){
        Object o = redisTemplate.opsForValue().get(token);
        if (null == o) {
            log.error("登录失效，请重新登录。");
            throw new BillException(BillExceptionEnum.LOGIN_TIME_OUT);
        }
        return (SUser) o;
    }
}
