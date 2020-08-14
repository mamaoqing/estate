package com.estate.sdzy.system.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.estate.sdzy.system.entity.SRole;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * <p>
 * 角色表  Mapper 接口
 * </p>
 *
 * @author mq
 * @since 2020-07-23
 */
@Repository
public interface SRoleMapper extends BaseMapper<SRole> {

    List<SRole> findRoleList(@Param("name")String name, @Param("type")String type, @Param("compId")List compId ,@Param("compid")String compid , @Param("pageNo")Integer pageNo, @Param("size")Integer size);
}
