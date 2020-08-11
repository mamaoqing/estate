package com.estate.sdzy.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.estate.sdzy.entity.SDictItem;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * <p>
 * 字典表 Mapper 接口
 * </p>
 *
 * @author mq
 * @since 2020-07-23
 */
@Repository
public interface SDictItemMapper extends BaseMapper<SDictItem> {

    List<SDictItem> findDictItemList(@Param("name")String name,@Param("dictId")String dictId, @Param("pageNo")Integer pageNo, @Param("size")Integer size,@Param("compId")String compId);
}
