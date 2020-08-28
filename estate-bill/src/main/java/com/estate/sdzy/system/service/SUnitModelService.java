package com.estate.sdzy.system.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.estate.common.exception.BillException;
import com.estate.sdzy.system.entity.SUnitModel;

import java.util.List;
import java.util.Map;

/**
 * 单元型号表 服务类
 * @author mzc
 * @since 2020-08-11
 */
public interface SUnitModelService extends IService<SUnitModel> {
    List<SUnitModel> listUnitModel(Map<String,String> map, Integer pageNo, Integer size, String token);
    boolean saveOrUpdate(SUnitModel sUnitModel, String token) throws BillException;
    boolean save(SUnitModel sUnitModel,String token) throws BillException;
    boolean remove(Long id,String token) throws BillException;
}
