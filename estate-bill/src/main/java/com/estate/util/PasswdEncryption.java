package com.estate.util;

import org.springframework.util.DigestUtils;
import sun.misc.BASE64Decoder;
import sun.misc.BASE64Encoder;

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

    /***
     * auth:cfy
     * date:2020年7月27日10:56:56
     * @return
     */
    public static String dencptyPasswd(String passwd) {
        StringBuffer sbf = new StringBuffer();
        byte[] pd = null;
        try {
            String password = new String(passwd.getBytes(),"utf-8");
            pd = decoder.decodeBuffer(password);
            sbf.append(pd);
            int start = sbf.indexOf("_");
            int end = sbf.length() - 1;
            sbf.replace(start, end, "");
            return sbf.toString();
        }catch (Exception e){
            e.printStackTrace();
            return null;
        }


    }
}
