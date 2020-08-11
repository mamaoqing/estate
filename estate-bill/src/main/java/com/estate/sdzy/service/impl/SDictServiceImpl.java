package com.estate.sdzy.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.estate.exception.BillException;
import com.estate.sdzy.entity.SDict;
import com.estate.sdzy.entity.SUser;
import com.estate.sdzy.mapper.SDictMapper;
import com.estate.sdzy.service.SDictService;
import com.estate.util.BillExceptionEnum;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.Map;

/**
 * <p>
 * 字典表 服务实现类
 * </p>
 *
 * @author mq
 * @since 2020-07-23
 */
@Service
@Slf4j
public class SDictServiceImpl extends ServiceImpl<SDictMapper, SDict> implements SDictService {

    @Autowired
    private SDictMapper sDictMapper;
    @Autowired
    private RedisTemplate<String, Object> redisTemplate;

    @Override
    public boolean save(SDict sDict, String token) {
        SUser user = getUserByToken(token);
        if (null == sDict) {
            throw new BillException(BillExceptionEnum.PARAMS_MISS_ERROR);
        }
        sDict.setCompId((long)0);
        sDict.setCreatedBy(user.getId());
        sDict.setCreatedName(user.getUserName());
        sDict.setModifiedBy(user.getId());
        sDict.setModifiedName(user.getUserName());
        int insert = sDictMapper.insert(sDict);
        if (insert > 0) {
            log.info("字典添加成功，添加人={}", user.getUserName());
        } else {
            throw new BillException(BillExceptionEnum.SYSTEM_INSERT_ERROR);
        }
        return insert > 0;
    }

    @Override
    public boolean update(SDict sDict, String token) {
        SUser user = getUserByToken(token);
        if (null == sDict) {
            throw new BillException(BillExceptionEnum.PARAMS_MISS_ERROR);
        }
        sDict.setModifiedBy(user.getId());
        sDict.setModifiedName(user.getUserName());
        int update = sDictMapper.updateById(sDict);
        if (update > 0) {
            log.info("字典修改成功，修改人={}", user.getUserName());
        } else {
            throw new BillException(BillExceptionEnum.SYSTEM_UPDATE_ERROR);
        }
        return update > 0;
    }

    @Override
    public boolean removeById(Long id, String token) {
        SUser user = getUserByToken(token);
        if(StringUtils.isEmpty(id)){
            throw new BillException(BillExceptionEnum.PARAMS_MISS_ERROR);
        }
        int delete = sDictMapper.deleteById(id);
        if(delete>0){
            log.info("字典删除成功，删除人={}",user.getUserName());
        }else{
            throw new BillException(BillExceptionEnum.SYSTEM_DELETE_ERROR);
        }
        return delete>0;
    }

    @Override
    public Page<SDict> listDict(Map<String, String> map, Integer pageNo, Integer size) {
        if(StringUtils.isEmpty(pageNo)){
            throw new BillException(BillExceptionEnum.PARAMS_MISS_ERROR);
        }
        if(StringUtils.isEmpty(size)){
            size = 10;
        }
        Page<SDict> page = new Page<>(pageNo,size);
        QueryWrapper<SDict> queryWrapper = new QueryWrapper<>();
        // 下面放查询条件
        // 名称查询
        if(!StringUtils.isEmpty(map.get("name"))){
            queryWrapper.like("name",map.get("name"));
        }
        //queryWrapper.eq("state","在用");
        queryWrapper.orderByDesc("id");
        Page<SDict> sSDictPage = sDictMapper.selectPage(page, queryWrapper);
        return sSDictPage;
    }

    @Override
    public boolean checkDictName(String name, String token) {
        Page<SDict> page = new Page<>();
        QueryWrapper<SDict> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("name",name);
        Page<SDict> sSDictPage = sDictMapper.selectPage(page, queryWrapper);
        return sSDictPage.getRecords().size()>0;
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
