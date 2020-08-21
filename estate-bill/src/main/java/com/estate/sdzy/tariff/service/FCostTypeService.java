package com.estate.sdzy.tariff.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.estate.sdzy.tariff.entity.FCostType;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;
import java.util.Map;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author mq
 * @since 2020-08-04
 */
public interface FCostTypeService extends IService<FCostType> {

    Page<FCostType> listCostType(Map<String,String> map);

    boolean save(FCostType costType,String token);

    boolean saveOrUpdate(FCostType costType,String token);

    boolean removeById(Long id,String token);

    List<FCostType> costTypeList();
}
