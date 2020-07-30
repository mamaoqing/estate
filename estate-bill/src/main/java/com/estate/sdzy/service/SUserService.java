package com.estate.sdzy.service;

import com.estate.sdzy.entity.SCompany;
import com.estate.sdzy.entity.SUser;
import com.baomidou.mybatisplus.extension.service.IService;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * <p>
 * 用户表 服务类
 * </p>
 *
 * @author mq
 * @since 2020-07-23
 */
public interface SUserService extends IService<SUser> {

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
    boolean setUserRole(Long userId,String roleIds,String token);

    boolean save(SUser user ,String token);

    boolean saveOrUpdate(SUser user ,String token);

    boolean removeById(Long id,String token);
}
