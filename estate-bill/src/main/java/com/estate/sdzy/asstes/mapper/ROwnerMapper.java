package com.estate.sdzy.asstes.mapper;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.estate.sdzy.asstes.entity.RCommunity;
import com.estate.sdzy.asstes.entity.ROwner;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.estate.sdzy.asstes.entity.ROwnerProperty;
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
    Integer selectPageTotal(Map map);
}
