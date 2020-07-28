package com.estate.sdzy.service.impl;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.estate.sdzy.entity.SUserRole;
import com.estate.sdzy.mapper.SUserRoleMapper;
import com.estate.sdzy.service.SUserRoleService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * <p>
 * 用户角色 服务实现类
 * </p>
 *
 * @author mq
 * @since 2020-07-23
 */
@Service
public class SUserRoleServiceImpl extends ServiceImpl<SUserRoleMapper, SUserRole> implements SUserRoleService {

    @Autowired
    private SUserRoleMapper userRoleMapper;


    @Override
    public List<Long> listUserRole(String token) {
        List<Long> list = new ArrayList<>();

        if(StringUtils.isEmpty(token)){
            return list;
        }

        // 1. 通过token查询用户id；
        String userId = "9";

        // 2. 通过用户id查询user_role.
        QueryWrapper<SUserRole> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("user_id", userId);

        List<SUserRole> sUserRoles = userRoleMapper.selectList(queryWrapper);

        for (int i = 0; i < sUserRoles.size(); i++) {
            list.add(sUserRoles.get(i).getId());
        }

        return list;
    }
}
