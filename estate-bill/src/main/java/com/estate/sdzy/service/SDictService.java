package com.estate.sdzy.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.estate.sdzy.entity.SDict;

import java.util.Map;

/**
 * <p>
 * 字典表 服务类
 * </p>
 *
 * @author mq
 * @since 2020-07-23
 */
public interface SDictService extends IService<SDict> {

    boolean save(SDict sDict,String token);
    boolean update(SDict sDict,String token);
    boolean removeById(Long id,String token);
    Page<SDict> listDict(Map<String,String> map, Integer pageNo, Integer size);
    boolean checkDictName(String name,String token);
}
