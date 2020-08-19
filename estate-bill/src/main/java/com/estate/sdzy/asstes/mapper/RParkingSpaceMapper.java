package com.estate.sdzy.asstes.mapper;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.estate.sdzy.asstes.entity.RParkingSpace;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * <p>
 * 停车位 Mapper 接口
 * </p>
 *
 * @author mq
 * @since 2020-08-17
 */
@Repository
public interface RParkingSpaceMapper extends BaseMapper<RParkingSpace> {


    Page<RParkingSpace> listPark(Page page, @Param("ew") QueryWrapper queryWrapper);

    Long getCommIdByName(@Param("name") String commName);

    Long getCompIdByName(@Param("name") String compName);

    Long getAreaIdByName(@Param("name") String areaName);
}
