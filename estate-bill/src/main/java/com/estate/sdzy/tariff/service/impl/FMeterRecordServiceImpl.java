package com.estate.sdzy.tariff.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.estate.common.entity.SUser;
import com.estate.common.exception.BillException;
import com.estate.common.util.BillExceptionEnum;
import com.estate.sdzy.tariff.entity.FMeter;
import com.estate.sdzy.tariff.entity.FMeterRecord;
import com.estate.sdzy.tariff.mapper.FMeterMapper;
import com.estate.sdzy.tariff.mapper.FMeterRecordMapper;
import com.estate.sdzy.tariff.service.FMeterRecordService;
import com.estate.sdzy.tariff.service.FMeterService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * 仪表流水表 服务实现类
 * </p>
 *
 * @author mq
 * @since 2020-08-04
 */
@Service
@Slf4j
public class FMeterRecordServiceImpl extends ServiceImpl<FMeterRecordMapper, FMeterRecord> implements FMeterRecordService {

    @Autowired
    private RedisTemplate<String, Object> redisTemplate;

    @Autowired
    private FMeterRecordMapper fMeterRecordMapper;

    @Autowired
    private FMeterService fMeterService;

    @Autowired
    private FMeterMapper fMeterMapper;

    @Override
    @Transactional
    public String save(FMeterRecord fMeterRecord, String token) {
        SUser user = getUserByToken(token);
        if (null == fMeterRecord) {
            throw new BillException(BillExceptionEnum.PARAMS_MISS_ERROR);
        }
        fMeterRecord.setOperType("抄表");
        fMeterRecord.setCreatedBy(user.getId());
        fMeterRecord.setCreatedName(user.getUserName());
        fMeterRecord.setModifiedBy(user.getId());
        fMeterRecord.setModifiedName(user.getUserName());
        int insert = fMeterRecordMapper.insert(fMeterRecord);
        if (insert > 0) {
            //同时修改仪表表中抄表刻度和抄表时间（系统时间）
            FMeter fMeter = fMeterService.getById(fMeterRecord.getMeterId());
            fMeter.setNewNum(fMeterRecord.getNewNum());
            fMeter.setMeterReadTime(fMeterRecord.getModifiedAt());
            //fMeterService.update(fMeter,token);
            fMeter.setModifiedBy(user.getId());
            fMeter.setModifiedName(user.getUserName());
            int update = fMeterMapper.updateById(fMeter);
            if (update > 0) {
                log.info("仪表修改成功，修改人={}", user.getUserName());
            }else{
                throw new BillException(BillExceptionEnum.SYSTEM_UPDATE_ERROR);
            }
            log.info("仪表抄表添加成功，添加人={}", user.getUserName());
        } else {
            //throw new BillException(BillExceptionEnum.SYSTEM_INSERT_ERROR);
            return "添加数据系统异常";
        }
        return "仪表抄表添加成功";
    }

    @Override
    public String saveByMeterId(Long meterId, BigDecimal newNum, String token) {
        FMeterRecord fMeterRecord = new FMeterRecord();
        fMeterRecord.setMeterId(meterId);
        fMeterRecord.setNewNum(newNum);
        FMeter fMeter = fMeterMapper.selectById(fMeterRecord.getMeterId());
        fMeterRecord.setCompId(fMeter.getCompId());
        fMeterRecord.setCommId(fMeter.getCommId());
        fMeterRecord.setPropertyType(fMeter.getPropertyType());
        fMeterRecord.setPropertyId(fMeter.getPropertyId());
        fMeterRecord.setType(fMeter.getType());
        if(fMeterRecord.getNewNum().compareTo(fMeter.getBillNum())==1||fMeterRecord.getNewNum().compareTo(fMeter.getBillNum())==0){
            return save(fMeterRecord,token);
        }else{
            return "抄表刻度小于仪表的账单刻度";
        }
    }

    @Override
    public void saveOrIgnoreMeter(FMeterRecord fMeterRecord, String token) {
        List<Long> meterIds = fMeterMapper.getMeterByNo(fMeterRecord.getNo(), fMeterRecord.getCommId());
        if(meterIds.size()==1){
            FMeter fMeter = fMeterMapper.selectById(meterIds.get(0));
            //判断抄表刻度表里的抄表刻度大于等于仪表表里的账单刻度
            if(fMeterRecord.getNewNum().compareTo(fMeter.getBillNum())==1||fMeterRecord.getNewNum().compareTo(fMeter.getBillNum())==0){
                fMeterRecord.setMeterId(meterIds.get(0));
                fMeterRecord.setOperType("抄表");
                fMeterRecord.setPropertyType(fMeter.getPropertyType());
                fMeterRecord.setPropertyId(fMeter.getPropertyId());
                fMeterRecord.setName(fMeter.getName());
                save(fMeterRecord,token);
            }
        }
    }

    @Override
    public String update(FMeterRecord fMeterRecord, String token) {
        FMeter fMeter = fMeterMapper.selectById(fMeterRecord.getMeterId());
        //判断抄表刻度表里的抄表刻度大于等于仪表表里的账单刻度
        if(fMeterRecord.getNewNum().compareTo(fMeter.getBillNum())==1||fMeterRecord.getNewNum().compareTo(fMeter.getBillNum())==0){
            SUser user = getUserByToken(token);
            if (null == fMeterRecord) {
                throw new BillException(BillExceptionEnum.PARAMS_MISS_ERROR);
            }
            fMeterRecord.setModifiedBy(user.getId());
            fMeterRecord.setModifiedName(user.getUserName());
            int update = fMeterRecordMapper.updateById(fMeterRecord);
            if (update > 0) {
                log.info("仪表抄表修改成功，修改人={}", user.getUserName());
                //同时修改仪表表中抄表刻度和抄表时间（系统时间）
                //FMeter fMeter = fMeterService.getById(fMeterRecord.getMeterId());
                fMeter.setNewNum(fMeterRecord.getNewNum());
                fMeter.setMeterReadTime(fMeterRecord.getModifiedAt());
                fMeterService.update(fMeter, token);
            } else {
                //throw new BillException(BillExceptionEnum.SYSTEM_UPDATE_ERROR);
                return "修改数据系统异常";
            }
            return "仪表抄表修改成功";
        }else{
            return "抄表刻度小于仪表的账单刻度";
        }

    }

    @Override
    public boolean delete(String id, String token) {
        SUser user = getUserByToken(token);
        if (null == id) {
            throw new BillException(BillExceptionEnum.PARAMS_MISS_ERROR);
        }
        FMeterRecord fMeterRecord = fMeterRecordMapper.selectById(id);
        fMeterRecord.setModifiedBy(user.getId());
        fMeterRecord.setModifiedName(user.getUserName());
        fMeterRecord.setIsDelete(1);
        QueryWrapper<FMeterRecord> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("id",id);
        int delete = fMeterRecordMapper.update(fMeterRecord,queryWrapper);
        if (delete > 0) {
            log.info("仪表信息删除成功，删除人={}", user.getUserName());
            return true;
        }
        throw new BillException(BillExceptionEnum.SYSTEM_UPDATE_ERROR);
    }

    @Override
    public List<FMeterRecord> list(Map<String, String> map, Integer pageNo, Integer size, String token) {
        //compId commId propertyType propertyName type no  modifiedAtBegin modifiedAtEnd pageNo size
        if(StringUtils.isEmpty(pageNo)){
            throw new BillException(BillExceptionEnum.PARAMS_MISS_ERROR);
        }
        if(StringUtils.isEmpty(size)){
            size = 10;
        }
        SUser user = getUserByToken(token);
        if(user.getCompId()==0){
            List<FMeterRecord> listMeterRecord = fMeterRecordMapper.getListMeterRecord(map.get("compName"), map.get("commName"),
                    map.get("propertyType"),map.get("propertyName"),map.get("type"),map.get("no"),
                    map.get("modifiedAtBegin"),map.get("modifiedAtEnd"),(pageNo-1)*size,size,null);
            return listMeterRecord;
        }else{
            List<FMeterRecord> listMeterRecord = fMeterRecordMapper.getListMeterRecord(map.get("compName"), map.get("commName"),
                    map.get("propertyType"),map.get("propertyName"),map.get("type"),map.get("no"),
                    map.get("modifiedAtBegin"),map.get("modifiedAtEnd"),(pageNo-1)*size,size,user.getId());
            return listMeterRecord;
        }
    }

    @Override
    public Integer listNum(Map<String, String> map, String token) {
        SUser user = getUserByToken(token);
        if(user.getCompId()==0){
            System.out.println(map.get("modifiedAtBegin")+"-----------------------------------"+map.get("modifiedAtEnd"));
            Integer listMeterRecordNum = fMeterRecordMapper.getListMeterRecordNum(map.get("compName"), map.get("commName"),
                    map.get("propertyType"),map.get("propertyName"),map.get("type"),map.get("no"),
                    map.get("modifiedAtBegin"),map.get("modifiedAtEnd"),null,null,null);
            return listMeterRecordNum;
        }else{
            Integer listMeterRecordNum = fMeterRecordMapper.getListMeterRecordNum(map.get("compName"), map.get("commName"),
                    map.get("propertyType"),map.get("propertyName"),map.get("type"),map.get("no"),
                    map.get("modifiedAtBegin"),map.get("modifiedAtEnd"),null,null,user.getId());
            return listMeterRecordNum;
        }
    }

    @Override
    public List<FMeterRecord> listAll(Map<String, String> map, String token) {
        SUser user = getUserByToken(token);
        if(user.getCompId()==0){
            List<FMeterRecord> listMeterRecord = fMeterRecordMapper.getListMeterRecord(map.get("compName"), map.get("commName"),
                    map.get("propertyType"),map.get("propertyName"),map.get("type"),map.get("no"),
                    map.get("modifiedAtBegin"),map.get("modifiedAtEnd"),null,null,null);
            return listMeterRecord;
        }else{
            List<FMeterRecord> listMeterRecord = fMeterRecordMapper.getListMeterRecord(map.get("compName"), map.get("commName"),
                    map.get("propertyType"),map.get("propertyName"),map.get("type"),map.get("no"),
                    map.get("modifiedAtBegin"),map.get("modifiedAtEnd"),null,null,user.getId());
            return listMeterRecord;
        }
    }

    @Override
    public String checkMeterRecord(FMeterRecord fMeterRecord, String token) {
        FMeter fMeter = fMeterMapper.selectById(fMeterRecord.getMeterId());
        //判断抄表刻度表里的抄表刻度大于等于仪表表里的账单刻度
        if(fMeterRecord.getNewNum().compareTo(fMeter.getBillNum())==1||fMeterRecord.getNewNum().compareTo(fMeter.getBillNum())==0){
            return null;
        }else{
            return "抄表刻度小于仪表的账单刻度";
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
