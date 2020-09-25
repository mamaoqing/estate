package com.estate.sdzy.tariff.service;

import com.estate.sdzy.asstes.entity.ROwner;
import com.estate.sdzy.tariff.entity.FVoucher;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;
import java.util.Map;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author mq
 * @since 2020-09-23
 */
public interface FVoucherService extends IService<FVoucher> {

    List<FVoucher> getAll(String token);
    List<ROwner> getOwners (String token,Map<String,String> map);
    Long getNo (String token);
    boolean deleteVoucher (Long id,String token);
    boolean insert(FVoucher voucher,String token);
}
