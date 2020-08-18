package com.estate.sdzy.asstes.service.impl;

import com.baomidou.mybatisplus.core.conditions.quer
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.estate.exception.BillException;
import com.estate.sdzy.asstes.entity.RBuilding;
import com.estate.sdzy.asstes.entity.RUnit;
import com.estate.sdzy.asstes.mapper.RUnitMapper;
import com.estate.sdzy.asstes.service.RUnitService;
import com.estate.sdzy.system.entity.SUnitModel;
import com.estate.sdzy.system.entity.SUser;
import com.estate.sdzy.system.mapper.SUnitModelMapper;
import com.estate.util.BillExceptionEnum;
import lombok.extern.slf4j.Slf4j;
import com.estate.sdzy.asstes.mapper.RBuildingMapper
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * <p>
 * 单元 服务实现类
 * </p>
 *
 * @author mq
 * @since 2020-07-28
 */
@Slf4j
@Service
public class RUnitServiceImpl extends ServiceImpl<RUnitMapper, RUnit> implements RUnitService {

    @Autowired
    private RUnitMapper mapper;
    @Autowired
    private RBuildingMapper buildingMapper;
    @Autowired
    private RedisTemplate<String, Object> redisTemplate;
    @Autowired
    private SUnitModelMapper modelMapper;

    @Override
    public List<RUnit> getAllUnit(String token) {
        SUser user = getUserByToken(token);
        if (user.getCompId()==0){
            return mapper.getAllUnit(null);
        }
        return mapper.getAllUnit(user.getId());
    }

    @Override
    public List<RBuilding> getAllBuilding(Long areaId,String token) {
        getUserByToken(token);
        QueryWrapper<RBuilding> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("comm_area_id",areaId);
        return buildingMapper.selectList(queryWrapper);
    }

    @Override
    public List<SUnitModel> getAllModel(String token) {
        getUserByToken(token);
        QueryWrapper<SUnitModel> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("is_delete",0);
        return modelMapper.selectList(queryWrapper);
    }

    private SUser getUserByToken(String token) {
        Object o = redisTemplate.opsForValue().get(token);
        if (null == o) {
            log.error("登录失效，请重新登录。");
            throw new BillException(BillExceptionEnum.LOGIN_TIME_OUT);
        }
        return (SUser) o;
    }
    @Override
    public boolean insert(RUnit unit,String token) {
        getUserByToken(token);
        SUser user = getUserByToken(token);
        if (null == unit) {
            throw new BillException(BillExceptionEnum.PARAMS_MISS_ERROR);
        }
        unit.setCreatedBy(user.getId());
        unit.setCreatedName(user.getUserName());
        int insert = mapper.insert(unit);
        if (insert > 0) {
            log.info("单元信息添加成功，创建人={}", user.getUserName());
            return true;
        }
        throw new BillException(BillExceptionEnum.SYSTEM_UPDATE_ERROR);
    }
    @Override
    public boolean save(RUnit rUnit, String token) {
        SUser user = getUserByToken(token);
        if (null == rUnit) {
            throw new BillException(BillExceptionEnum.PARAMS_MISS_ERROR);
        }
        rUnit.setCreatedBy(user.getId());
        rUnit.setCreatedName(user.getUserName());
        rUnit.setModifiedBy(user.getId());
        rUnit.setModifiedName(user.getUserName());
        int insert = rUnitMapper.insert(rUnit);
        if (insert > 0) {
            log.info("建筑添加成功，添加人={}", user.getUserName());
        } else {
            throw new BillException(BillExceptionEnum.SYSTEM_INSERT_ERROR);
        }
        return insert > 0;
    }

}
