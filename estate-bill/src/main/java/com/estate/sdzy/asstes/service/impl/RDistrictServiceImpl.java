package com.estate.sdzy.asstes.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.estate.sdzy.asstes.entity.RDistrict;
import com.estate.sdzy.asstes.mapper.RCityMapper;
import com.estate.sdzy.asstes.mapper.RDistrictMapper;
import com.estate.sdzy.asstes.service.RDistrictService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * <p>
 * 县区 服务实现类
 * </p>
 *
 * @author mq
 * @since 2020-08-04
 */
@Service
public class RDistrictServiceImpl extends ServiceImpl<RDistrictMapper, RDistrict> implements RDistrictService {

    @Autowired
    private RDistrictMapper districtMapper;
    @Autowired
    private RCityMapper cityMapper;

    @Override
    public List<RDistrict> districtList(Long code) {
        QueryWrapper<RDistrict> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("city_id",code);
        return districtMapper.selectList(queryWrapper);
    }
}
