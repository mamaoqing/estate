package com.estate.sdzy.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.estate.sdzy.entity.SMenu;
import com.estate.sdzy.entity.SRoleMenu;
import com.estate.sdzy.entity.SUser;
import com.estate.sdzy.mapper.SMenuMapper;
import com.estate.sdzy.mapper.SRoleMenuMapper;
import com.estate.sdzy.service.SMenuService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * <p>
 * 菜单表 服务实现类
 * </p>
 *
 * @author mq
 * @since 2020-07-23
 */
@Service
@Slf4j
public class SMenuServiceImpl extends ServiceImpl<SMenuMapper, SMenu> implements SMenuService {

    @Autowired
    private SMenuMapper menuMapper;

    @Autowired
    private SRoleMenuMapper roleMenuMapper;

    @Autowired
    private RedisTemplate<String, Object> redisTemplate;

    @Override
    public List<SMenu> listMenu(List<Long> roleIds) {
        List<SMenu> list = new ArrayList<>();
        // 如果传递的参数不存在直接返回。
        if (roleIds.isEmpty()) {
            return list;
        }

        for (Long roleId : roleIds) {
            // 1.通过roleid查询出菜单id -> menuid,一个权限role可能对应多个菜单menu；
            QueryWrapper<SRoleMenu> qw = new QueryWrapper<>();
            qw.eq("role_id", roleId);
            List<SRoleMenu> sRoleMenus = roleMenuMapper.selectList(qw);

            // 2.遍历得到的权限菜单表，通过菜单id查询该权限下的所有的菜单
            for (SRoleMenu x : sRoleMenus) {
                Long menuId = x.getMenuId();
                QueryWrapper<SMenu> q = new QueryWrapper();
                q.eq("id", menuId);
                SMenu sMenu = menuMapper.selectOne(q);

                list.add(sMenu);
            }
        }

        return list;
    }

    @Override
    public boolean insertMenu(SMenu menu, String token) {
        SUser user = getUserByToken(token);
        if (null == user) {
            return false;
        }
        menu.setCreatedBy(user.getId());
        menu.setCreatedName(user.getUserName());

        int insert = menuMapper.insert(menu);
        if (insert > 0) {
            log.info("添加菜单信息成功，添加人={}", user.getName());
        }
        return insert > 0;
    }

    @Override
    public boolean updateMenu(SMenu menu, String token) {

        SUser user = getUserByToken(token);
        if (null == user) {
            return false;
        }
        menu.setModifiedBy(user.getId());
        menu.setModifiedName(user.getUserName());
        int i = menuMapper.updateById(menu);
        if (i > 0) {
            log.info("修改菜单信息成功，修改人={}", user.getName());
        }
        return i > 0;
    }

    @Override
    @Transactional
    public boolean deleteMenuById(Long id, String token) {
        if (StringUtils.isEmpty(id)) {
            return false;
        }
        SUser user = getUserByToken(token);
        if (null == user) {
            return false;
        }

        // 删除菜单分为两部，1. 删除全部的子菜单如果子菜单存在子菜单继续删除。2.删除当前菜单。
        // 1.删除当前菜单的全部子菜单
        QueryWrapper<SMenu> qw = new QueryWrapper<>();
        qw.like("parent_id_list", id + ",");
        menuMapper.delete(qw);

        // 2.删除当前菜单
        int i = menuMapper.deleteById(id);
        if (i > 0) {
            log.info("菜单删除成功，删除人={}", user.getUserName());
        }
        return i > 0;
    }

    private SUser getUserByToken(String token) {
        Object o = redisTemplate.opsForValue().get(token);
        if (null == o) {
            log.error("登录失效，请重新登录。");
            return null;
        }
        return (SUser) o;
    }

}
