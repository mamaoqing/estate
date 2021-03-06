package com.estate.sdzy.common.service.impl;

import com.estate.common.entity.SUser;
import com.estate.common.exception.BillException;
import com.estate.common.util.BillExceptionEnum;
import com.estate.common.util.Result;
import com.estate.common.util.ResultUtil;
import com.estate.sdzy.common.service.LoginService;
import com.estate.sdzy.system.service.impl.SUserServiceImpl;
import com.estate.util.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.DigestUtils;
import org.springframework.util.StringUtils;

import javax.servlet.http.HttpServletRequest;
import java.util.UUID;


@Service
public class LoginServiceImpl implements LoginService {

    @Autowired
    private RedisUtil redisUtil;

    @Autowired
    private SUserServiceImpl userService;

    public Result login(HttpServletRequest req) {
        String username = req.getParameter("username");
        String passwd = req.getParameter("passwd");
        SUser user = userService.findByUserName(username);
        if(StringUtils.isEmpty(user)){
            return ResultUtil.error("没有找到用户信息，请重新确认",1);
        }
        String password = user.getPassword();
        StringBuffer sbf = new StringBuffer(passwd);
        //在用户输入的密码两头拼接sdzy
        sbf.insert(0, "sdzy");
        sbf.insert(passwd.length() - 1, "sdzy");
        //用md5加密
        String pwd = DigestUtils.md5DigestAsHex(sbf.toString().getBytes());
        String pd = PasswdEncryption.dencptyPasswd(password);

        if(pwd.equals(pd)) {
            String key = String.valueOf(UUID.randomUUID());
            String token = key+":"+user.getUserName();
            // 设置过期时间单位秒
            redisUtil.set(token, user, 500 * 60);
            return ResultUtil.success(token);
        }else{
            return ResultUtil.error("登陆失败，请联系管理员",1);
        }
    }

    @Override
    public boolean logout(String token) {
        if(StringUtils.isEmpty(token)){
            throw new BillException(BillExceptionEnum.PARAMS_MISS_ERROR);
        }
        Object o = redisUtil.get(token);
        if(null == o){
            return true;
        }
        return  redisUtil.delete(token);
    }


}
