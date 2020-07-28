package com.estate.sdzy.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.estate.sdzy.entity.SUser;
import com.estate.sdzy.entity.SUserComm;
import com.estate.sdzy.mapper.SUserCommMapper;
import com.estate.sdzy.service.SUserCommService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * <p>
 * 用户社区表 服务实现类
 * </p>
 *
 * @author mq
 * @since 2020-07-24
 */
@Service
@Slf4j
public class SUserCommServiceImpl extends ServiceImpl<SUserCommMapper, SUserComm> implements SUserCommService {

    @Autowired
    private SUserCommMapper userCommMapper;

    @Autowired
    private RedisTemplate<String, Object> redisTemplate;

    @Override
    public List<Long> getUserCommIdList(String token) {
        List<Long> list = new ArrayList<>();
        SUser user = getUserByToken(token);
        //if (null == user) { return list; }
        QueryWrapper<SUserComm> query = new QueryWrapper<>();
//        query.eq("user_id",9);
        query.eq("user_id",user.getId());
        List<SUserComm> sUserComms = userCommMapper.selectList(query);

        for (SUserComm x : sUserComms){
            list.add(x.getCommId());
        }

        return list;
    }


    private SUser getUserByToken(String token) {
        Object o = redisTemplate.opsForValue().get(token);
        if (null == o) {
            log.error("登录失效，请重新登录。");
            return null;
        }
        return (SUser) o;
    }

}
