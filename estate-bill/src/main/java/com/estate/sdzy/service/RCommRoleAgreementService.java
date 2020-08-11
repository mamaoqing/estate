package com.estate.sdzy.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.estate.sdzy.entity.RCommRoleAgreement;

import java.util.List;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author mq
 * @since 2020-07-28
 */
public interface RCommRoleAgreementService extends IService<RCommRoleAgreement> {

    /**
     * 判断用户是否有权限
     * @param ids 角色id的一个集合
     * @return 有权限返回true 否则返回false
     */
    boolean isPermission(List<Long> ids);

    List<RCommRoleAgreement> getRCommRoleAgreements(String commId);
}
