package com.estate.sdzy.tariff.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.estate.sdzy.tariff.entity.FMeterRecord;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * <p>
 * 仪表流水表 Mapper 接口
 * </p>
 *
 * @author mq
 * @since 2020-08-04
 */
public interface FMeterRecordMapper extends BaseMapper<FMeterRecord> {

    //compId commId propertyType propertyName type no  modifiedAtBegin modifiedAtEnd pageNo size
    List<FMeterRecord> getListMeterRecord(@Param("compId") Long compId, @Param("commId") Long commId, @Param("propertyType") String propertyType,
                                          @Param("propertyName") String propertyName, @Param("type") String  type, @Param("no") String no,
                                          @Param("modifiedAtBegin") String modifiedAtBegin,@Param("modifiedAtEnd") String modifiedAtEnd,
                                          @Param("pageNo") Integer pageNo,@Param("size") Integer size,@Param("userId") Long userId);
    Integer getListMeterRecordNum(@Param("compId") Long compId, @Param("commId") Long commId, @Param("propertyType") String propertyType,
                                  @Param("propertyName") String propertyName, @Param("type") String  type, @Param("no") String no,
                                  @Param("modifiedAtBegin") String modifiedAtBegin,@Param("modifiedAtEnd") String modifiedAtEnd,
                                  @Param("pageNo") Integer pageNo,@Param("size") Integer size,@Param("userId") Long userId);
}
