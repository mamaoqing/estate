package com.estate.sdzy.mapper;

import com.estate.sdzy.system.entity.SUser;
import com.estate.sdzy.system.mapper.SUserMapper;
import com.estate.util.PasswdEncryption;
import junit.framework.TestCase;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;

import java.util.List;

/**
 * @author mq
 * @date 2020/7/27 14:34
 * @description
 */
@SpringBootTest
@WebAppConfiguration
@RunWith(SpringJUnit4ClassRunner.class)
public class SUserMapperTest extends TestCase {

    @Autowired
    private SUserMapper userMapper;

    @Test
    public void test1(){
        SUser entity = new SUser();
        entity.setUserName("admin");
        String passwd = PasswdEncryption.encptyPasswd("123456");
        entity.setPassword(passwd);


        userMapper.insert(entity);

    }

    @Test
    public void test2(){
        List<SUser> sUsers = userMapper.selectList(null);
        sUsers.forEach(System.out::println);

    }
    @Test
    public void test3(){
        SUser entity = new SUser();
        entity.setUserName("admin");
        String passwd = PasswdEncryption.encptyPasswd("123456");
        entity.setPassword(passwd);


        userMapper.insert(entity);
    }
}