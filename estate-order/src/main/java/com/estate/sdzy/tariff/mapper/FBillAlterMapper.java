package com.estate.sdzy.tariff.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.estate.sdzy.tariff.entity.FBillAlter;
import org.apache.ibatis.annotations.Param;

import java.util.Map;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author mq
 * @since 2020-09-02
 */
public interface FBillAlterMapper extends BaseMapper<FBillAlter> {

    Page<FBillAlter> getListBillAlter(Page page, @Param("map") Map<String,String> map, @Param("pageNo") Integer pageNo, @Param("size") Integer size, @Param("userId") Long userId);
}
