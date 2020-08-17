package com.estate.sdzy.system.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.estate.exception.BillException;
import com.estate.sdzy.asstes.entity.RCommunity;
import com.estate.sdzy.system.entity.SUser;
import com.estate.sdzy.system.entity.SUserComm;
import com.estate.sdzy.system.mapper.SUserCommMapper;
import com.estate.sdzy.system.service.SUserCommService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.estate.util.BillExceptionEnum;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

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

        QueryWrapper<SUserComm> query = new QueryWrapper<>();
//        query.eq("user_id",9);
        query.eq("user_id", user.getId());
        List<SUserComm> sUserComms = userCommMapper.listCommUser(user.getId());

        for (SUserComm x : sUserComms) {
            list.add(x.getCommId());
        }

        return list;
    }

    @Override
    @Transactional
    public boolean setUserComm(Long userId, String commIds, String token, String remark) {
        SUser user = getUserByToken(token);
        if (StringUtils.isEmpty(userId) || StringUtils.isEmpty(commIds)) {
            throw new BillException(BillExceptionEnum.PARAMS_MISS_ERROR);
        }
        // 在更新权限之前。首先将之前存在的权限删除。如果之前没有权限，则直接创建。否则就需要先删除之前的权限在操作
        // 如果存在之前的权限，但是删除失败了直接抛出异常。
        QueryWrapper<SUserComm> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("user_id", userId);
        List<SUserComm> sUserComms = userCommMapper.selectList(queryWrapper);
        boolean flag = true;
        if (!sUserComms.isEmpty()) {
            int delete = userCommMapper.delete(queryWrapper);
            flag = delete > 0 ? true : false;
        }
        if (flag) {
            String[] commIdArr = commIds.split(",");
            for (String commId : commIdArr) {
                SUserComm userComm = new SUserComm(user.getCompId(), userId, Long.valueOf(commId), remark, user.getId(), user.getUserName());
                int count = userCommMapper.insert(userComm);
                if (!(count > 0)) {
                    throw new BillException(BillExceptionEnum.SET_USER_COMM_ERROR);
                }
            }
        } else {
            throw new BillException(BillExceptionEnum.SET_USER_COMM_ERROR);
        }

        return true;
    }

    @Override
    public List<Map<String, String>> listUserComm(Long compId,Long id) {
        if (StringUtils.isEmpty(id)) {
            throw new BillException(BillExceptionEnum.PARAMS_MISS_ERROR);
        }
        return userCommMapper.listUserComm(id,compId);
    }

    @Override
    public List<RCommunity> getUserComm(Long userId, Long compId) {
        return userCommMapper.getUserComm(userId,compId);
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
