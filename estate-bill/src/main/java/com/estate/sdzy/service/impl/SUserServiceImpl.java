package com.estate.sdzy.service.impl;

import com.estate.sdzy.entity.SUser;
import com.estate.sdzy.mapper.SUserMapper;
import com.estate.sdzy.service.SUserService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * <p>
 * 用户表 服务实现类
 * </p>
 *
 * @author mq
 * @since 2020-07-23
 */
@Service
public class SUserServiceImpl extends ServiceImpl<SUserMapper, SUser> implements SUserService {

    @Autowired
    private SUserMapper userMapper;


    @Override
    @Transactional
    public List<SUser> findOne(Integer id) {
        return userMapper.findOne(id);
    }

    @Override
    public SUser findByUserName(String username) {
        return userMapper.findByUserName(username);
    }
}
