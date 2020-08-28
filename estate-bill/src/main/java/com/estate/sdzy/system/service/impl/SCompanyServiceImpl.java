package com.estate.sdzy.system.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.estate.common.exception.BillException;
import com.estate.common.util.BillExceptionEnum;
import com.estate.sdzy.system.entity.SCompany;
import com.estate.sdzy.system.entity.SUser;
import com.estate.sdzy.system.mapper.SCompanyMapper;
import com.estate.sdzy.system.service.SCompanyService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.List;
import java.util.Map;

/**
 * <p>
 * 物业公司表 服务实现类
 * </p>
 *
 * @author mq
 * @since 2020-07-23
 */
@Service
@Slf4j
public class SCompanyServiceImpl extends ServiceImpl<SCompanyMapper, SCompany> implements SCompanyService {

    @Autowired
    private SCompanyMapper companyMapper;
    @Autowired
    private RedisTemplate<String, Object> redisTemplate;

    @Override
    public boolean save(SCompany company, String token) {
        SUser user = getUserByToken(token);
        if (null == company) {
            throw new BillException(BillExceptionEnum.PARAMS_MISS_ERROR);
        }
        company.setCreatedBy(user.getId());
        company.setCreatedName(user.getUserName());
        company.setModifiedBy(user.getId());
        company.setModifiedName(user.getUserName());
        int insert = companyMapper.insert(company);
        if (insert > 0) {
            log.info("公司添加成功，添加人={}", user.getUserName());
        } else {
            throw new BillException(BillExceptionEnum.SYSTEM_INSERT_ERROR);
        }
        return insert > 0;
    }

    @Override
    public boolean saveOrUpdate(SCompany company, String token) {
        SUser user = getUserByToken(token);
        if (null == company) {
            throw new BillException(BillExceptionEnum.PARAMS_MISS_ERROR);
        }
        company.setModifiedBy(user.getId());
        company.setModifiedName(user.getUserName());
        int insert = companyMapper.updateById(company);
        if (insert > 0) {
            log.info("{} 修改成功，修改人={}", company.getName(), user.getUserName());
        } else {
            throw new BillException(BillExceptionEnum.SYSTEM_UPDATE_ERROR);
        }
        return insert > 0;
    }

    @Override
    public boolean removeById(Long id, String token) {
        SUser user = getUserByToken(token);
        if (StringUtils.isEmpty(id)) {
            throw new BillException(BillExceptionEnum.PARAMS_MISS_ERROR);
        }
        int delete = companyMapper.deleteById(id);
        if (delete > 0) {
            log.info("公司删除成功，删除人={}", user.getUserName());
        } else {
            throw new BillException(BillExceptionEnum.SYSTEM_DELETE_ERROR);
        }
        return delete > 0;
    }

    @Override
    public Page<SCompany> listCompany(Map<String, String> map, Integer pageNo, Integer size) {
        if (StringUtils.isEmpty(pageNo)) {
            throw new BillException(BillExceptionEnum.PAGENO_MISS_ERROR);
        }
        if (StringUtils.isEmpty(size)) {
            size = 10;
        }
        Page<SCompany> page = new Page<>(pageNo, size);
        QueryWrapper<SCompany> queryWrapper = new QueryWrapper<>();
        // 下面放查询条件
        // 名称查询
        queryWrapper.like(!StringUtils.isEmpty(map.get("name")), "name", map.get("name"));
        // 简称查询
        queryWrapper.like(!StringUtils.isEmpty(map.get("abbreviation")), "abbreviation", map.get("abbreviation"));
//        // 省
//        queryWrapper.eq(!StringUtils.isEmpty(map.get("province")), "province", map.get("province"));
//        // 市
//        queryWrapper.eq(!StringUtils.isEmpty(map.get("city")), "city", map.get("city"));
//        // 区
//        queryWrapper.eq(!StringUtils.isEmpty(map.get("district")), "district", map.get("district"));

        // 省
        queryWrapper.eq(!StringUtils.isEmpty(map.get("provinceId")), "province_id", map.get("provinceId"));
        // 市
        queryWrapper.eq(!StringUtils.isEmpty(map.get("cityId")), "city_id", map.get("cityId"));
        // 区
        queryWrapper.eq(!StringUtils.isEmpty(map.get("districtId")), "district_id", map.get("districtId"));


        Page<SCompany> sCompanyPage = companyMapper.selectPage(page, queryWrapper);
        return sCompanyPage;
    }

    @Override
    public Page<SCompany> getListCompany(Map<String, String> map, String token) {
        SUser use = getUserByToken(token);
        Page<SCompany> page = new Page<>();
        QueryWrapper<SCompany> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq(!StringUtils.isEmpty(map.get("district")), "district", map.get("district"));
        if(!(use.getCompId()==0)){
            queryWrapper.eq("id",use.getCompId());
        }
        Page<SCompany> sCompanyPage = companyMapper.selectPage(page, queryWrapper);
        return sCompanyPage;
    }

    @Override
    public List<SCompany> getComp(String token) {
        SUser user = getUserByToken(token);

        QueryWrapper<SCompany> queryWrapper = new QueryWrapper<>();
        log.info("当前角色为->{}", user.getType());
        if (!"超级管理员".equals(user.getType())) {
            queryWrapper.eq("id", user.getCompId());
        }
        queryWrapper.select("name", "id");
        return companyMapper.selectList(queryWrapper);
    }

    private SUser getUserByToken(String token) {
        Object o = redisTemplate.opsForValue().get(token);
        if (null == o) {
            log.error("登录失效，请重新登录。");
            throw new BillException(BillExceptionEnum.LOGIN_TIME_OUT);
        }
        return (SUser) o;
    }
}
