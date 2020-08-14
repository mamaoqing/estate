package com.estate.sdzy.mapper;

import com.estate.sdzy.asstes.mapper.RCommunityMapper;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;

@SpringBootTest
@WebAppConfiguration
@RunWith(SpringJUnit4ClassRunner.class)
public class RCommunityMapperTest {

    @Autowired
    private RCommunityMapper communityMapper;

    @Test
    public void selecttest(){
        communityMapper.selectById(1);
    }

}