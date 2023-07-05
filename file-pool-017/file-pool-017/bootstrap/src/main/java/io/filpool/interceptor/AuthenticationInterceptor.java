package io.filpool.interceptor;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTDecodeException;
import com.auth0.jwt.exceptions.JWTVerificationException;
import io.filpool.framework.common.exception.NeedLoginException;
import io.filpool.framework.jwt.CheckLogin;
import io.filpool.framework.util.Constants;
import io.filpool.framework.util.RedisUtil;
import io.filpool.pool.entity.User;
import io.filpool.pool.service.UserService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.lang.reflect.Method;

public class AuthenticationInterceptor implements HandlerInterceptor {
    @Autowired
    private UserService userService;
    @Autowired
    private RedisUtil redisUtil;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object object) throws Exception {

        // 如果不是映射到方法直接通过
        if (!(object instanceof HandlerMethod)) {
            return true;
        }
        HandlerMethod handlerMethod = (HandlerMethod) object;
        Method method = handlerMethod.getMethod();
        if (method.isAnnotationPresent(CheckLogin.class)) {
            CheckLogin annotation = method.getAnnotation(CheckLogin.class);
            if (annotation.required()) {
                //认证
                String token = request.getHeader("token");// 从 http 请求头中取出 token
                if (StringUtils.isEmpty(token)) {
                    throw new NeedLoginException();
                }
                Long userId;
                try {
                    userId = Long.parseLong(JWT.decode(token).getAudience().get(0));
                } catch (JWTDecodeException e) {
                    throw new NeedLoginException();
                }
                String key = Constants.TOKEN_KEY + userId;
                if (!redisUtil.exists(key)) {
                    //redis中key不存在或已过期
                    throw new NeedLoginException();
                }
                //不正确
                String o = (String) redisUtil.get(key);
                if (!StringUtils.equals(o, token)) {
                    throw new NeedLoginException();
                }
                User byId = userService.getById(userId);
                if (byId == null) {
                    throw new NeedLoginException();
                }
                if (!byId.getIsEnable()){
                    //禁用了该用户
                    throw new NeedLoginException();
                }
                JWTVerifier jwtVerifier = JWT.require(Algorithm.HMAC256(byId.getPassword())).build();
                try {
                    jwtVerifier.verify(token);
                } catch (JWTVerificationException e) {
                    throw new NeedLoginException();
                }
                //设置用户信息到请求
                request.setAttribute(Constants.USER_IDENTITY_KEY, byId);
            }
        }
        return true;
    }
}
