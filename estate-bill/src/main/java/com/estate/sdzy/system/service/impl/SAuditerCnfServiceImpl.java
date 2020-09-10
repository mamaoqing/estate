package com.estate.sdzy.system.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.estate.common.entity.SUser;
import com.estate.common.exception.BillException;
import com.estate.common.util.BillExceptionEnum;
import com.estate.sdzy.system.entity.SAuditerCnf;
import com.estate.sdzy.system.mapper.SAuditerCnfMapper;
import com.estate.sdzy.system.service.SAuditerCnfService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.List;
import java.util.Map;


@Service
@Slf4j
public class SAuditerCnfServiceImpl extends ServiceImpl<SAuditerCnfMapper, SAuditerCnf> implements SAuditerCnfService {

    @Autowired
    private RedisTemplate<String, Object> redisTemplate;

    @Autowired
    private SAuditerCnfMapper sAuditerCnfMapper;

    @Override
    public boolean save(SAuditerCnf sAuditerCnf, String token) {
        SUser user = getUserByToken(token);
        if (null == sAuditerCnf) {
            throw new BillException(BillExceptionEnum.PARAMS_MISS_ERROR);
        }
        sAuditerCnf.setCreatedBy(user.getId());
        sAuditerCnf.setCreatedName(user.getUserName());
        sAuditerCnf.setModifiedBy(user.getId());
        sAuditerCnf.setModifiedName(user.getUserName());
        int insert = sAuditerCnfMapper.insert(sAuditerCnf);
        if (insert > 0) {
            log.info("审核人添加成功，添加人={}", user.getUserName());
        } else {
            throw new BillException(BillExceptionEnum.SYSTEM_INSERT_ERROR);
        }
        return insert > 0;
    }

    @Override
    public boolean update(SAuditerCnf sAuditerCnf, String token) {
        SUser user = getUserByToken(token);
        if (null == sAuditerCnf) {
            throw new BillException(BillExceptionEnum.PARAMS_MISS_ERROR);
        }
        sAuditerCnf.setModifiedBy(user.getId());
        sAuditerCnf.setModifiedName(user.getUserName());
        int update = sAuditerCnfMapper.updateById(sAuditerCnf);
        if (update > 0) {
            log.info("审核人修改成功，修改人={}", user.getUserName());
        } else {
            throw new BillException(BillExceptionEnum.SYSTEM_UPDATE_ERROR);
        }
        return update > 0;
    }

    @Override
    public boolean delete(String id, String token) {
        SUser user = getUserByToken(token);
        if (null == id) {
            throw new BillException(BillExceptionEnum.PARAMS_MISS_ERROR);
        }
        int delete = sAuditerCnfMapper.deleteById(id);
        if (delete > 0) {
            log.info("审核人删除成功，删除人={}", user.getUserName());
            return true;
        }
        throw new BillException(BillExceptionEnum.SYSTEM_UPDATE_ERROR);
    }

    @Override
    public List<SAuditerCnf> list(Map<String, String> map, Integer pageNo, Integer size, String token) {
        SUser user = getUserByToken(token);
        if(user.getCompId()==0){
            List<SAuditerCnf> listAuditerCnf = sAuditerCnfMapper.getListAuditerCnf(map,(pageNo-1)*size,size,null);
            return listAuditerCnf;
        }else{
            List<SAuditerCnf> listAuditerCnf = sAuditerCnfMapper.getListAuditerCnf(map,(pageNo-1)*size,size,user.getId());
            return listAuditerCnf;
        }
    }

    @Override
    public List<SAuditerCnf> listAll(Map<String, String> map,  String token) {
        SUser user = getUserByToken(token);
        if(user.getCompId()==0){
            List<SAuditerCnf> listAuditerCnf = sAuditerCnfMapper.getListAuditerCnf(map,null,null,null);
            return listAuditerCnf;
        }else{
            List<SAuditerCnf> listAuditerCnf = sAuditerCnfMapper.getListAuditerCnf(map,null,null,user.getId());
            return listAuditerCnf;
        }
    }

    @Override
    public Integer listNum(Map<String, String> map, String token) {
        SUser user = getUserByToken(token);
        if(user.getCompId()==0){
            Integer listAuditerCnfNum = sAuditerCnfMapper.getListAuditerCnfNum(map,null,null,null);
            return listAuditerCnfNum;
        }else{
            Integer listAuditerCnfNum = sAuditerCnfMapper.getListAuditerCnfNum(map,null,null,user.getId());
            return listAuditerCnfNum;
        }
    }

    @Override
    public String checkSAuditerCnf(SAuditerCnf sAuditerCnf) {
        QueryWrapper<SAuditerCnf> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("comm_id",sAuditerCnf.getCommId());
        queryWrapper.eq("user_id",sAuditerCnf.getUserId());
        queryWrapper.eq("type",sAuditerCnf.getType());
        if(!StringUtils.isEmpty(sAuditerCnf.getId())){
            queryWrapper.ne("id",sAuditerCnf.getId());
        }
        List<SAuditerCnf> sAuditerCnfs = sAuditerCnfMapper.selectList(queryWrapper);
        if(sAuditerCnfs.size()>0){
            return "审核人重复，请重新选择";
        }else{
            return "";
        }
    }

    public SUser getUserByToken(String token) {
        Object o = redisTemplate.opsForValue().get(token);
        if (null == o) {
            log.error("登录失效，请重新登录。");
            throw new BillException(BillExceptionEnum.LOGIN_TIME_OUT);
        }
        return (SUser) o;
    }
}
