package com.example.springbootdemo.utils;

import java.util.regex.Pattern;

/**
 * 验证格式工具类
 */
public class RegexUtil {

    private static final String PHONE_REGEX = "^(11|12|13|14|15|16|17|18|19)[0-9]{9}$";

    /**
     * 验证手机号码
     */
    public static boolean isPhone(String phone) {
        if (phone == null) {
            return false;
        }
        return Pattern.matches(PHONE_REGEX, phone);
    }
}
