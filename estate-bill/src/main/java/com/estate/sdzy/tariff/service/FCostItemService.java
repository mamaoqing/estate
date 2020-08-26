package com.estate.sdzy.tariff.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.estate.sdzy.tariff.entity.FCostItem;
import com.baomidou.mybatisplus.extension.service.IService;
import com.estate.sdzy.tariff.entity.FCostType;

import java.util.List;
import java.util.Map;

/**
 * <p>
 * 费用项目 服务类
 * </p>
 *
 * @author mq
 * @since 2020-08-04
 */
public interface FCostItemService extends IService<FCostItem> {

    Page<FCostItem> listCostItem(Map<String,String> map,String token);

    boolean save(FCostItem item,String token);

    boolean saveOrUpdate(FCostItem item,String token);

    boolean removeById(Long id ,String token);

    List<FCostType> costTypeList();

    List<FCostItem> costItemList(Long compId);
}
