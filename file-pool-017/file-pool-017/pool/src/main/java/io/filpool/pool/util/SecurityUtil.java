package io.filpool.pool.util;

import io.filpool.framework.util.Constants;
import io.filpool.pool.entity.User;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;

public class SecurityUtil {
    public static User currentLogin(){
        return (User) getRequest().getAttribute(Constants.USER_IDENTITY_KEY);
    }

    public static HttpServletRequest getRequest() {
        ServletRequestAttributes attrs = (ServletRequestAttributes) RequestContextHolder
                .getRequestAttributes();
        return attrs.getRequest();
    }
}
