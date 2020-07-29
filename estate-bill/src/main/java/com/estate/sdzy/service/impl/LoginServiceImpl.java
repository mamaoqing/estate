package com.estate.sdzy.service.impl;

import com.estate.sdzy.entity.SUser;
import com.estate.sdzy.service.LoginService;
import com.estate.util.PasswdEncryption;
import com.estate.util.Result;
import com.estate.util.ResultUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.util.DigestUtils;
import org.springframework.util.StringUtils;

import javax.servlet.http.HttpServletRequest;
import java.util.UUID;
import java.util.concurrent.TimeUnit;


@Service
public class LoginServiceImpl implements LoginService {

    @Autowired
    private SUserServiceImpl userService;
    @Autowired
    private RedisTemplate<String, Object> redisTemplate;

    public Result login(HttpServletRequest req) {
        String username = req.getParameter("username");
        String passwd = req.getParameter("passwd");
        SUser user = userService.findByUserName(username);
        if(StringUtils.isEmpty(user)){
            return ResultUtil.error("没有找到用户信息，请重新确认",0);
        }
        String password = user.getPassword();
        StringBuffer sbf = new StringBuffer(passwd);
        //在用户输入的密码两头拼接sdzy
        sbf.insert(0, "sdzy");
        sbf.insert(passwd.length() - 1, "sdzy");
        //用md5加密
        String pwd = DigestUtils.md5DigestAsHex(sbf.toString().getBytes());
        String pd = PasswdEncryption.dencptyPasswd(password);

        if(pwd.equals(pd)){
            String s = UUID.randomUUID().toString();
            String s1 = s.replace("-", "");
            redisTemplate.opsForValue().set(s1,user,30*60, TimeUnit.SECONDS);
            return ResultUtil.success(s1);
        }else{
            return ResultUtil.error("登陆失败，请联系管理员",0);
        }
    }
}
