package com.estate.sdzy.tariff.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.estate.sdzy.tariff.entity.FMeter;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * <p>
 * 仪表表 Mapper 接口
 * </p>
 *
 * @author mq
 * @since 2020-08-04
 */
public interface FMeterMapper extends BaseMapper<FMeter> {

    List<FMeter> getListMeter(@Param("compId") String compId, @Param("commId") String  commId, @Param("commAreaId") String  commAreaId,@Param("propertyType") String  propertyType,
                              @Param("propertyName") String propertyName,@Param("type") String type,@Param("no") String no,
                              @Param("pageNo") Integer pageNo,@Param("size") Integer size,@Param("userId") Long userId);
    Integer getListMeterNum(@Param("compId") String compId, @Param("commId") String  commId, @Param("commAreaId") String  commAreaId,@Param("propertyType") String  propertyType,
                              @Param("propertyName") String propertyName,@Param("type") String type,@Param("no") String no,
                              @Param("pageNo") Integer pageNo,@Param("size") Integer size,@Param("userId") Long userId);
    Integer updateMeter(@Param("state") String state,@Param("remark") String remark,@Param("userId") Long userId,
                        @Param("userName") String userName,@Param("id") Long id);
    List<Long>  getMeterByNo(@Param("no") String no,@Param("commId") Long commId);
}
