package com.estate.sdzy.asstes.service;

import com.estate.sdzy.asstes.entity.ROwnerInvoiceInfo;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * <p>
 * 业主开票信息 服务类
 * </p>
 *
 * @author mq
 * @since 2020-08-04
 */
public interface ROwnerInvoiceInfoService extends IService<ROwnerInvoiceInfo> {

    List<ROwnerInvoiceInfo> getListByOwnerId(Long ownerId,String token);
    boolean add(ROwnerInvoiceInfo ownerInvoiceInfo,String token);
    boolean update(ROwnerInvoiceInfo ownerInvoiceInfo,String token);
    boolean delete(Long id,String token);
    ROwnerInvoiceInfo getInfo(ROwnerInvoiceInfo ownerInvoiceInfo,String token);
}
