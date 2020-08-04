package com.estate.sdzy.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.estate.sdzy.entity.RCity;
import com.estate.sdzy.entity.RDistrict;
import com.estate.sdzy.entity.RProvince;
import com.estate.sdzy.mapper.RCityMapper;
import com.estate.sdzy.mapper.RDistrictMapper;
import com.estate.sdzy.mapper.RProvinceMapper;
import com.estate.sdzy.service.RProvinceService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * <p>
 * 服务实现类
 * </p>
 *
 * @author mq
 * @since 2020-08-04
 */
@Service
public class RProvinceServiceImpl extends ServiceImpl<RProvinceMapper, RProvince> implements RProvinceService {

    @Autowired
    private RProvinceMapper provinceMapper;
    @Autowired
    private RCityMapper cityMapper;
    @Autowired
    private RDistrictMapper districtMapper;

    @Override
    public List<RProvince> listProvinces() {
        List<RProvince> rProvinces = provinceMapper.selectList(null);
        List<RProvince> provinceList = new ArrayList<>();
        for (RProvince province : rProvinces) {
            Long provinceId = province.getId();
            QueryWrapper<RCity> cityQueryWrapper = new QueryWrapper<>();
            cityQueryWrapper.eq("province_id", provinceId);
            // 市的列表
            List<RCity> rCities = cityMapper.selectList(cityQueryWrapper);
            List<RCity> cityList = new ArrayList<>();
            for (RCity city : rCities) {
                QueryWrapper<RDistrict> districtQueryWrapper = new QueryWrapper<>();
                districtQueryWrapper.eq("city_id",city.getId());
                // 区的列表
                List<RDistrict> rDistricts = districtMapper.selectList(districtQueryWrapper);

                city.setDistrictList(rDistricts);
                cityList.add(city);
            }

            province.setCityList(cityList);
            provinceList.add(province);
        }

        return provinceList;
    }
}
