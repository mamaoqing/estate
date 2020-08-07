package com.estate.sdzy.service;

import com.estate.sdzy.entity.RDistrict;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * <p>
 * 县区 服务类
 * </p>
 *
 * @author mq
 * @since 2020-08-04
 */
public interface RDistrictService extends IService<RDistrict> {

    List<RDistrict> districtList(Long code);
}
