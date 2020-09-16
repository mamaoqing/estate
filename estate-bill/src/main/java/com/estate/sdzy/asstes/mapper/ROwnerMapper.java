package com.estate.sdzy.asstes.mapper;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.estate.sdzy.asstes.entity.*;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
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
    List<ROwner> getOwenerByRoom(Map map);
    List<ROwner> getExcel(Map map);
    List<ROwner> getOwenerByCommId(Map map);
    List<RRoom> selectRoomByOwnerId(Integer ownerId);
    List<RParkingSpace> selectParkByOwnerId(Integer ownerId);
    Integer selectPageTotal(Map map);
}
