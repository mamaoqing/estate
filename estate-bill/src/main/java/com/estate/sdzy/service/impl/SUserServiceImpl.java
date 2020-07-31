package com.estate.sdzy.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.estate.exception.BillException;
import com.estate.sdzy.entity.SCompany;
import com.estate.sdzy.entity.SUser;
import com.estate.sdzy.entity.SUserRole;
import com.estate.sdzy.mapper.SUserMapper;
import com.estate.sdzy.mapper.SUserRoleMapper;
import com.estate.sdzy.service.SUserService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.estate.util.BillExceptionEnum;
import com.estate.util.PasswdEncryption;
import com.estate.util.Pinyin;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.util.List;

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

        return insert > 0;
    }

    @Override
    @Transactional
    public boolean setUserRole(Long userId, String roleIds, String token) {
        // 如果参数有一个为空，直接返回
        if (StringUtils.isEmpty(roleIds) || StringUtils.isEmpty(userId)) {
            throw new BillException(BillExceptionEnum.PARAMS_MISS_ERROR);
        }
        // 更新用户权限的时候，需要先删除之前存在的用户角色关系然后从新生成。
        QueryWrapper<SUserRole> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("user_id", userId);
        int delete = userRoleMapper.delete(queryWrapper);

        if (delete > 0) {
            SUser user = getUserByToken(token);
            String[] roleIdArr = roleIds.split(",");
            for (String roleId : roleIdArr) {
                SUserRole userRole = new SUserRole();
                userRole.setUserId(userId);
                userRole.setRoleId(Long.valueOf(roleId));
                userRole.setCreatedBy(user.getId());
                userRole.setCreatedName(user.getUserName());

                int insert = userRoleMapper.insert(userRole);
                if(!(insert > 0)){
                    throw new BillException(BillExceptionEnum.SET_USER_ROLE_ERROR);
                }
            }
        }else {
            throw new BillException(BillExceptionEnum.SET_USER_ROLE_ERROR);
        }

        return true;
    }

    @Override
    public boolean save(SUser user, String token) {
        SUser users = getUserByToken(token);
        if(null == user){
            throw new BillException(BillExceptionEnum.PARAMS_MISS_ERROR);
        }
        int insert = userMapper.insert(user);
        if (insert > 0) {
            log.info("用户添加成功，添加人={}", users.getUserName());
        }else{
            throw new BillException(BillExceptionEnum.SYSTEM_INSERT_ERROR);
        }
        return insert > 0;
    }

    @Override
    public boolean saveOrUpdate(SUser user, String token) {
        SUser users = getUserByToken(token);
        int i = userMapper.updateById(user);
        if (i > 0) {
            log.info("用户更新成功，修改人={}", users.getUserName());
        }else {
            throw new BillException(BillExceptionEnum.SYSTEM_UPDATE_ERROR);
        }
        return i > 0;
    }

    @Override
    public boolean removeById(Long id, String token) {
        SUser users = getUserByToken(token);
        int i = userMapper.deleteById(id);
        if (i > 0) {
            log.info("用户删除成功，删除人={}", users.getUserName());
        }else{
            throw new BillException(BillExceptionEnum.SYSTEM_DELETE_ERROR);
        }
        return i > 0;
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
