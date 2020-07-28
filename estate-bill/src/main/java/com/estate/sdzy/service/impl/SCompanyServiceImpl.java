package com.estate.sdzy.service.impl;

import com.estate.sdzy.entity.SCompany;
import com.estate.sdzy.entity.SUser;
import com.estate.sdzy.mapper.SCompanyMapper;
import com.estate.sdzy.service.SCompanyService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

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
        if (null == user) { return false; }
        company.setCreatedBy(user.getId());
        company.setCreatedName(user.getUserName());
        int insert = companyMapper.insert(company);
        if (insert>0){
            log.info("公司添加成功，添加人={}",user.getUserName());
        }
        return insert > 0;
    }

    @Override
    public boolean saveOrUpdate(SCompany company, String token) {
        SUser user = getUserByToken(token);
        if (null == user) { return false; }
        company.setCreatedBy(user.getId());
        company.setCreatedName(user.getUserName());
        int insert = companyMapper.updateById(company);
        if (insert>0){
            log.info("公司修改成功，修改人={}",user.getUserName());
        }
        return insert>0;
    }

    @Override
    public boolean removeById(Long id, String token) {
        SUser user = getUserByToken(token);
        if (null == user) { return false; }
        int delete = companyMapper.deleteById(id);
        if (delete > 0){
            log.info("公司删除成功，删除人={}",user.getUserName());
        }
        return delete > 0;
    }

    private SUser getUserByToken(String token){
        Object o = redisTemplate.opsForValue().get(token);
        if (null == o) {
            log.error("登录失效，请重新登录。");
            return null;
        }
        return (SUser) o;
    }
}
