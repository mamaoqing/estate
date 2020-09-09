package com.estate.sdzy.tariff.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.estate.common.entity.SUser;
import com.estate.common.exception.BillException;
import com.estate.common.util.BillExceptionEnum;
import com.estate.sdzy.tariff.entity.FMeter;
import com.estate.sdzy.tariff.mapper.FMeterMapper;
import com.estate.sdzy.tariff.service.FMeterService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * 仪表表 服务实现类
 * </p>
 *
 * @author mq
 * @since 2020-08-04
 */
@Service
@Slf4j
public class FMeterServiceImpl extends ServiceImpl<FMeterMapper, FMeter> implements FMeterService {

    @Autowired
    private RedisTemplate<String, Object> redisTemplate;

    @Autowired
    private FMeterMapper fMeterMapper;

    @Override
    public String save(FMeter fMeter, String token) {
        QueryWrapper<FMeter> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("comm_id",fMeter.getCommId());
        queryWrapper.eq("no",fMeter.getNo());//仪表编号
        queryWrapper.eq("is_delete",0);
        List<FMeter> meters = fMeterMapper.selectList(queryWrapper);
        if(meters.size()>0){
            return "仪表编号重复";
        }else{
            SUser user = getUserByToken(token);
            if (null == fMeter) {
                throw new BillException(BillExceptionEnum.PARAMS_MISS_ERROR);
            }
            if(StringUtils.isEmpty(fMeter.getNewNum())){
                fMeter.setNewNum(new BigDecimal("0"));
            }
            if(StringUtils.isEmpty(fMeter.getBillNum())){
                fMeter.setBillNum(new BigDecimal("0"));
            }
            fMeter.setCreatedBy(user.getId());
            fMeter.setCreatedName(user.getUserName());
            fMeter.setModifiedBy(user.getId());
            fMeter.setModifiedName(user.getUserName());
            int insert = fMeterMapper.insert(fMeter);
            if (insert > 0) {
                log.info("仪表添加成功，添加人={}", user.getUserName());
            } else {
                throw new BillException(BillExceptionEnum.SYSTEM_INSERT_ERROR);
            }
            return "仪表添加成功";
        }
    }

    @Override
    public void saveOrUpdateMeter(FMeter fMeter, String token) {
        //保存前进行判断是否已经存在数据
        QueryWrapper<FMeter> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("comm_id",fMeter.getCommId());//社区
        queryWrapper.eq("property_type",fMeter.getPropertyType());//物业类型
        queryWrapper.eq("property_id",fMeter.getPropertyId());//物业id
        queryWrapper.eq("type",fMeter.getType());//仪表类型
        queryWrapper.eq("no",fMeter.getNo());//仪表编号
        queryWrapper.eq("is_delete",0);
        List<FMeter> meters = fMeterMapper.selectList(queryWrapper);
        if(meters.size()==1){//执行update
            if(!updateMeter(fMeter,token,meters.get(0).getId())){
                throw new BillException(415,"导入失败");
            }
        }else if(meters.size()==0){//执行新增
            save(fMeter,token);
        }else{
            throw new BillException(415,"导入失败，物业编号"+fMeter.getPropertyId()+"错误");
        }
    }

    @Override
    public String checkMeterNo(FMeter fMeter) {
        if(!StringUtils.isEmpty(fMeter.getId())){
            fMeter = fMeterMapper.selectById(fMeter.getId());
        }
        QueryWrapper<FMeter> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("comm_id",fMeter.getCommId());//社区
        /*queryWrapper.eq("property_type",fMeter.getPropertyType());//物业类型
        queryWrapper.eq("property_id",fMeter.getPropertyId());//物业id
        queryWrapper.eq("type",fMeter.getType());//仪表类型*/
        queryWrapper.eq("no",fMeter.getNo());//仪表编号
        queryWrapper.eq("is_delete",0);
        List<FMeter> meters = fMeterMapper.selectList(queryWrapper);
        if(meters.size()>0){
            return "仪表编号重复";
        }else{
            return null;
        }
    }

    @Override
    public String getPropertyName(Long id) {
        return fMeterMapper.getPropertyName(id);
    }

    public boolean updateMeter(FMeter fMeterUpload, String token, Long id) {
        SUser user = getUserByToken(token);
        Integer update = fMeterMapper.updateMeter(fMeterUpload.getState(), fMeterUpload.getRemark(), user.getId(),
                user.getUserName(), id);
        if (update > 0) {
            log.info("仪表修改成功，修改人={}", user.getUserName());
        } else {
            throw new BillException(BillExceptionEnum.SYSTEM_UPDATE_ERROR);
        }
        return update > 0;
    }

    @Override
    public String update(FMeter fMeter, String token) {
        QueryWrapper<FMeter> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("comm_id",fMeter.getCommId());
        queryWrapper.eq("no",fMeter.getNo());//仪表编号
        queryWrapper.eq("is_delete",0);
        List<FMeter> meters = fMeterMapper.selectList(queryWrapper);
        if(meters.size()>0){
            return "仪表编号重复";
        }else{
            SUser user = getUserByToken(token);
            if (null == fMeter) {
                throw new BillException(BillExceptionEnum.PARAMS_MISS_ERROR);
            }
            fMeter.setModifiedBy(user.getId());
            fMeter.setModifiedName(user.getUserName());
            int update = fMeterMapper.updateById(fMeter);
            if (update > 0) {
                log.info("仪表修改成功，修改人={}", user.getUserName());
            } else {
                //throw new BillException(BillExceptionEnum.SYSTEM_UPDATE_ERROR);
                return "修改数据系统异常";
            }
            return "仪表修改成功";
        }
    }

    @Override
    public boolean delete(String id, String token) {
        SUser user = getUserByToken(token);
        if (null == id) {
            throw new BillException(BillExceptionEnum.PARAMS_MISS_ERROR);
        }
        FMeter fMeter = fMeterMapper.selectById(id);
        fMeter.setModifiedBy(user.getId());
        fMeter.setModifiedName(user.getUserName());
        fMeter.setIsDelete(1);
        QueryWrapper<FMeter> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("id",id);
        int delete = fMeterMapper.update(fMeter,queryWrapper);
        if (delete > 0) {
            log.info("仪表信息删除成功，删除人={}", user.getUserName());
            return true;
        }
        throw new BillException(BillExceptionEnum.SYSTEM_UPDATE_ERROR);
    }

    @Override
    public List<FMeter> list(Map<String, String> map, Integer pageNo, Integer size, String token) {
        if(StringUtils.isEmpty(pageNo)){
            throw new BillException(BillExceptionEnum.PARAMS_MISS_ERROR);
        }
        if(StringUtils.isEmpty(size)){
            size = 10;
        }
        SUser user = getUserByToken(token);
        if(user.getCompId()==0){
            List<FMeter> listMeter = fMeterMapper.getListMeter(map.get("ownerName"),map.get("compName"),map.get("commName"),map.get("commAreaName"),map.get("propertyType"),map.get("propertyName"),
                    map.get("type"),map.get("no") ,(pageNo-1)*size,size,null);
            return listMeter;
        }else{
            List<FMeter> listMeter = fMeterMapper.getListMeter(map.get("ownerName"),map.get("compName"),map.get("commName"),map.get("commAreaName"),map.get("propertyType"),map.get("propertyName"),
                    map.get("type"),map.get("no") ,(pageNo-1)*size,size,user.getId());
            return listMeter;
        }
    }

    @Override
    public Integer listNum(Map<String, String> map, String token) {
        SUser user = getUserByToken(token);
        if(user.getCompId()==0){
            Integer list = fMeterMapper.getListMeterNum(map.get("ownerName"),map.get("compName"),map.get("commName"),map.get("commAreaName"),map.get("propertyType"),map.get("propertyName"),
                    map.get("type"),map.get("no") ,null,null,null);
            return list;
        }else{
            Integer list = fMeterMapper.getListMeterNum(map.get("ownerName"),map.get("compName"),map.get("commName"),map.get("commAreaName"),map.get("propertyType"),map.get("propertyName"),
                    map.get("type"),map.get("no") ,null,null,user.getId());
            return list;
        }
    }

    @Override
    public List<FMeter> listAll(Map<String, String> map, String token) {
        SUser user = getUserByToken(token);
        if(user.getCompId()==0){
            List<FMeter> listMeter = fMeterMapper.getListMeter(map.get("ownerName"),map.get("compName"),map.get("commName"),map.get("commAreaName"),map.get("propertyType"),map.get("propertyName"),
                    map.get("type"),map.get("no") ,null,null,null);
            return listMeter;
        }else{
            List<FMeter> listMeter = fMeterMapper.getListMeter(map.get("ownerName"),map.get("compName"),map.get("commName"),map.get("commAreaName"),map.get("propertyType"),map.get("propertyName"),
                    map.get("type"),map.get("no") ,null,null,user.getId());
            return listMeter;
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
