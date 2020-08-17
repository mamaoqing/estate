package com.estate.sdzy.system.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.estate.sdzy.system.entity.SCompany;
import com.estate.sdzy.system.entity.SUser;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;
import java.util.Map;

/**
 * <p>
 * 用户表 服务类
 * </p>
 *
 * @author mq
 * @since 2020-07-23
 */
public interface SUserService extends IService<SUser> {

    Page<SUser> listUser(String token, Map<String,String> map);

    List<SUser> findOne(Integer id);
    SUser findByUserName(String username);

    /**
     * 自动添加公司管理员角色
     * @param company 公司信息
     * @return
     */
    Boolean autoSave(SCompany company);

    /**
     * 设置用户角色，用户与角色是一对多的关系，一个用户可以有多个角色
     *
     * @param userId 用户di
     * @param roleIds 角色id的string串，中间用“,”隔开
     * @return
     */
    boolean setUserRole(Long userId,Long compId,String roleIds,String token);

    boolean save(SUser user ,String token);

    boolean saveOrUpdate(SUser user ,String token);

    boolean removeById(Long id,String token);

    /**
     * 重设密码
     * @param password 新密码
     * @param id id
     * @param token 登录凭证
     * @param oldPassword 旧密码
     * @return
     */
    boolean reSetPassword(String password,Long id,String token,String oldPassword);

    boolean reSetPassword(String password,String token,Long id);
}
