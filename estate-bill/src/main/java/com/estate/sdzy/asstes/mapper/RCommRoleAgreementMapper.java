package com.estate.sdzy.asstes.mapper;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.estate.sdzy.asstes.entity.RCommRoleAgreement;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author mq
 * @since 2020-07-28
 */
public interface RCommRoleAgreementMapper extends BaseMapper<RCommRoleAgreement> {

    Page<RCommRoleAgreement> listCommRoleAgreement(Page<RCommRoleAgreement> page,@Param("ew") QueryWrapper<RCommRoleAgreement> queryWrapper);
}
