package com.estate.sdzy.service;

import com.estate.sdzy.entity.SMenu;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * <p>
 * 菜单表 服务类
 * </p>
 *
 * @author mq
 * @since 2020-07-23
 */
public interface SMenuService extends IService<SMenu> {

    /**
     * 通过roleid 权限id查询对应的菜单
     * @param roleIds 权限id的集合
     * @return 返回一个菜单集合
     */
    List<SMenu> listMenu(List<Long> roleIds);

    /**
     * 添加菜单
     * @param menu 菜单信息
     * @return
     */
    boolean insertMenu(SMenu menu,String token);

    /**
     * 更新菜单
     * @param menu
     * @return
     */
    boolean updateMenu(SMenu menu,String token);

    /**
     * 删除菜单
     * @param id
     * @return
     */
    boolean deleteMenuById(Long id,String token);
}
