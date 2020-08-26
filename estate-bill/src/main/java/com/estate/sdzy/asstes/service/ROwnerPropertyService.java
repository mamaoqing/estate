package com.estate.sdzy.asstes.service;

import com.estate.sdzy.asstes.entity.ROwner;
import com.estate.sdzy.asstes.entity.ROwnerProperty;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * <p>
 * 业主与物业对应关系 服务类
 * </p>
 *
 * @author mq
 * @since 2020-08-04
 */
public interface ROwnerPropertyService extends IService<ROwnerProperty> {

    /**
     * 根据停车位id查询下面的业主信息
     * @param id
     * @return
     */
    List<ROwner> ownerProByParkId(Long id);

}