package com.estate.sdzy.service.impl;

import com.estate.sdzy.entity.SCompany;
import com.estate.sdzy.entity.SUser;
import com.estate.sdzy.mapper.SUserMapper;
import com.estate.sdzy.service.SUserService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.estate.util.PasswdEncryption;
import com.estate.util.Pinyin;
import org.springframework.beans.factory.annotation.Autowired;
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
public class SUserServiceImpl extends ServiceImpl<SUserMapper, SUser> implements SUserService {

    @Autowired
    private SUserMapper userMapper;


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
        String userName = Pinyin.getPinYinHeadChar(company.getAbbreviation());
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
}
