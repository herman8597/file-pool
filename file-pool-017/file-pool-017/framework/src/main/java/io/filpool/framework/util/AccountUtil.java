package io.filpool.framework.util;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;

/**
 * 账号格式工具类
 **/
@Slf4j
public class AccountUtil {

    /**
     * @param strEmail 邮箱地址
     * @return 是否为邮箱地址
     */
    public static boolean checkEmail(String strEmail) {
        String strPattern = "^[a-zA-Z0-9][\\w\\.-]*[a-zA-Z0-9]@[a-zA-Z0-9][\\w\\.-]*[a-zA-Z0-9]\\.[a-zA-Z][a-zA-Z\\.]*[a-zA-Z]$";
        if (StringUtils.isEmpty(strEmail)) {
            return false;
        } else {
            return strEmail.matches(strPattern);
        }
    }
    /**
     * 手机号码格式校验
     *
     * @param mobile
     * @return
     */
    public static boolean checkPhone(String mobile) {
        if (StringUtils.isEmpty(mobile)) {
            return false;
        }
        if (mobile.length() < 4 || mobile.length() > 11) {
            return false;
        }
        return true;
    }

    /**
     * 判断是否是正确账号格式
     * */
    public static boolean checkAccount(String strAccount) {
        if (!checkEmail(strAccount) && !checkPhone(strAccount))
            return false;
        else
            return true;
    }

    /**
     * 手机号码脱敏
     * 截取手机号码前三位，后4为，中间4位使用*号代替
     * 18812345678
     * 188****5678
     *
     * @param phone
     * @return
     */
    public static String desensitize(String phone) {
        // 校验手机号码
        if (StringUtils.isBlank(phone)) {
            return null;
        }
        if (phone.length() < 4) {
            log.error("手机号码不合法：" + phone);
            return phone;
        }

        String before = phone.substring(0, 1);
        String after = phone.substring(phone.length() - 2);
        return  before + "****" + after;
    }

    /**
     * 邮箱脱敏
     * */
    public static String desensitizeEmail(String email) {
        // 校验手机号码
        if (StringUtils.isBlank(email)) {
            return null;
        }
        if (!checkEmail(email)) {
            log.error("邮箱地址不合法：" + email);
            return email;
        }
        return email.replaceAll("(\\w?)(\\w+?\\.?\\w+)(\\w)(@\\w+\\.[a-z]+(\\.[a-z]+)?)","$1****$3$4");
    }
}
