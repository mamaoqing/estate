package com.estate.sdzy.system.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.estate.common.entity.SUser;
import com.estate.common.exception.BillException;
import com.estate.common.util.BillExceptionEnum;
import com.estate.sdzy.system.entity.SMenu;
import com.estate.sdzy.system.mapper.SMenuMapper;
import com.estate.sdzy.system.mapper.SRoleMenuMapper;
import com.estate.sdzy.system.service.SMenuService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.util.List;
import java.util.Map;

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
    public List<SMenu> listMenu(String token) {
        SUser user = getUserByToken(token);
        if ("超级管理员".equals(user.getType())) {
            return roleMenuMapper.listMenuAll();
        }
        return roleMenuMapper.listMenu(user.getId());
    }

    @Override
    public boolean insertMenu(SMenu menu, String token) {
        SUser user = getUserByToken(token);
        if (null == menu) {
            throw new BillException(BillExceptionEnum.PARAMS_MISS_ERROR);
        }
        menu.setCreatedBy(user.getId());
        menu.setCreatedName(user.getUserName());

        int insert = menuMapper.insert(menu);
        if (insert > 0) {
            log.info("添加菜单信息成功，添加人={}", user.getName());
        } else {
            throw new BillException(BillExceptionEnum.SYSTEM_INSERT_ERROR);
        }
        return insert > 0;
    }

    @Override
    public boolean updateMenu(SMenu menu, String token) {
        if (null == menu) {
            throw new BillException(BillExceptionEnum.PARAMS_MISS_ERROR);
        }
        SUser user = getUserByToken(token);
        menu.setModifiedBy(user.getId());
        menu.setModifiedName(user.getUserName());
        int i = menuMapper.updateById(menu);
        if (i > 0) {
            log.info("修改菜单信息成功，修改人={}", user.getName());
        } else {
            throw new BillException(BillExceptionEnum.SYSTEM_UPDATE_ERROR);
        }
        return i > 0;
    }

    @Override
    @Transactional
    public boolean deleteMenuById(Long id, String token) {
        if (StringUtils.isEmpty(id)) {
            throw new BillException(BillExceptionEnum.PARAMS_MISS_ERROR);
        }
        SUser user = getUserByToken(token);
        SMenu menu = menuMapper.selectById(id);
        String concat = menu.getParentIdList().concat(",").concat(id + "");
        // 删除菜单分为两部，1. 删除全部的子菜单如果子菜单存在子菜单继续删除。2.删除当前菜单。
        // 1.删除当前菜单的全部子菜单,父菜单列表之间用","隔开，需要在条件中添加','
        QueryWrapper<SMenu> sMenuQueryWrapper = new QueryWrapper<>();
        sMenuQueryWrapper.and(queryWrapper -> queryWrapper.like("parent_id_list",concat.concat(",")).or().eq("parent_id_list",concat));
        menuMapper.delete(sMenuQueryWrapper);

        // 2.删除当前菜单
        int i = menuMapper.deleteById(id);
        if (i > 0) {
            log.info("菜单删除成功，删除人={}", user.getUserName());
        } else {
            throw new BillException(BillExceptionEnum.SYSTEM_DELETE_ERROR);
        }
        return i > 0;
    }

    @Override
    public Page<SMenu> getMenuListUser(String token, Map<String, String> map) {
        Integer pageNo;
        Integer size;
        if (StringUtils.isEmpty(map.get("pageNo"))) {
            throw new BillException(BillExceptionEnum.PARAMS_MISS_ERROR);
        }
        pageNo = Integer.valueOf(map.get("pageNo"));
        size = StringUtils.isEmpty(map.get("size")) ? 10 : Integer.valueOf(map.get("size"));
        SUser user = getUserByToken(token);
        QueryWrapper<SMenu> queryWrapper = new QueryWrapper<>();
        queryWrapper.like(!StringUtils.isEmpty(map.get("menuName")), "name", map.get("menuName"));
        Page<SMenu> page = new Page<>(pageNo, size);

        return menuMapper.selectPage(page,queryWrapper);
    }

    private SUser getUserByToken(String token) {
        Object o = redisTemplate.opsForValue().get(token);
        if (null == o) {
            log.error("登录失效，请重新登录。");
            throw new BillException(BillExceptionEnum.LOGIN_TIME_OUT);
        }
        return (SUser) o;
    }

}
