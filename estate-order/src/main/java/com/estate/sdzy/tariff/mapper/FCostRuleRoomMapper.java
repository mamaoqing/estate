package com.estate.sdzy.tariff.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.estate.sdzy.tariff.entity.FCostRuleRoom;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author mq
 * @since 2020-08-27
 */
public interface FCostRuleRoomMapper extends BaseMapper<FCostRuleRoom> {

    String getRoomIds(@Param("ruleId") Long ruleId);
    String getParkIds(@Param("ruleId") Long ruleId);

    List<Map<String,String>> costPark(@Param("ruleId") Long ruleId);
}
