package com.estate.sdzy.tariff.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.estate.common.exception.OrderException;
import com.estate.common.util.OrderExceptionEnum;
import com.estate.sdzy.system.entity.SUser;
import com.estate.sdzy.tariff.entity.FCostType;
import com.estate.sdzy.tariff.mapper.FCostTypeMapper;
import com.estate.sdzy.tariff.service.FCostTypeService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.List;
import java.util.Map;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author mq
 * @since 2020-08-04
 */
@Service
@Slf4j
@RequiredArgsConstructor(onConstructor = @_(@Autowired))
public class FCostTypeServiceImpl extends ServiceImpl<FCostTypeMapper, FCostType> implements FCostTypeService {
    private final FCostTypeMapper costTypeMapper;
    private final RedisTemplate<String,Object> redisTemplate;

    @Override
    public Page<FCostType> listCostType(Map<String, String> map) {
        if (StringUtils.isEmpty(map.get("pageNo"))) {
            log.error("参数错误，请输入页码");
            throw new OrderException(OrderExceptionEnum.PARAMS_MISS_ERROR);
        }
        Integer pageNo = Integer.valueOf(map.get("pageNo"));
        Integer size = StringUtils.isEmpty(map.get("size")) ? 10 : Integer.valueOf(map.get("size"));
        QueryWrapper<FCostType> queryWrapper = new QueryWrapper<>();
        Page<FCostType> page = new Page<>(pageNo,size);

        return costTypeMapper.selectPage(page,queryWrapper);
    }

    @Override
    public boolean removeById(Long id,String token) {
        SUser user = getUserByToken(token);
        int i = costTypeMapper.deleteById(id);
        if(i>0){
            log.info("物业费类型删除成功，删除人:{}",user.getUserName());
            return true;
        }
        log.error("物业费类型删除失败");
        throw new OrderException(OrderExceptionEnum.SYSTEM_DELETE_ERROR);
    }

    @Override
    public List<FCostType> costTypeList() {
        QueryWrapper<FCostType> queryWrapper = new QueryWrapper<>();
        queryWrapper.select("id","type_no","type_name");
        return costTypeMapper.selectList(queryWrapper);
    }

    @Override
    public boolean save(FCostType entity,String token) {
        SUser user = getUserByToken(token);
        entity.setCreatedBy(user.getId());
        entity.setCreatedName(user.getUserName());
        int insert = costTypeMapper.insert(entity);
        if(insert > 0){
            log.info("物业费类型添加成功，添加人:{}",user.getUserName());
            return true;
        }
        log.error("物业费类型添加失败");
        throw new OrderException(OrderExceptionEnum.SYSTEM_INSERT_ERROR);
    }

    @Override
    public boolean saveOrUpdate(FCostType entity,String token) {
        SUser user = getUserByToken(token);
        entity.setModifiedBy(user.getId());
        entity.setModifiedName(user.getUserName());
        int update = costTypeMapper.updateById(entity);
        if(update > 0){
            log.info("物业费类型更新成功，修改人:{}",user.getUserName());
            return true;
        }
        log.error("物业费类型修改失败");
        throw new OrderException(OrderExceptionEnum.SYSTEM_UPDATE_ERROR);
    }

    private SUser getUserByToken(String token) {
        Object o = redisTemplate.opsForValue().get(token);
        if (null == o) {
            log.error("登录失效，请重新登录。");
            throw new OrderException(OrderExceptionEnum.LOGIN_TIME_OUT);
        }
        return (SUser) o;
    }
}
