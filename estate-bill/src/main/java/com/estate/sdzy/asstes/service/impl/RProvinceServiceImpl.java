package com.estate.sdzy.asstes.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.estate.exception.BillException;
import com.estate.sdzy.asstes.entity.RCity;
import com.estate.sdzy.asstes.entity.RDistrict;
import com.estate.sdzy.asstes.entity.RProvince;
import com.estate.sdzy.asstes.mapper.RCityMapper;
import com.estate.sdzy.asstes.mapper.RDistrictMapper;
import com.estate.sdzy.asstes.mapper.RProvinceMapper;
import com.estate.sdzy.asstes.service.RProvinceService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.estate.util.BillExceptionEnum;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

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
@RequiredArgsConstructor(onConstructor = @_(@Autowired))
public class RProvinceServiceImpl extends ServiceImpl<RProvinceMapper, RProvince> implements RProvinceService {

    private final RProvinceMapper provinceMapper;
    private final RCityMapper cityMapper;
    private final RDistrictMapper districtMapper;

    @Override//id
    public List<RCity> listProvinces(Long code) {
        QueryWrapper<RProvince> queryWrapper = new QueryWrapper();
        queryWrapper.eq("id", code);
        RProvince province = provinceMapper.selectOne(queryWrapper);
        Long provinceId = province.getId();
        QueryWrapper<RCity> cityQueryWrapper = new QueryWrapper<>();
        cityQueryWrapper.eq("province_id", provinceId);
        // 市的列表
        List<RCity> rCities = cityMapper.selectList(cityQueryWrapper);
        List<RCity> cityList = new ArrayList<>();
        for (RCity city : rCities) {
            QueryWrapper<RDistrict> districtQueryWrapper = new QueryWrapper<>();
            districtQueryWrapper.eq("city_id", city.getId());
            // 区的列表
            List<RDistrict> rDistricts = districtMapper.selectList(districtQueryWrapper);

            city.setChildList(rDistricts);
            cityList.add(city);
        }

        province.setChildList(cityList);

        return cityList;
    }

    @Override
    public List<RProvince> listProvince() {
        return provinceMapper.selectList(null);
    }

    @Override
    public List<RProvince> getProvinceChild() {
        List<RProvince> provinceList = listProvince();
        List<RProvince> result = new ArrayList<>();
        for (RProvince province : provinceList){
            province.setName(province.getProvinceName());
            Long provinceId = province.getId();
            QueryWrapper<RCity> cityQueryWrapper = new QueryWrapper<>();
            cityQueryWrapper.eq("province_id", provinceId);
            // 市的列表
            List<RCity> rCities = cityMapper.selectList(cityQueryWrapper);
            List<RCity> cityList = new ArrayList<>();
            for (RCity city : rCities) {
                city.setName(city.getCityName());
                QueryWrapper<RDistrict> districtQueryWrapper = new QueryWrapper<>();
                districtQueryWrapper.eq("city_id", city.getId());
                // 区的列表
                List<RDistrict> rDistricts = districtMapper.selectList(districtQueryWrapper);
                List<RDistrict> districtList = new ArrayList<>();
                for (RDistrict district :rDistricts){
                    district.setName(district.getDistrictName());
                    districtList.add(district);
                }

                city.setChildList(districtList);
                cityList.add(city);
            }

            province.setChildList(cityList);
            result.add(province);
        }

        return result;
    }

    @Override
    public List<RCity> getCityList(Long id) {
        if (StringUtils.isEmpty(id)) {
            throw new BillException(BillExceptionEnum.PARAMS_MISS_ERROR);
        }
        QueryWrapper<RCity> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("province_id",id);

        return cityMapper.selectList(queryWrapper);
    }

    @Override
    public List<RDistrict> getDistList(Long id) {
        if (StringUtils.isEmpty(id)) {
            throw new BillException(BillExceptionEnum.PARAMS_MISS_ERROR);
        }
        QueryWrapper<RDistrict> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("city_id",id);
        return districtMapper.selectList(queryWrapper);
    }
}
