package com.estate.sdzy.service;

import com.estate.sdzy.entity.SUserRole;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;
import java.util.Map;

/**
 * <p>
 * 用户角色 服务类
 * </p>
 *
 * @author mq
 * @since 2020-07-23
 */
public interface SUserRoleService extends IService<SUserRole> {

    /**
     * 通过用户token查询用户的权限
     * @param token 前端传递的登录凭证
     * @return 返回一个用户权限id的集合
     */
    List<Long> listUserRole(String token);

    List<Map<String,String>> listUserRole(Long userId,Long compId);

}
