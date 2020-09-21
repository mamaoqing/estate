package com.estate.sdzy.tariff.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.estate.sdzy.tariff.entity.FBillAlter;

import java.util.List;
import java.util.Map;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author mq
 * @since 2020-09-02
 */
public interface FBillAlterService extends IService<FBillAlter> {

    boolean save(FBillAlter fBillAlter, String token);
    String update(FBillAlter fBillAlter, String token);
    boolean delete(String id,String token);
    Page<FBillAlter> list(Map<String,String> map, Integer pageNo, Integer size, String token);
    List<FBillAlter> listAll(Map<String, String> parameterMap, String token);
    List<FBillAlter> getBillAlertByBillId(Long billId, String token);
}
