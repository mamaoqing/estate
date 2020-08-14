package com.estate.sdzy.system.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.estate.sdzy.system.entity.SMenu;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * <p>
 * 菜单表 Mapper 接口
 * </p>
 *
 * @author mq
 * @since 2020-07-23
 */
@Repository
public interface SMenuMapper extends BaseMapper<SMenu> {

    List<SMenu> findMenuList(@Param("compId")Long name);
}
