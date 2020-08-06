package com.estate.sdzy.service;

import com.estate.sdzy.entity.SCompany;
import com.estate.sdzy.entity.SOrg;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * <p>
 * 机构表 服务类
 * </p>
 *
 * @author mq
 * @since 2020-07-23
 */
public interface SOrgService extends IService<SOrg> {

    /**
     * 自动添加组织机构
     * @param company
     * @return
     */
    boolean autoSave(SCompany company);

    List<SOrg> listOrg(String token);

    boolean save(SOrg org,String token);
    boolean saveOrUpdate(SOrg org,String token);
    boolean removeById(Long id,String token);



}
