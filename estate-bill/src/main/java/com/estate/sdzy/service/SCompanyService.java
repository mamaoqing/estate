package com.estate.sdzy.service;

import com.estate.sdzy.entity.SCompany;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * <p>
 * 物业公司表 服务类
 * </p>
 *
 * @author mq
 * @since 2020-07-23
 */
public interface SCompanyService extends IService<SCompany> {

    boolean save(SCompany company,String token);

    boolean saveOrUpdate(SCompany company,String token);

    boolean removeById(Long id ,String token);
}
