package com.estate.sdzy.mapper;

import com.estate.sdzy.entity.SUserRole;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

/**
 * <p>
 * 用户角色 Mapper 接口
 * </p>
 *
 * @author mq
 * @since 2020-07-23
 */
@Repository
public interface SUserRoleMapper extends BaseMapper<SUserRole> {

    /**
     * 查询公司下的角色
     * @param userId
     * @param compId
     * @return
     */
    List<Map<String,String>> listUserRole(@Param("userId") Long userId,@Param("compId") Long compId);
}
