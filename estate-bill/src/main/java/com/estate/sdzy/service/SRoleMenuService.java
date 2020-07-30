package com.estate.sdzy.service;

import com.estate.sdzy.entity.SRoleMenu;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * <p>
 * 角色菜单 服务类
 * </p>
 *
 * @author mq
 * @since 2020-07-23
 */
public interface SRoleMenuService extends IService<SRoleMenu> {

    /**
     * 设置角色菜单
     * @param roleId 角色id
     * @param menuIds 菜单id的string，每个菜单id之间用“,”隔开
     * @return
     */
    boolean setRoleMenu(Long roleId,String menuIds);
}
