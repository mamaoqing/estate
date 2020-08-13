package com.estate.sdzy.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.estate.exception.BillException;
import com.estate.sdzy.entity.*;
import com.estate.sdzy.mapper.*;
import com.estate.sdzy.service.RCommRoleAgreementService;
import com.estate.sdzy.service.SMenuService;
import com.estate.sdzy.service.SRoleService;
import com.estate.sdzy.service.SUserCommService;
import com.estate.util.BillExceptionEnum;
import com.estate.util.MenuUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.util.*;

/**
 * <p>
 * 角色表  服务实现类
 * </p>
 *
 * @author mq
 * @since 2020-07-23
 */
@Service
@Slf4j
public class SRoleServiceImpl extends ServiceImpl<SRoleMapper, SRole> implements SRoleService {

    @Autowired
    private SRoleMapper roleMapper;

    @Autowired
    private SUserRoleMapper userRoleMapper;

    @Autowired
    private SCompanyMapper companyMapper;

    @Autowired
    private SUserMapper userMapper;

    @Autowired
    private SRoleMenuMapper roleMenuMapper;

    @Autowired
    private RCommRoleAgreementService rCommRoleAgreementService;

    @Autowired
    private RedisTemplate<String, Object> redisTemplate;

    @Autowired
    private SMenuService sMenuService;

    @Autowired
    private SMenuMapper sMenuMapper;

    @Autowired
    private SUserCommService sUserCommServic;

    @Override
    public boolean saveOrUpdate(SRole role, String token) throws BillException{
        SUser user = getUserByToken(token);
        if(null == role){
            throw new BillException(BillExceptionEnum.PARAMS_MISS_ERROR);
        }
        role.setModifiedBy(user.getId());
        role.setModifiedName(user.getUserName());
        int update = roleMapper.updateById(role);
        if(update > 0){
            log.info("角色信息修改成功，修改人={}",user.getUserName());
        }else{
            throw new BillException(BillExceptionEnum.SYSTEM_UPDATE_ERROR);
        }
        return update > 0;
    }

    @Override
    public boolean save(SRole role, String token) throws BillException{
        SUser user = getUserByToken(token);

        if(null == role){
            throw new BillException(BillExceptionEnum.PARAMS_MISS_ERROR);
        }
        role.setCreatedBy(user.getId());
        role.setCreatedName(user.getUserName());
        role.setModifiedBy(user.getId());
        role.setModifiedName(user.getUserName());
        role.setCompId(user.getCompId());//公司id为当前登录人的公司id
        //判断当前登录人是开发公司还是物业公司
        if(0==user.getCompId()){//是开发公司,角色类型为全局角色
            role.setType("全局角色");//角色分为、物业公司角色
        }else{//是物业公司,角色类型为物业公司角色
            role.setType("物业公司角色");//角色分为、物业公司角色
        }
        int insert = roleMapper.insert(role);
        if(insert > 0){
            log.info("角色添加成功，添加人={}",user.getUserName());
        }else{
            throw new BillException(BillExceptionEnum.SYSTEM_INSERT_ERROR);
        }
        return insert > 0;
    }

    @Override
    @Transactional
    public boolean remove(Long id, String token) throws BillException{
        SUser user = getUserByToken(token);
        if(null == id){
            throw new BillException(BillExceptionEnum.PARAMS_MISS_ERROR);
        }
        int delete = roleMapper.deleteById(id);
        //同时物理删除用户角色表
        QueryWrapper<SUserRole> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("role_id",id);//根据角色id查询用户角色中间表
        int userRole = userRoleMapper.delete(queryWrapper);
        if(userRole<=0){
            throw new BillException(BillExceptionEnum.SET_USER_ROLE_ERROR);
        }
        //同时物理删除用户菜单表
        QueryWrapper<SRoleMenu> queryRoleMenu = new QueryWrapper<>();
        queryRoleMenu.eq("role_id",id);//根据角色id查询角色菜单中间表
        int roleMenu = roleMenuMapper.delete(queryRoleMenu);
        if(roleMenu<=0){
            throw new BillException(BillExceptionEnum.SET_ROLE_MENU_ERROR);
        }
        if(delete > 0){
            log.info("角色删除成功，删除人={}",user.getUserName());
        }else{
            throw new BillException(BillExceptionEnum.SYSTEM_DELETE_ERROR);
        }
        return delete > 0;
    }

    @Override
    public List<SRole> listRole(Map<String, String> map, Integer pageNo, Integer size,String token) {
        if(StringUtils.isEmpty(pageNo)){
            throw new BillException(BillExceptionEnum.PARAMS_MISS_ERROR);
        }
        if(StringUtils.isEmpty(size)){
            size = 10;
        }
        Page<SRole> page = new Page<>(pageNo,size);
        //QueryWrapper<SRole> queryWrapper = new QueryWrapper<>();
        // 下面放查询条件
        // 名称查询
        List<String> compId = new ArrayList<>();
        if(!StringUtils.isEmpty(map.get("compId"))){//如果有公司的查询条件则只看查询的公司角色信息
            compId.add(map.get("compId"));
        }else{
            if(getUserByToken(token).getCompId()==0){//没有公司的查询条件，开发公司看全部

            }else{//没有公司的查询条件，物业公司看自己和公共的角色
                compId.add(String.valueOf(getUserByToken(token).getCompId()));
                compId.add("0");
            }
        }
        //System.out.println(map.get("name"));
        List<SRole> sSRolePage = roleMapper.findRoleList(map.get("name"),map.get("type"),compId,map.get("compId"),(pageNo-1)*size, size);
        //Page<SRole> sSRolePage = roleMapper.selectPage(page, queryWrapper);
        return sSRolePage;
    }

    @Override
    public Page<SRole> listRoleNum(Map<String, String> map, Integer pageNo, Integer size,String token) {
        if(StringUtils.isEmpty(pageNo)){
            throw new BillException(BillExceptionEnum.PARAMS_MISS_ERROR);
        }
        if(StringUtils.isEmpty(size)){
            size = 10;
        }
        Page<SRole> page = new Page<>(pageNo,size);
        QueryWrapper<SRole> queryWrapper = new QueryWrapper<>();
        // 下面放查询条件
        // 名称查询
        if(!StringUtils.isEmpty(map.get("name"))){
            queryWrapper.like("name",map.get("name"));
        }
        if(!StringUtils.isEmpty(map.get("type"))){
            queryWrapper.like("type",map.get("type"));
        }
        if(!StringUtils.isEmpty(map.get("compId"))){//如果有公司的查询条件则只看查询的公司角色信息
            queryWrapper.eq("comp_id", map.get("compId"));
        }else{
            if(getUserByToken(token).getCompId()==0){//没有公司的查询条件，开发公司看全部

            }else{//没有公司的查询条件，物业公司只看自己的所在公司和公共的角色
                queryWrapper.in("comp_id", Arrays.asList(getUserByToken(token).getCompId(),"0"));
            }
        }
        queryWrapper.orderByDesc("id");
        Page<SRole> sSRolePage = roleMapper.selectPage(page, queryWrapper);
        return sSRolePage;
    }

    @Override
    public String checkRoleMenuUser(Long id) throws BillException {
        SRole sRole = roleMapper.selectById(id);
        //根据角色id查询是否授权给用户和是否有菜单功能
        //1.根据角色id查询是否授权给用户
        List<SUserRole> userRoles= getUserRoleByRoleId(id);
        String userName="";
        if(userRoles.size()>0){//角色已经授予用户
            for (SUserRole userRole:userRoles){
                SUser sUser = userMapper.selectById(userRole.getUserId());
                if(sUser!=null){
                    userName = userName + sUser.getUserName()+"、";
                }
            }
        }
        //2.根据角色id查询是否有菜单功能
        List<SRoleMenu> roleMenus = getRoleMenuByRoleId(id);
        if(roleMenus.size()>0){//角色已有菜单功能
            if(!StringUtils.isEmpty(userName)){//角色已有菜单功能并且角色已经授予用户
                return "该角色已授权给用户，授权的用户："+userName.substring(0,userName.lastIndexOf("、"))+"，是否仍要删除？";
            }
        }
        return "";
    }

    @Override
    @Transactional
    public boolean setRoleMenu(String roleId,String menuIds, String token) {
        // 如果参数有一个为空，直接返回
        if (StringUtils.isEmpty(roleId) || StringUtils.isEmpty(menuIds)) {
            throw new BillException(BillExceptionEnum.PARAMS_MISS_ERROR);
        }
        // 更新用户权限的时候，需要先删除之前存在的用户角色关系然后从新生成。
        QueryWrapper<SRoleMenu> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("role_id", roleId);
        // 先查询用户的权限，如果没有权限，直接添加，如果有权限，需要先删除在添加新的权限
        List<SRoleMenu> sUserRoles = roleMenuMapper.selectList(queryWrapper);
        if (!sUserRoles.isEmpty()) {
            int delete = roleMenuMapper.delete(queryWrapper);
            if(!(delete > 0)){
                throw new BillException(BillExceptionEnum.SET_ROLE_MENU_ERROR);
            }
        }
        SUser user = getUserByToken(token);
        String[] menuIdArr = menuIds.split(",");
        for (String menuId : menuIdArr) {
            SRoleMenu sRoleMenu = new SRoleMenu();
            sRoleMenu.setRoleId(Long.valueOf(roleId));
            sRoleMenu.setCompId(user.getCompId());
            sRoleMenu.setMenuId(Long.valueOf(menuId));
            sRoleMenu.setCreatedBy(user.getId());
            sRoleMenu.setCreatedName(user.getUserName());
            int insert = roleMenuMapper.insert(sRoleMenu);
            if (!(insert > 0)) {
                throw new BillException(BillExceptionEnum.SET_USER_ROLE_ERROR);
            }
        }

        return true;
    }

    @Override
    public List<SMenu> listRoleMenu(String token) {
        SUser user = getUserByToken(token);
        //根据用户的社区id去查所有的授权角色，然后根据授权角色获取授权菜单列表
        if(user.getCompId()==0){//如果是开发公司则显示所有的菜单
            QueryWrapper<SMenu> queryWrapper = new QueryWrapper<>();
            queryWrapper.eq("is_delete",0);
            return MenuUtil.getAllRoleMenu(sMenuService.list(queryWrapper));
        }else{//如果不是开发公司，根据用户的社区id去查所有的授权角色，然后根据授权角色获取授权菜单列表
            return MenuUtil.getAllRoleMenu(sMenuMapper.findMenuList(user.getCompId()));
        }
    }

    public boolean isEffectiveDate(Date nowTime, Date startTime, Date endTime) {
        if (nowTime.getTime() == startTime.getTime()
                || nowTime.getTime() == endTime.getTime()) {
            return true;
        }

        Calendar date = Calendar.getInstance();
        date.setTime(nowTime);

        Calendar begin = Calendar.getInstance();
        begin.setTime(startTime);

        Calendar end = Calendar.getInstance();
        end.setTime(endTime);

        if (date.after(begin) && date.before(end)) {
            return true;
        } else {
            return false;
        }
    }

    public List<SUserRole> getUserRoleByRoleId(Long roleId){
        QueryWrapper<SUserRole> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("role_id",roleId);//根据角色id查询用户角色中间表
        List<SUserRole> sUserRoles = userRoleMapper.selectList(queryWrapper);
        return sUserRoles;
    }

    public List<SRoleMenu> getRoleMenuByRoleId(Long roleId){
        QueryWrapper<SRoleMenu> queryRoleMenu = new QueryWrapper<>();
        queryRoleMenu.eq("role_id",roleId);//根据角色id查询角色菜单中间表
        List<SRoleMenu> sRoleMenus = roleMenuMapper.selectList(queryRoleMenu);
        return sRoleMenus;
    }

    private SUser getUserByToken(String token){
        Object o = redisTemplate.opsForValue().get(token);
        if (null == o) {
            log.error("登录失效，请重新登录。");
            throw new BillException(BillExceptionEnum.LOGIN_TIME_OUT);
        }
        return (SUser) o;
    }

    public List<Long> getRoleMenuByRoleId(String roleId){
        List<SRoleMenu> roleMenus = getRoleMenuByRoleId(Long.valueOf(roleId));
        List<Long> menuIds = new ArrayList<>();
        for(SRoleMenu roleMenu:roleMenus){
            menuIds.add(roleMenu.getMenuId());
        }
        return menuIds;
    }
}
