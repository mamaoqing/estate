package com.estate.sdzy.asstes.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.estate.sdzy.asstes.entity.RCommRoleAgreement;
import com.estate.sdzy.asstes.mapper.RCommRoleAgreementMapper;
import com.estate.sdzy.asstes.service.RCommRoleAgreementService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

/**
 * <p>
 * 服务实现类
 * </p>
 *
 * @author mq
 * @since 2020-07-28
 */
@Service
public class RCommRoleAgreementServiceImpl extends ServiceImpl<RCommRoleAgreementMapper, RCommRoleAgreement> implements RCommRoleAgreementService {

    @Autowired
    private RCommRoleAgreementMapper commRoleAgreementMapper;

    @Override
    public boolean isPermission(List<Long> ids) {
        boolean flag = false;
        /**
         * ids 是该用户的所有权限的id集合。
         * 如果ids中的权限没有在r_romm_role_agreement这个表中的话，表示用户没有权限访问该菜单
         * 如果用户有权限访问。则需要判断该权限的时间是否过期，过期返回false 没过期返回true
         */
        if (ids.isEmpty()) {
            return false;
        }
        //
        for (Long id : ids) {
            QueryWrapper<RCommRoleAgreement> agreementQueryWrapper = new QueryWrapper<>();
            agreementQueryWrapper.eq("role_id", id);
            RCommRoleAgreement commRoleAgreement = commRoleAgreementMapper.selectOne(agreementQueryWrapper);
            // 如果查询接口不为空，表示用户有权限，需要进一步验证权限是否过期
            if (null != commRoleAgreement) {

                Date now = new Date();
                Date beginDate = commRoleAgreement.getBeginDate();
                Date endDate = commRoleAgreement.getEndDate();

                if (beginDate.before(now) && endDate.after(now)) {
                    flag = true;
                    break;
                }
            }
        }

        return flag;
    }

    @Override
    public List<RCommRoleAgreement> getRCommRoleAgreements(String commId) {
        QueryWrapper<RCommRoleAgreement> agreementQueryWrapper = new QueryWrapper<>();
        agreementQueryWrapper.eq("comm_id", commId);
        List<RCommRoleAgreement> rCommRoleAgreements = commRoleAgreementMapper.selectList(agreementQueryWrapper);
        return rCommRoleAgreements;
    }
}
