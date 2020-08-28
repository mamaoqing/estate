package com.estate.sdzy.system.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.estate.common.exception.BillException;
import com.estate.common.util.BillExceptionEnum;
import com.estate.sdzy.system.entity.SCompany;
import com.estate.sdzy.system.entity.SOrg;
import com.estate.sdzy.system.entity.SUser;
import com.estate.sdzy.system.entity.SUserRole;
import com.estate.sdzy.system.mapper.SOrgMapper;
import com.estate.sdzy.system.mapper.SUserMapper;
import com.estate.sdzy.system.mapper.SUserRoleMapper;
import com.estate.sdzy.system.service.SUserService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.estate.util.PasswdEncryption;
import com.estate.util.Pinyin;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.io.UnsupportedEncodingException;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * 用户表 服务实现类
 * </p>
 *
 * @author mq
 * @since 2020-07-23
 */
@Service
@Slf4j
public class SUserServiceImpl extends ServiceImpl<SUserMapper, SUser> implements SUserService {

    @Autowired
    private SUserMapper userMapper;
    @Autowired
    private RedisTemplate<String, Object> redisTemplate;
    @Autowired
    private SUserRoleMapper userRoleMapper;

    @Autowired
    private SOrgMapper orgMapper;


    @Override
    public Page<SUser> listUser(String token, Map<String, String> map) {
        Page<SUser> sUserPage = null;
        Integer pageNo = Integer.valueOf(map.get("pageNo"));
        Integer size = 10;
        if (StringUtils.isEmpty(pageNo)) {
            throw new BillException(BillExceptionEnum.PAGENO_MISS_ERROR);
        }
        if (!StringUtils.isEmpty(map.get("size"))) {
            size = Integer.valueOf(map.get("size"));
        }
        SUser user = getUserByToken(token);
        String concat = null;

        if (!StringUtils.isEmpty(map.get("orgId"))) {
            String orgId = map.get("orgId");
            // 先查询当前组织的上级
            QueryWrapper<SOrg> queryWrapper = new QueryWrapper<>();
            queryWrapper.eq("id", orgId);
            SOrg sOrg = orgMapper.selectOne(queryWrapper);
            concat = sOrg.getParentIdList().concat(",").concat(orgId);
        }

        // 区分超级管理员跟普通用户
        Page<SUser> page = new Page<>(pageNo, size);
        if (!"超级管理员".equals(user.getType())) {
            map.put("compId", user.getCompId() + "");
        }
        return userMapper.findUserList(page, map.get("compId"), map.get("userName"), map.get("name"), concat);
    }

    @Override
    @Transactional
    public List<SUser> findOne(Integer id) {
        return userMapper.findOne(id);
    }

    @Override
    public SUser findByUserName(String username) {
        return userMapper.findByUserName(username);
    }

    @Override
    public Boolean autoSave(SCompany company) {
        Long companyId = company.getId();
        String companyName = company.getName();
        String userName = Pinyin.getPinYinHeadChar(company.getAbbreviation() + "admin");
        String password = PasswdEncryption.encptyPasswd("123456");
        String type = "管理员";
        String remark = "创建公司系统自动生成管理员账号";
        String state = "";
        SUser user = new SUser();
        user.setPassword(password);
        user.setUserName(userName);
        user.setCompId(companyId);
        user.setType(type);
        user.setRemark(remark);
        user.setState(state);

        int insert = userMapper.insert(user);
        if (insert > 0) {
            log.info("自动添加公司管理员角色，登录用户名={},密码=123456", userName);
            return insert > 0;
        }
        log.error("自动添加管理员角色失败,请联系管理员添加账号");
        throw new BillException(BillExceptionEnum.SYSTEM_INSERT_ERROR);


    }

    @Override
    @Transactional
    public boolean setUserRole(Long userId, Long compId, String roleIds, String token) {
        // 如果参数有一个为空，直接返回
        if (StringUtils.isEmpty(roleIds) || StringUtils.isEmpty(userId)) {
            throw new BillException(BillExceptionEnum.PARAMS_MISS_ERROR);
        }
        // 更新用户权限的时候，需要先删除之前存在的用户角色关系然后从新生成。
        QueryWrapper<SUserRole> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("user_id", userId);
        // 先查询用户的权限，如果没有权限，直接添加，如果有权限，需要先删除在添加新的权限
        List<SUserRole> sUserRoles = userRoleMapper.selectList(queryWrapper);
        if (!sUserRoles.isEmpty()) {
            int delete = userRoleMapper.delete(queryWrapper);
            if (!(delete > 0)) {
                throw new BillException(BillExceptionEnum.SET_USER_ROLE_ERROR);
            }
        }

        SUser user = getUserByToken(token);
        String[] roleIdArr = roleIds.split(",");
        for (String roleId : roleIdArr) {
            SUserRole userRole = new SUserRole();
            userRole.setUserId(userId);
            userRole.setCompId(compId);
            userRole.setRoleId(Long.valueOf(roleId));
            userRole.setCreatedBy(user.getId());
            userRole.setCreatedName(user.getUserName());

            int insert = userRoleMapper.insert(userRole);
            if (!(insert > 0)) {
                throw new BillException(BillExceptionEnum.SET_USER_ROLE_ERROR);
            }
        }

        return true;
    }

    @Override
    public boolean save(SUser user, String token) {
        SUser users = getUserByToken(token);
        String psw = PasswdEncryption.encptyPasswd(user.getPassword());
        user.setPassword(psw);
        user.setCreatedBy(users.getId());
        user.setCreatedName(users.getUserName());
        int insert = userMapper.insert(user);
        if (insert > 0) {
            log.info("用户添加成功，添加人={}", users.getUserName());
            return true;
        }
        throw new BillException(BillExceptionEnum.SYSTEM_INSERT_ERROR);

    }

    @Override
    public boolean saveOrUpdate(SUser user, String token) {
        SUser users = getUserByToken(token);
        user.setModifiedBy(users.getId());
        user.setModifiedName(users.getUserName());
        int i = userMapper.updateById(user);
        if (i > 0) {
            log.info("用户更新成功，修改人={}", users.getUserName());
            return true;
        }
        throw new BillException(BillExceptionEnum.SYSTEM_UPDATE_ERROR);

    }

    @Override
    public boolean removeById(Long id, String token) {
        SUser users = getUserByToken(token);
        int i = userMapper.deleteById(id);
        if (i > 0) {
            log.info("用户删除成功，删除人={}", users.getUserName());
            return true;
        }
        throw new BillException(BillExceptionEnum.SYSTEM_DELETE_ERROR);

    }

    @Override
    public boolean reSetPassword(String password, Long id, String token, String oldPassword) {
        SUser userByToken = getUserByToken(token);
        if (StringUtils.isEmpty(password) || StringUtils.isEmpty(id) || StringUtils.isEmpty(oldPassword)) {
            throw new BillException(BillExceptionEnum.PARAMS_MISS_ERROR);
        }
        SUser user = userMapper.selectById(id);
        String s = PasswdEncryption.dencptyPasswd(user.getPassword());
        String s1 = null;
        try {
            s1 = PasswdEncryption.setMD5String(oldPassword);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

        if (!s.equals(s1)) {
            throw new BillException(BillExceptionEnum.RESET_PASSWORD_ERROR);
        }

        String psw = PasswdEncryption.encptyPasswd(password);
        user.setPassword(psw);
        int i = userMapper.updateById(user);
        if (i > 0) {
            log.info("密码修改成功,修改人{}", userByToken.getUserName());
            return true;
        }
        throw new BillException(BillExceptionEnum.SYSTEM_UPDATE_ERROR);
    }

    @Override
    public boolean reSetPassword(String password, String token, Long id) {
        SUser user = getUserByToken(token);
        if (StringUtils.isEmpty(password)) {
            throw new BillException(BillExceptionEnum.PARAMS_MISS_ERROR);
        }
        SUser sUser = userMapper.selectById(id);
        String psw = PasswdEncryption.encptyPasswd(password);
        sUser.setModifiedName(user.getUserName());
        sUser.setModifiedBy(user.getId());
        sUser.setPassword(psw);
        int i = userMapper.updateById(sUser);
        if (i > 0) {
            log.info("用户{}密码重置成功,操作人{}", sUser.getUserName(), user.getUserName());
            return true;
        }
        throw new BillException(BillExceptionEnum.RESET_PASSWORD_ERROR_SYSTEM);
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
