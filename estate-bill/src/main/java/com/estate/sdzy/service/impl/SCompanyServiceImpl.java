package com.estate.sdzy.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.estate.exception.BillException;
import com.estate.sdzy.entity.SCompany;
import com.estate.sdzy.entity.SUser;
import com.estate.sdzy.mapper.SCompanyMapper;
import com.estate.sdzy.service.SCompanyService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.estate.util.BillExceptionEnum;
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
        company.setCreatedBy(user.getId());
        company.setCreatedName(user.getUserName());
        int insert = companyMapper.updateById(company);
        if (insert > 0) {
            log.info("公司修改成功，修改人={}", user.getUserName());
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
        if(StringUtils.isEmpty(pageNo)){
            throw new BillException(BillExceptionEnum.PARAMS_MISS_ERROR);
        }
        if(StringUtils.isEmpty(size)){
            size = 10;
        }
        Page<SCompany> page = new Page<>(pageNo,size);
        QueryWrapper<SCompany> queryWrapper = new QueryWrapper<>();
        // 下面放查询条件

        Page<SCompany> sCompanyPage = companyMapper.selectPage(page, queryWrapper);
        return sCompanyPage;
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
