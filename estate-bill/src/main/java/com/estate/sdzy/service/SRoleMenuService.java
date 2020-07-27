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

    boolean setRoleMenu(Long roleId,String menuIds);
}
