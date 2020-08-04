package com.estate.util;

import org.springframework.util.DigestUtils;
import sun.misc.BASE64Decoder;
import sun.misc.BASE64Encoder;

import java.io.UnsupportedEncodingException;
import java.util.Base64;
import java.util.Calendar;

public class PasswdEncryption {

    static final BASE64Encoder encoder = new BASE64Encoder();
    static final BASE64Decoder decoder = new BASE64Decoder();

    /***
     * 密码加密方法
     * auth:cfy
     * date:2020年7月27日10:56:56
     * @return
     */
    public static String encptyPasswd(String passwd){
        StringBuffer sbf = new StringBuffer();
        sbf.append(passwd);
        //在用户输入的密码两头拼接sdzy
        sbf.insert(0,"sdzy");
        sbf.insert(passwd.length()-1,"sdzy");
        //用md5加密
        String pwd = DigestUtils.md5DigestAsHex(sbf.toString().getBytes());
        String now = Calendar.getInstance().getTimeInMillis()+"";
        sbf.setLength(0);
        sbf.append(pwd);
        //加密后的字符串最后在拼接上当前时间戳
        sbf.append("_"+now);
        String password = sbf.toString();
        byte[] pd = null;
        try {
            pd = password.getBytes("utf-8");
            return encoder.encode(pd);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }

    }

    public static String setMD5String(String password) throws UnsupportedEncodingException {
        StringBuffer sbf = new StringBuffer();
        sbf.append(password);
        //在用户输入的密码两头拼接sdzy
        sbf.insert(0,"sdzy");
        sbf.insert(password.length()-1,"sdzy");
        //用md5加密
        String pwd = DigestUtils.md5DigestAsHex(sbf.toString().getBytes());
//        byte[] bytes = pwd.getBytes("utf-8");
        return pwd;//encoder.encode(bytes);
    }

    /***
     * 密码解密方法
     * auth:cfy
     * date:2020年7月27日10:56:56
     * @return
     */
    public static String dencptyPasswd(String passwd) {
        StringBuffer sbf = new StringBuffer();
        byte[] pd = null;
        try {
            Base64.Decoder decoder = Base64.getDecoder();
            pd = decoder.decode(passwd);
            String pwd = new String(pd,"utf-8");
            int start = pwd.indexOf("_");
            int end = pwd.length();
            sbf.append(pwd);
            sbf.replace(start, end, "");
            return sbf.toString();
        }catch (Exception e){
            e.printStackTrace();
            return null;
        }


    }
}
