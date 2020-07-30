package com.estate.sdzy.service.impl;

import com.estate.exception.BillException;
import com.estate.sdzy.entity.SCompany;
import com.estate.sdzy.entity.SUser;
import com.estate.sdzy.mapper.SUserMapper;
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
        String userName = Pinyin.getPinYinHeadChar(company.getAbbreviation()+"admin");
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
    public boolean save(SUser user, String token) {
        SUser users = getUserByToken(token);
        int insert = userMapper.insert(user);
        if (insert > 0) {
            log.info("用户添加成功，添加人={}",users.getUserName());
        }
        return insert > 0;
    }

    @Override
    public boolean saveOrUpdate(SUser user, String token) {
        SUser users = getUserByToken(token);
        int i = userMapper.updateById(user);
        if (i > 0) {
            log.info("用户更新成功，修改人={}",users.getUserName());
        }
        return i > 0;
    }

    @Override
    public boolean removeById(Long id, String token) {
        SUser users = getUserByToken(token);
        int i = userMapper.deleteById(id);
        if (i > 0) {
            log.info("用户删除成功，删除人={}",users.getUserName());
        }
        return i > 0;
    }

    private SUser getUserByToken(String token){
        Object o = redisTemplate.opsForValue().get(token);
        if (null == o) {
            log.error("登录失效，请重新登录。");
            throw new BillException(BillExceptionEnum.LOGIN_TIME_OUT);
        }
        return (SUser) o;
    }
}
