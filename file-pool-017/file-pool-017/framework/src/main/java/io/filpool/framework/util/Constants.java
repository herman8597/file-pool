package io.filpool.framework.util;

public class Constants {
    public static final String TOKEN_KEY = "mobile:";
    //过期时间
    public static final Long TOKEN_KEY_EXPIRE = 60L * 60 * 24 * 7;
    public static final String USER_IDENTITY_KEY = "user_identity_key";
    public static final String API_PREFIX = "/v1";
    /**
     * 手机验证码码缓存
     */
    public static final String VERIFY_MOBILE_KEY = "mobile:verify:";
    /**
     * 邮箱验证码码缓存
     */
    public static final String VERIFY_EMAIL_KEY = "email:verify:";
    /**
     * 登录后的手机安全验证码
     */
    public static final String VERIFY_SECURITY_MOBILE_KEY = "mobile:security:verify:";
    /**
     * 登录后的邮箱安全验证码
     */
    public static final String VERIFY_SECURITY_EMAIL_KEY = "email:security:verify:";
    public static final String USDT_PRICE_KEY = "USDT_PRICE";
//    public static final String CURRENCY_PRICE_KEY = "CURRENCY_PRICE:";
//    public static final String CURRENCY_PERCENT_KEY = "CURRENCY_PERCENT:";

    public static final String VERIFY_SECURITY_TOKEN = "account:security:token:";
    /**
     * 验证码有效期
     */
    public static final Long VERIFY_CODE_TIMEOUT = 600L;

    /**
     * 订单有效时间
     * */
    public static final Long ORDER_KEY_EXPIRE = 60L * 15;
    /**
     * 订单缓存
     * */
    public static final String ORDER_NUMBER_KEY = "order:number:";
    /**
     * 谷歌校验码
     * */
    public static final String VERIFY_SECURITY_GOOGLE_KEY = "google:security:verify:";
    /**
     * 获取币种USDT价格
     */
    public static final String COIN_PRICE_URL = "https://dncapi.bqiapp.com/api/search/websearch?pagesize=1&code=";
    public static final String COIN_PRICE_URL_URL = "https://dncapi.bqiapp.com/api/coin/web-coinrank?page=1&type=-1&pagesize=10&webp=1";
    public static final String CURRENCY_FIL_KEY = "CURRENCY_FIL_INFO";
    public static final String FIL_INFO = "FIL:INFO";
}
