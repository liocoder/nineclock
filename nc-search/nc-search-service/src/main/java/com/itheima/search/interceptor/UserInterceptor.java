package com.itheima.search.interceptor;

import com.itheima.common.threadlocals.UserHolder;
import com.itheima.common.util.JsonUtils;
import com.itheima.common.vo.UserInfo;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * 拦截请求，获取token中用户信息
 */
@Component
public class UserInterceptor implements HandlerInterceptor {
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
        //对于匿名访问接口 框架封装AnonymousAuthenticationToken 认证主体用户：anonymousUser字符串
        try {
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            if (authentication instanceof AnonymousAuthenticationToken) {
                return true;
            }
            //从认证对象中获取 认证主体用户 令牌中user_name的值
            String userInfoStr = (String) authentication.getPrincipal();
            UserInfo userInfo = JsonUtils.jsonToPojo(userInfoStr, UserInfo.class);
            UserHolder.setUser(userInfo);
        } catch (Exception e) {
            e.printStackTrace();
            return true;
        }
        return true;
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) {
        UserHolder.remove();
    }
}
