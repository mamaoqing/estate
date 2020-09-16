package com.estate.sdzy.wechat.util;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.util.Arrays;

/**
 * @author mq
 * @description: TODO
 * @title: CheckUtil
 * @projectName estate-parent
 * @date 2020/9/1517:34
 */
public class CheckUtil {

    private static final  String token = "sdzy";

    public static boolean checkWeChat(String signature,String timestamp,String nonce){
        String [] arr = new String[]{token,timestamp,nonce};

        // 字典排序
        Arrays.sort(arr);
        StringBuilder sb = new StringBuilder();
        for (String s : arr) {
            sb.append(s);
        }

        // sha1加密
        String result = encrypt(sb.toString());

        return signature.equals(result);
    }

    public static String encrypt(String str){
        if(str == null || str.length()==0){
            return null;
        }
        char hexDigits[]={'0','1','2','3','4','5','6','7','8','9','a','b','c','d','e','f'};

        try {
            MessageDigest mdTemp = MessageDigest.getInstance("SHA1");
            mdTemp.update(str.getBytes("UTF-8"));

            byte[] md = mdTemp.digest();
            int j = md.length;
            char buf[] = new char[j*2];
            int k = 0;
            for(int i=0;i<j;i++){
                byte byte0 = md[i];
                buf[k++] = hexDigits[byte0 >>> 4 & 0xf];
                buf[k++] = hexDigits[byte0 & 0xf];
            }
            return new String(buf);
        } catch (Exception e) {
            return null;
        }
    }
}
