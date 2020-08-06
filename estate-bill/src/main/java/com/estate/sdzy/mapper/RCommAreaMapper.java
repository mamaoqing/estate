package com.estate.sdzy.mapper;

import com.estate.sdzy.entity.RCommArea;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * <p>
 * 社区分区表 Mapper 接口
 * </p>
 *
 * @author mq
 * @since 2020-07-28
 */
public interface RCommAreaMapper extends BaseMapper<RCommArea> {

    /**
     * 通过社区id查询分区信息
     * @param commId
     * @return
     */
    List<Map<String,Object>> listCommAreaMap(@Param("id") Long commId);
}
