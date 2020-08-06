package com.estate.sdzy.mapper;

import com.estate.sdzy.entity.RCommunity;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;

import java.util.Map;

/**
 * <p>
 * 社区表 Mapper 接口
 * </p>
 *
 * @author mq
 * @since 2020-07-28
 */
public interface RCommunityMapper extends BaseMapper<RCommunity> {

    /**
     * 通过用户权限id查询社区
     * @param id
     * @return 返回一个map
     */
    Map<String,Object> communityMap(@Param("id") Long id);

}
