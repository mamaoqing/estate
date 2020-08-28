package com.estate.sdzy.asstes.mapper;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.estate.sdzy.asstes.entity.ROwner;
import com.estate.sdzy.asstes.entity.ROwnerProperty;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * <p>
 * 业主与物业对应关系 Mapper 接口
 * </p>
 *
 * @author mq
 * @since 2020-08-04
 */
public interface ROwnerPropertyMapper extends BaseMapper<ROwnerProperty> {

    List<ROwner> ownerProByParkId(@Param("id") Long id,@Param("type")String type,@Param("del")Integer del);
    List<ROwnerProperty> getOwnerProperty(@Param("ownerId") Long ownerId);


}
