package com.estate.sdzy.asstes.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.estate.sdzy.asstes.entity.RRoom;
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
    List<RRoom> getListRoom(@Param("compName")String compName, @Param("commName")String commName, @Param("commAreaName")String commAreaName,
                            @Param("buildingName")String buildingName, @Param("unitName")String unitName, @Param("roomNo")String roomNo,
                            @Param("roomModel")String roomModel, @Param("usable")String usable,@Param("name")String name,
                            @Param("pageNo") Integer pageNo, @Param("size") Integer size, @Param("userId") Long userId);
    Integer getListRoomNum(@Param("compName")String compName, @Param("commName")String commName, @Param("commAreaName")String commAreaName,
                           @Param("buildingName")String buildingName, @Param("unitName")String unitName, @Param("roomNo")String roomNo,
                           @Param("roomModel")String roomModel, @Param("usable")String usable,@Param("name")String name,
                           @Param("pageNo") Integer pageNo, @Param("size") Integer size, @Param("userId") Long userId);
    int updateBatch(@Param("userId")Long userId,@Param("userName")String userName,@Param("ids")String[] ids);
    int updateOwnerProperty(@Param("userId")Long userId,@Param("userName")String userName,@Param("ids")String[] ids);
}
