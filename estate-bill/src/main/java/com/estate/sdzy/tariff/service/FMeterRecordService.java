package com.estate.sdzy.tariff.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.estate.sdzy.tariff.entity.FMeterRecord;

import java.util.List;
import java.util.Map;

/**
 * <p>
 * 仪表流水表 服务类
 * </p>
 *
 * @author mq
 * @since 2020-08-04
 */
public interface FMeterRecordService extends IService<FMeterRecord> {

    boolean save(FMeterRecord fMeterRecord, String token);
    void saveOrIgnoreMeter(FMeterRecord fMeterRecord, String token);
    boolean update(FMeterRecord fMeterRecord, String token);
    boolean delete(String id,String token);
    List<FMeterRecord> list(Map<String,String> map, Integer pageNo, Integer size, String token);
    Integer listNum(Map<String,String> map,String token);
    List<FMeterRecord> listAll(Map<String, String> parameterMap, String token);
}
