package com.estate.sdzy.mapper;

import com.estate.sdzy.entity.RRoom;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * <p>
 * 房间 Mapper 接口
 * </p>
 *
 * @author mq
 * @since 2020-07-28
 */
public interface RRoomMapper extends BaseMapper<RRoom> {

    List<Map<String,Object>> listRoomMap(@Param("id") Long unitId);
}
