package com.estate.sdzy.tariff.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.estate.sdzy.tariff.entity.FBill;

import java.util.Map;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author mq
 * @since 2020-08-27
 */
public interface FBillService extends IService<FBill> {

    Page<FBill> listBill(Map<String,String> map,String token);

}
