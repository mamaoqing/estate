package com.estate.sdzy.service;

import com.estate.sdzy.entity.RCity;
import com.estate.sdzy.entity.RProvince;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author mq
 * @since 2020-08-04
 */
public interface RProvinceService extends IService<RProvince> {

    /**
     * 获取全国全部的省份 和下属单位
     * @return
     */
    List<RCity> listProvinces(Long code);

    /**
     * 获取全国全部的省份
     * @return
     */
    List<RProvince> listProvince();
}
