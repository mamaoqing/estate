package com.estate.sdzy.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.estate.sdzy.entity.SRoleMenu;
import com.estate.sdzy.mapper.SRoleMenuMapper;
import com.estate.sdzy.service.SRoleMenuService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * <p>
 * 角色菜单 服务实现类
 * </p>
 *
 * @author mq
 * @since 2020-07-23
 */
@Service
@Slf4j
public class SRoleMenuServiceImpl extends ServiceImpl<SRoleMenuMapper, SRoleMenu> implements SRoleMenuService {

    @Autowired
    private SRoleMenuMapper roleMenuMapper;

    @Override
    @Transactional
    public boolean setRoleMenu(Long roleId, String menuIds) {

        String[] menuIdArr = menuIds.split(",");

        QueryWrapper<SRoleMenu> query = new QueryWrapper<>();
        query.eq("role_id",roleId);
        try{
            // 执行修改之前，先将所有的该角色的菜单关系删除，然后在从新添加
            roleMenuMapper.delete(query);

            for (String menuId : menuIdArr) {
                SRoleMenu roleMenu = new SRoleMenu();
                roleMenu.setMenuId(Long.valueOf(menuId));
                roleMenu.setRoleId(roleId);
//            roleMenu.setCompId();
                roleMenuMapper.insert(roleMenu);
            }
        }catch (Exception e){
            e.printStackTrace();
            log.error("删除菜单出现错误，角色id={}",roleId);
            return  false;
        }

        return true;
    }
}
