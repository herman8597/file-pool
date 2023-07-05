package network.vena.cooperation.common.utils;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTDecodeException;
import com.auth0.jwt.interfaces.DecodedJWT;
import org.jeecg.common.exception.JeecgBootException;

import javax.servlet.http.HttpServletRequest;
import java.util.Date;

/**
 * 基于管理端的Jwt扩展方法
 */
public class  ApiJwtUtil {

    // Token过期时间30分钟（用户登录过期时间是此时间的两倍，以token在reids缓存时间为准）
    public static final long EXPIRE_TIME = 120 * 60 * 1000;

    public static final String userName = "username";

    public static final String userId = "mallUserId";

    /**
     * 校验token是否正确
     *
     * @param token  密钥
     * @param secret 用户的密码（用户id）
     * @return 是否正确
     */
    public static boolean verify(String token, String username, String secret) {
        try {
            // 根据密码生成JWT效验器
            Algorithm algorithm = Algorithm.HMAC256(secret);
            JWTVerifier verifier = JWT.require(algorithm).withClaim(userId, secret).withClaim(userName, username).build();
            // 效验TOKEN
            DecodedJWT jwt = verifier.verify(token);
            return true;
        } catch (Exception exception) {
            return false;
        }
    }

    /**
     * 生成签名,5min后过期
     *
     * @param username 用户名
     * @param secret   用户的密码(用户ID)
     * @return 加密的token
     */
    public static String sign(String username, String secret) {
        Date date = new Date(System.currentTimeMillis() + EXPIRE_TIME);
        Algorithm algorithm = Algorithm.HMAC256(secret);
        // 附带username信息
        return JWT.create().withClaim(userId, secret).withClaim(userName, username).withExpiresAt(date).sign(algorithm);
    }

    /**
     * 获得token中的信息无需secret解密也能获得
     *
     * @return token中包含的用户名
     */
    public static String getUsername(String token) {
        try {
            DecodedJWT jwt = JWT.decode(token);
            return jwt.getClaim(userName).asString();
        } catch (JWTDecodeException e) {
            return null;
        }
    }

    /**
     * 获得token中的信息无需secret解密也能获得
     *
     * @return token中包含的会员Id
     */
    public static String getMallUserId(String token) {
        try {
            DecodedJWT jwt = JWT.decode(token);
            return jwt.getClaim(userId).asString();
        } catch (JWTDecodeException e) {
            return null;
        }
    }

    /**
     * 获得token中的信息无需secret解密也能获得
     *
     * @return token中包含的会员Id
     */
    public static String getMallUserId(HttpServletRequest request) {
        try {
            DecodedJWT jwt = JWT.decode(getToken(request));
            return jwt.getClaim(userId).asString();
        } catch (JWTDecodeException e) {
            e.printStackTrace();
            return null;
        }
    }


    /**
     * 获取request中的token
     *
     * @param request
     * @return
     * @throws JeecgBootException
     */
    public static String getToken(HttpServletRequest request) throws JeecgBootException {
        String token = request.getHeader("X-Access-Token");
        return token;
    }

    public static void main(String[] args) {
        String token =   ApiJwtUtil.sign("18218606373", "1212356287787827202");
        System.out.println(token);
    }
}
