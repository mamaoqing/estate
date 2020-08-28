package com.estate.sdzy.system.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.estate.common.entity.SUser;
import com.estate.common.exception.BillException;
import com.estate.common.util.BillExceptionEnum;
import com.estate.sdzy.system.entity.SUnitModel;
import com.estate.sdzy.system.mapper.SUnitModelMapper;
import com.estate.sdzy.system.service.SUnitModelService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.List;
import java.util.Map;

/**
 * 单元型号表 服务实现类
 * @author mzc
 * @since 2020-08-11
 */
@Service
@Slf4j
public class SUnitModelServiceImpl extends ServiceImpl<SUnitModelMapper, SUnitModel> implements SUnitModelService {

    @Autowired
    private SUnitModelMapper sUnitModelMapper;

    @Autowired
    private RedisTemplate<String, Object> redisTemplate;

    public List<SUnitModel> listUnitModel(Map<String,String> map, Integer pageNo, Integer size, String token){
        //Page<SUnitModel> page = new Page<>();
        QueryWrapper<SUnitModel> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("is_delete",0);
        List<SUnitModel> sUnitModelPage = sUnitModelMapper.selectList(queryWrapper);
        return sUnitModelPage;
    }

    public boolean save(SUnitModel sUnitModel, String token){
        SUser user = getUserByToken(token);
        if (null == sUnitModel) {
            throw new BillException(BillExceptionEnum.PARAMS_MISS_ERROR);
        }
        sUnitModel.setCreatedBy(user.getId());
        sUnitModel.setCreatedName(user.getUserName());
        sUnitModel.setModifiedBy(user.getId());
        sUnitModel.setModifiedName(user.getUserName());
        int insert = sUnitModelMapper.insert(sUnitModel);
        if (insert > 0) {
            log.info("单元型号添加成功，添加人={}", user.getUserName());
        } else {
            throw new BillException(BillExceptionEnum.SYSTEM_INSERT_ERROR);
        }
        return insert > 0;
    }

    public boolean saveOrUpdate(SUnitModel sUnitModel,String token) {
        SUser user = getUserByToken(token);
        if (null == sUnitModel) {
            throw new BillException(BillExceptionEnum.PARAMS_MISS_ERROR);
        }
        sUnitModel.setModifiedBy(user.getId());
        sUnitModel.setModifiedName(user.getUserName());
        int update = sUnitModelMapper.updateById(sUnitModel);
        if (update > 0) {
            log.info("单元型号修改成功，修改人={}", user.getUserName());
        } else {
            throw new BillException(BillExceptionEnum.SYSTEM_UPDATE_ERROR);
        }
        return update > 0;
    }

    public boolean remove(Long id,String token) {
        SUser user = getUserByToken(token);
        if(StringUtils.isEmpty(id)){
            throw new BillException(BillExceptionEnum.PARAMS_MISS_ERROR);
        }
        SUnitModel sUnitModel = sUnitModelMapper.selectById(id);
        sUnitModel.setModifiedBy(user.getId());
        sUnitModel.setModifiedName(user.getUserName());
        sUnitModel.setIsDelete(1);
        QueryWrapper<SUnitModel> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("id",id);
        int delete = sUnitModelMapper.update(sUnitModel,queryWrapper);
        if(delete>0){
            log.info("单元型号删除成功，删除人={}",user.getUserName());
        }else{
            throw new BillException(BillExceptionEnum.SYSTEM_DELETE_ERROR);
        }
        return delete>0;
    }

    private SUser getUserByToken(String token){
        Object o = redisTemplate.opsForValue().get(token);
        if (null == o) {
            log.error("登录失效，请重新登录。");
            throw new BillException(BillExceptionEnum.LOGIN_TIME_OUT);
        }
        return (SUser) o;
    }
}
