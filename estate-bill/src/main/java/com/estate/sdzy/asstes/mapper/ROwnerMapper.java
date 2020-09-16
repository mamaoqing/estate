package com.estate.sdzy.asstes.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.estate.sdzy.asstes.entity.ROwner;
import com.estate.sdzy.asstes.entity.RParkingSpace;
import com.estate.sdzy.asstes.entity.RRoom;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * <p>
 * 业主表 Mapper 接口
 * </p>
 *
 * @author mq
 * @since 2020-08-04
 */
public interface ROwnerMapper extends BaseMapper<ROwner> {

    List<ROwner> getOwenerList(Map map);
    Page<ROwner> getOwenerPageList(Page<ROwner> page,@Param("map") Map map);
    List<ROwner> getOwenerByRoom(Map map);
    List<ROwner> getExcel(Map map);
    List<ROwner> getOwenerByCommId(Map map);
    List<RRoom> selectRoomByOwnerId(Integer ownerId);
    List<RParkingSpace> selectParkByOwnerId(Integer ownerId);
    Integer selectPageTotal(Map map);
}
