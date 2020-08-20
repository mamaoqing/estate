package com.estate.sdzy.asstes.mapper;

import com.estate.sdzy.asstes.entity.ROwner;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

import java.util.List;

/**
 * <p>
 * 业主表 Mapper 接口
 * </p>
 *
 * @author mq
 * @since 2020-08-04
 */
public interface ROwnerMapper extends BaseMapper<ROwner> {

    public List<ROwner> getOwenerList(Long compId);
}
