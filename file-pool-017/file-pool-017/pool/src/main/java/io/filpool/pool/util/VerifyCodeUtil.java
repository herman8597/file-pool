package io.filpool.pool.util;

import io.filpool.config.constant.CommonRedisKey;
import io.filpool.framework.common.exception.FILPoolException;
import io.filpool.framework.util.Constants;
import io.filpool.framework.util.RedisUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

//校验验证码工具类
@Component
@Slf4j
public class VerifyCodeUtil {
    @Autowired
    private RedisUtil redisUtil;

    /**
     * 验证图片验证码
     *
     * @param verifyToken 验证token
     * @param vCode       验证码
     * @throws Exception
     */
    public void checkImageCode(String verifyToken, String vCode, boolean isRemove) throws Exception {
        if (StringUtils.isEmpty(verifyToken) || StringUtils.isEmpty(vCode)){
            throw new FILPoolException("verify.code.error.image");
        }
        String verifyKey = String.format(CommonRedisKey.VERIFY_CODE, verifyToken);
        if (!redisUtil.exists(verifyKey)) {
            throw new FILPoolException("verify.code.error.image");
        }
        String o = (String) redisUtil.get(verifyKey);
        if (!StringUtils.equals(o.toLowerCase(), vCode.toLowerCase())) {
            throw new FILPoolException("verify.code.error.image");
        }
        //删除验证码
        if (isRemove)
            redisUtil.remove(verifyKey);
    }

    /**
     * 校验手机验证码
     *
     * @param areaCode 区号
     * @param mobile   手机
     * @param code     验证码
     * @throws Exception
     */
    public void checkMobileCode(String areaCode, String mobile, String code) throws Exception {
        if (StringUtils.isEmpty(mobile) || StringUtils.isEmpty(code)){
            throw new FILPoolException("verify.code.error");
        }
        //手机验证码
        String key = Constants.VERIFY_MOBILE_KEY + areaCode + mobile;
        if (!redisUtil.exists(key)) {
            throw new FILPoolException("verify.code.error");
        }
        String mobileCode = (String) redisUtil.get(key);
        if (!StringUtils.equals(mobileCode.toLowerCase(), code.toLowerCase())) {
            throw new FILPoolException("verify.code.error");
        }
        //删除验证码
        redisUtil.remove(key);
    }
    /**
     * 校验邮箱验证码
     *
     * @param email   手机
     * @param code     验证码
     * @throws Exception
     */
    public void checkEmailCode(String email, String code) throws Exception {
        if (StringUtils.isEmpty(email) || StringUtils.isEmpty(code)){
            throw new FILPoolException("verify.code.error");
        }
        //手机验证码
        String key = Constants.VERIFY_EMAIL_KEY + email;
        if (!redisUtil.exists(key)) {
            throw new FILPoolException("verify.code.error");
        }
        String mobileCode = (String) redisUtil.get(key);
        if (!StringUtils.equals(mobileCode.toLowerCase(), code.toLowerCase())) {
            throw new FILPoolException("verify.code.error");
        }
        //删除验证码
        redisUtil.remove(key);
    }

    public void checkSecurityMobile(String areaCode, String mobile, String code) throws Exception {
        log.info("mobile :{} areaCode:{}",mobile,areaCode);
        if (StringUtils.isEmpty(mobile) || StringUtils.isEmpty(code)){
            throw new FILPoolException("verify.code.error");
        }
        String key = Constants.VERIFY_SECURITY_MOBILE_KEY + areaCode + mobile;
        log.info("key :{}",key);
        if (!redisUtil.exists(key)) {
            throw new FILPoolException("verify.code.error");
        }
        String mobileCode = (String) redisUtil.get(key);
        if (!StringUtils.equals(mobileCode.toLowerCase(), code.toLowerCase())) {
            throw new FILPoolException("verify.code.error");
        }
        //删除验证码
        redisUtil.remove(key);
    }

    public void checkSecurityEmail(String email, String code) throws Exception {
        if (StringUtils.isEmpty(email) || StringUtils.isEmpty(code)){
            throw new FILPoolException("verify.code.error");
        }
        String key = Constants.VERIFY_SECURITY_EMAIL_KEY + email;
        if (!redisUtil.exists(key)) {
            throw new FILPoolException("verify.code.error");
        }
        String mobileCode = (String) redisUtil.get(key);
        if (!StringUtils.equals(mobileCode.toLowerCase(), code.toLowerCase())) {
            throw new FILPoolException("verify.code.error");
        }
        //删除验证码
        redisUtil.remove(key);
    }

    /**
     * 校验手机验证token
     *
     * @param mobile      手机
     * @param mobileToken 验证token
     * @throws Exception
     */
    public void checkAccountToken(String mobile, String mobileToken) throws Exception {
        if (StringUtils.isEmpty(mobile) || StringUtils.isEmpty(mobileToken)){
            throw new FILPoolException("verify.code.error");
        }
        String tokenKey = Constants.VERIFY_SECURITY_TOKEN + mobile;
        if (!redisUtil.exists(tokenKey)) {
            throw new FILPoolException("verify.code.error");
        }
        String token = (String) redisUtil.get(tokenKey);
        if (!StringUtils.equals(token, mobileToken)) {
            throw new FILPoolException("verify.code.error");
        }
        redisUtil.remove(tokenKey);
    }

    /**
     * 校验谷歌验证token
     * */
    public void checkGoogleToken(String secretKey,String googleToken) throws Exception{
        if (StringUtils.isEmpty(secretKey) || StringUtils.isEmpty(googleToken)){
            throw new FILPoolException("verify.code.error");
        }
        String tokenKey = Constants.VERIFY_SECURITY_GOOGLE_KEY + secretKey;
        if (!redisUtil.exists(tokenKey)) {
            throw new FILPoolException("verify.code.error");
        }
        String token = (String) redisUtil.get(tokenKey);
        if (!StringUtils.equals(token, googleToken)) {
            throw new FILPoolException("verify.code.error");
        }
        redisUtil.remove(tokenKey);
    }




}
