package com.estate.sdzy.asstes.mapper;

import com.estate.sdzy.asstes.entity.RUnit;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * <p>
 * 单元 Mapper 接口
 * </p>
 *
 * @author mq
 * @since 2020-07-28
 */
public interface RUnitMapper extends BaseMapper<RUnit> {

    List<Map<String,Object>> listUnitMap(@Param("id") Long buildId);
    List<RUnit> getAllUnit(Map map);
    Integer getPageTotal(Map map);
    List<RUnit> selectUnitByBuildingId(@Param("buildingId") Long buildingId,@Param("newBuildingId") Long newBuildingId);
}
