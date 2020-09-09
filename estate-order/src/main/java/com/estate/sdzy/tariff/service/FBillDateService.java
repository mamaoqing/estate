package com.estate.sdzy.tariff.service;

import com.estate.sdzy.tariff.entity.FBillDate;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author mq
 * @since 2020-09-02
 */
public interface FBillDateService extends IService<FBillDate> {

    List<FBillDate> listBills(Long ruleId);

    boolean saveOrUpdate(FBillDate date,String token);

}
