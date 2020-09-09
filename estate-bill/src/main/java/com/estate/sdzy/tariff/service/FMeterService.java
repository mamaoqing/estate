package com.estate.sdzy.tariff.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.estate.sdzy.tariff.entity.FMeter;

import java.util.List;
import java.util.Map;

/**
 * <p>
 * 仪表表 服务类
 * </p>
 *
 * @author mq
 * @since 2020-08-04
 */
public interface FMeterService extends IService<FMeter> {

    String save(FMeter fMeter, String token);
    void saveOrUpdateMeter(FMeter fMeter, String token);
    String update(FMeter fMeter, String token);
    boolean delete(String id,String token);
    List<FMeter> list(Map<String,String> map, Integer pageNo, Integer size, String token);
    Integer listNum(Map<String,String> map,String token);
    List<FMeter> listAll(Map<String, String> parameterMap, String token);
    String checkMeterNo(FMeter fMeter);
    String getPropertyName(Long id);
}
