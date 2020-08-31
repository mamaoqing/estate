package com.estate.sdzy.system.mapper;

import com.estate.sdzy.system.entity.SMenu;
import com.estate.sdzy.system.entity.SRoleMenu;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * <p>
 * 角色菜单 Mapper 接口
 * </p>
 *
 * @author mq
 * @since 2020-07-23
 */
@Repository
public interface SRoleMenuMapper extends BaseMapper<SRoleMenu> {

    List<SMenu> listMenu(@Param("id")Long userId);
    List<SMenu> listMenuAll();
}
