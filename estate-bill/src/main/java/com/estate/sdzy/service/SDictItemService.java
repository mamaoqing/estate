package com.estate.sdzy.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.estate.sdzy.entity.SDictItem;

import java.util.List;
import java.util.Map;

/**
 * <p>
 * 字典表 服务类
 * </p>
 *
 * @author mq
 * @since 2020-07-23
 */
public interface SDictItemService extends IService<SDictItem> {
    boolean save(SDictItem sDictItem, String token);
    boolean update(SDictItem sDictItem,String token);
    String removeById(Long id,String token);
    List<SDictItem> listDictItem(Map<String,String> map, Integer pageNo, Integer size,String token);
    List<SDictItem> findDictItemList(Map<String,String> map, Integer pageNo, Integer size,String token);
    boolean checkDictItemName(String dictId,String name,String token);
}
