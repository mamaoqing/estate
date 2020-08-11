package com.estate.sdzy.mapper;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.estate.sdzy.entity.SUser;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * <p>
 * 用户表 Mapper 接口
 * </p>
 *
 * @author mq
 * @since 2020-07-23
 */
@Repository
public interface SUserMapper extends BaseMapper<SUser> {

    List<SUser> findOne(@Param("id") Integer id);
    SUser findByUserName(@Param("username") String username);

    Page<SUser> listUser(Page page,@Param("ew") QueryWrapper queryWrapper);

    Page<SUser> findUserList(Page page,@Param("compId") String compId,@Param("userName")String userName,@Param("name")String name,@Param("parentIdList")String parentIdList);
}
