package com.phantom.client.utils;

import java.security.MessageDigest;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.regex.Pattern;

public class StringUtils {

    /**
     * MD5加密
     *
     * @param src src
     * @return md5
     */
    public static String MD5(String src) {
        char hexDigits[] = {'0', '1', '2', '3', '4', '5', '6',
                '7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f'};
        try {
            MessageDigest mdTemp = MessageDigest.getInstance("MD5");
            mdTemp.update(src.getBytes());
            byte[] md = mdTemp.digest();
            int j = md.length;
            char str[] = new char[j * 2];
            int k = 0;
            for (byte byte0 : md) {
                str[k++] = hexDigits[byte0 >>> 4 & 0xf];
                str[k++] = hexDigits[byte0 & 0xf];
            }
            return new String(str);
        } catch (Exception e) {
            return "";
        }
    }

    /**
     * 获取本机当前时间
     *
     * @return 本机当前时间
     */
    public static String getTime() {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(
                "yyyy-MM-dd HH:mm:ss:SSS", Locale.getDefault());
        return simpleDateFormat.format(new Date());
    }

    /**
     * 匹配电话号码
     *
     * @param text text
     * @return 是否是电话号码
     */
    public static boolean matchPhone(CharSequence text) {
        return Pattern.compile("^[1]\\d{10}$").matcher(text).matches();
    }

}
