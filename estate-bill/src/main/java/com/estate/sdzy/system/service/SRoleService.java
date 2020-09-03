package com.estate.sdzy.system.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.estate.common.exception.BillException;
import com.estate.sdzy.system.entity.SMenu;
import com.estate.sdzy.system.entity.SRole;

import java.util.List;
import java.util.Map;

/**
 * <p>
 * 角色表  服务类
 * </p>
 *
 * @author mq
 * @since 2020-07-23
 */
public interface SRoleService extends IService<SRole> {

    boolean saveOrUpdate(SRole role,String token) throws BillException;
    boolean save(SRole role,String token) throws BillException;
    boolean remove(Long id,String token) throws BillException;
    List<SRole> listRole(Map<String,String> map, Integer pageNo, Integer size, String token);
    Integer listRoleNum(Map<String,String> map, Integer pageNo, Integer size, String token);
    String checkRoleMenuUser(Long id) throws BillException;
    boolean setRoleMenu(String roleId,String menuId, String token);
    List<SMenu> listRoleMenu(String token);
    List<Long> getRoleMenuByRoleId(String roleId);
}
