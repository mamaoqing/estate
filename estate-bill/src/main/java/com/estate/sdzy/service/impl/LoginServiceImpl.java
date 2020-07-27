package com.estate.sdzy.service.impl;

import com.estate.sdzy.entity.SUser;
import com.estate.sdzy.service.LoginService;
import com.estate.util.PasswdEncryption;
import com.estate.util.Result;
import com.estate.util.ResultUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.DigestUtils;

import javax.servlet.http.HttpServletRequest;


@Service
public class LoginServiceImpl implements LoginService {

    @Autowired
    private SUserServiceImpl userService;

    public Result login(HttpServletRequest req) {
        String username = req.getParameter("username");
        String passwd = req.getParameter("passwd");
        SUser user = userService.findByUserName(username);
        String password = user.getPassword();
        StringBuffer sbf = new StringBuffer(passwd);
        //在用户输入的密码两头拼接sdzy
        sbf.insert(0, "sdzy");
        sbf.insert(passwd.length() - 1, "sdzy");
        //用md5加密
        String pwd = DigestUtils.md5DigestAsHex(sbf.toString().getBytes());
        String pd = PasswdEncryption.dencptyPasswd(password);

        if(pwd.equals(pd)){
            return ResultUtil.success();
        }else{
            return ResultUtil.error("登陆失败",0);
        }
    }
}
