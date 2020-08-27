package com.estate.sdzy.tariff.mapper;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.estate.sdzy.tariff.entity.FCostItem;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;

/**
 * <p>
 * 费用项目 Mapper 接口
 * </p>
 *
 * @author mq
 * @since 2020-08-04
 */
public interface FCostItemMapper extends BaseMapper<FCostItem> {

    Page<FCostItem> listCostItem(Page<FCostItem> page, @Param("ew") QueryWrapper<FCostItem> queryWrapper);

}
