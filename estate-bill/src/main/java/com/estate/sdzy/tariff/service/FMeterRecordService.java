package com.estate.sdzy.tariff.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.estate.sdzy.tariff.entity.FMeterRecord;

import java.math.BigDecimal;
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

    String save(FMeterRecord fMeterRecord, String token);
    String saveByMeterId(Long meterId, BigDecimal newNum, String token);
    void saveOrIgnoreMeter(FMeterRecord fMeterRecord, String token);
    String update(FMeterRecord fMeterRecord, String token);
    boolean delete(String id,String token);
    List<FMeterRecord> list(Map<String,String> map, Integer pageNo, Integer size, String token);
    Integer listNum(Map<String,String> map,String token);
    List<FMeterRecord> listAll(Map<String, String> parameterMap, String token);
    String checkMeterRecord(FMeterRecord fMeterRecord, String token);
}
