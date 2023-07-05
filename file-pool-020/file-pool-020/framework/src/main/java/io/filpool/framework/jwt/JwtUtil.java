package io.filpool.framework.jwt;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import io.filpool.framework.common.bean.PoolUserInfo;
import io.filpool.framework.util.Constants;
import org.apache.commons.lang3.time.DateUtils;

import java.util.Date;

public class JwtUtil {
    //获取token
    public static String getToken(PoolUserInfo userInfo) {
        //只存储uid
        return JWT.create().withAudience(String.valueOf(userInfo.getUserId()))
                .withIssuedAt(new Date())
                // token过期时间
                .withExpiresAt(DateUtils.addSeconds(new Date(), Constants.TOKEN_KEY_EXPIRE.intValue()))
                .sign(Algorithm.HMAC256(userInfo.getPassword()));
    }


}
