package com.estate.sdzy.tariff.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.estate.sdzy.asstes.entity.ROwner;
import com.estate.sdzy.tariff.entity.FBill;

import java.util.List;
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
    List<FBill> listBillNoPage(Map<String,String> map,String token);

    boolean resetBill(Long id);
    boolean addBill(FBill bill,String token);
    boolean resetBillAll(Map<String,Object> mqp,String token);

    List<ROwner> listOwner(String token);

    boolean doPay(Map<String,Object> map ,String token);

    List<Map<String,Object>> getOwners(Map<String,Object> map);

    boolean save(FBill bill,String token);

}
