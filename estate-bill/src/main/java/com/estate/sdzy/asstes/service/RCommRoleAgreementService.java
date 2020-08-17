package com.estate.sdzy.asstes.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.estate.sdzy.asstes.entity.RCommRoleAgreement;

import java.util.List;
import java.util.Map;

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

    Boolean save(RCommRoleAgreement rCommRoleAgreement ,String token);
    Boolean saveOrUpdate(RCommRoleAgreement rCommRoleAgreement ,String token);
    Boolean removeById(Long id ,String token);

    Page<RCommRoleAgreement> listCommRoleAgreement(Map<String,String> map , String token);
}
