package com.estate.sdzy.asstes.mapper;

import com.estate.sdzy.asstes.entity.RCommArea;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

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
@Repository
public interface RCommAreaMapper extends BaseMapper<RCommArea> {

    /**
     * 通过社区id查询分区信息
     * @param commId
     * @return
     */
    List<Map<String,Object>> listCommAreaMap(@Param("id") Long commId);
    List<RCommArea> listAreaByUserId(Map map);
    List<Map<String,Object>> listAreaMapByUserId(@Param("userId") Long userId);
    Integer selectPageTotal(Map map);
}
