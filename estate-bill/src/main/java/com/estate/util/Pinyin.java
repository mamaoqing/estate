package com.estate.util;

import net.sourceforge.pinyin4j.PinyinHelper;

/**
 * @author mq
 * @date 2020/7/27 17:22
 * @description 拼音工具类
 */
public class Pinyin {

    /**
     * 得到中文首字母
     *
     * @param str
     * @return
     */
    public static String getPinYinHeadChar(String str) {

        String convert = "";
        for (int j = 0; j < str.length(); j++) {
            char word = str.charAt(j);
            String[] pinyinArray = PinyinHelper.toHanyuPinyinStringArray(word);
            if (pinyinArray != null) {
                convert += pinyinArray[0].charAt(0);
            } else {
                convert += word;
            }
        }
        return convert;
    }
}
