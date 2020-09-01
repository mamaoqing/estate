package com.estate.sdzy.asstes.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.estate.common.entity.SUser;
import com.estate.common.exception.BillException;
import com.estate.common.util.BillExceptionEnum;
import com.estate.sdzy.asstes.entity.RCommunity;
import com.estate.sdzy.asstes.entity.ROwner;
import com.estate.sdzy.asstes.entity.ROwnerProperty;
import com.estate.sdzy.asstes.entity.RRoom;
import com.estate.sdzy.asstes.mapper.ROwnerInvoiceInfoMapper;
import com.estate.sdzy.asstes.mapper.ROwnerMapper;
import com.estate.sdzy.asstes.mapper.ROwnerPropertyMapper;
import com.estate.sdzy.asstes.service.ROwnerService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * 业主表 服务实现类
 * </p>
 *
 * @author mq
 * @since 2020-08-04
 */
@Slf4j
@Service
public class ROwnerServiceImpl extends ServiceImpl<ROwnerMapper, ROwner> implements ROwnerService {

    @Autowired
    private ROwnerMapper mapper;
    @Autowired
    private ROwnerInvoiceInfoMapper ownerInvoiceInfoMapper;
    @Autowired
    private ROwnerPropertyMapper ownerPropertyMapper;
    @Autowired
    private RedisTemplate redisTemplate;

    @Override
    public List<ROwner> getOwenerList(Map map, String token) {
        SUser user = getUserByToken(token);
        if (null == user.getCompId()) {
            throw new BillException(BillExceptionEnum.PARAMS_MISS_ERROR);
        }
        if (user.getCompId()!=0){
            map.put("compId",user.getCompId());
        }
        return mapper.getOwenerList(map);
    }

    @Override
    public boolean insert(ROwner owner, String token) {
        SUser user = getUserByToken(token);
        if (null == owner) {
            throw new BillException(BillExceptionEnum.PARAMS_MISS_ERROR);
        }
        owner.setCreatedBy(user.getId());
        owner.setCreatedName(user.getUserName());
        owner.setModifiedBy(user.getId());
        owner.setModifiedName(user.getUserName());


        int insert = mapper.insert(owner);
        if (insert > 0) {
            log.info("业主信息添加成功，添加人={}", user.getUserName());
            return true;
        }
        throw new BillException(BillExceptionEnum.SYSTEM_INSERT_ERROR);
    }

    @Override
    public boolean update(ROwner owner, String token) {
        SUser user = getUserByToken(token);
        if (null == owner) {
            throw new BillException(BillExceptionEnum.PARAMS_MISS_ERROR);
        }
        owner.setModifiedBy(user.getId());
        owner.setModifiedName(user.getUserName());
        int update = mapper.updateById(owner);
        if (update > 0) {
            log.info("业主信息修改成功，修改人={}", user.getUserName());
            return true;
        }
        throw new BillException(BillExceptionEnum.SYSTEM_UPDATE_ERROR);
    }

    @Override
    public boolean delete(Long id, String token) {
        SUser user = getUserByToken(token);
        if (null == id) {
            throw new BillException(BillExceptionEnum.PARAMS_MISS_ERROR);
        }
        int b = mapper.deleteById(id);
        QueryWrapper queryWrapper = new QueryWrapper();
        queryWrapper.eq("owner_id", id);
        ownerInvoiceInfoMapper.delete(queryWrapper);
        ownerPropertyMapper.delete(queryWrapper);
        if (b > 0) {
            log.info("业主信息删除成功，删除人={}", user.getUserName());
            return true;
        }
        throw new BillException(BillExceptionEnum.SYSTEM_DELETE_ERROR);
    }

    @Override
    public boolean deleteIds(String delIds, String token) {
        SUser user = getUserByToken(token);
        if (null == delIds) {
            throw new BillException(BillExceptionEnum.PARAMS_MISS_ERROR);
        }
        String[] split = delIds.split(",");
        List<String> ids = Arrays.asList(split);

        int b = mapper.deleteBatchIds(ids);
        for (int i =0;i<ids.size();i++){
            QueryWrapper queryWrapper = new QueryWrapper();
            queryWrapper.eq("owner_id", ids.get(i));
            ownerInvoiceInfoMapper.delete(queryWrapper);
            ownerPropertyMapper.delete(queryWrapper);
        }
        if (b > 0) {
            log.info("业主信息删除成功，删除人={}", user.getUserName());
            return true;
        }
        throw new BillException(BillExceptionEnum.SYSTEM_DELETE_ERROR);
    }

    @Override
    public ROwner getCount(ROwner owner, String token) {
        SUser user = getUserByToken(token);
        if (null == owner) {
            throw new BillException(BillExceptionEnum.PARAMS_MISS_ERROR);
        }

        QueryWrapper<ROwner> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("comp_id", owner.getCompId());
        queryWrapper.eq("owner_type", owner.getOwnerType());
        queryWrapper.eq("cert_type", owner.getCertType());
        queryWrapper.eq("cert_number", owner.getCertNumber());
        queryWrapper.orderByDesc("created_at");
        List<ROwner> rOwners = mapper.selectList(queryWrapper);
        if (rOwners.size() != 0) {
            return rOwners.get(0);
        } else {

            return null;
        }
    }

    @Override
    public List<ROwner> getOwenerByRoom(Map map, String token) {
        SUser user = getUserByToken(token);
        if (null == map) {
            throw new BillException(BillExceptionEnum.PARAMS_MISS_ERROR);
        }
        return mapper.getOwenerByRoom(map);
    }

    @Override
    public List<ROwner> getExcel(Map map, String token) {
        SUser user = getUserByToken(token);
        if (null == map) {
            throw new BillException(BillExceptionEnum.PARAMS_MISS_ERROR);
        }
        if (map.get("compId")==null||map.get("compId").equals("")){
            if (user.getCompId()!=0){
                map.put("compId",user.getCompId());
            }else{
                map.put("compId",null);
            }
        }
        return mapper.getExcel(map);
    }

    public void saveOrUpdateOwner(ROwner owner, String token){
        //保存前进行判断是否已经存在数据
        QueryWrapper<ROwner> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("comp_id",owner.getCompId());
        queryWrapper.eq("cert_type",owner.getCertType());
        queryWrapper.eq("cert_number",owner.getCertNumber());
        queryWrapper.eq("is_delete",0);
        List<ROwner> owners = mapper.selectList(queryWrapper);
        if(owners.size()==1){//执行update
            owner.setId(owners.get(0).getId());
            update(owner,token);
        }else if(owners.size()==0){//执行新增
            insert(owner,token);
        }else{
            throw new BillException(415,"导入失败，业主"+owner.getName()+"错误");
        }
    }

    @Override
    public Integer selectPageTotal(Map map, String token) {
        getUserByToken(token);
        if (null == map) {
            throw new BillException(BillExceptionEnum.PARAMS_MISS_ERROR);
        }
        return mapper.selectPageTotal(map);
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
