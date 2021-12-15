package com.itheima.notify.intercepror;

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
 * 执行时机，Security校验解析JWT令牌过滤器之后执行
 * 从Security上下文中获取用户主体信息
 * @author: itheima
 * @create: 2021-05-27 11:47
 */
@Component
public class UserInterceptor implements HandlerInterceptor {


    /**
     * 从Security上下文中获取用户信息，将用户信息存入ThreadLocal
     * @param request
     * @param response
     * @param handler
     * @return
     * @throws Exception
     */
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        //从Security上下文中获取用户信息
        try {
            Authentication authentication1 = SecurityContextHolder.getContext().getAuthentication();
            //当访问是匿名可以访问请求-Security上下文中封装匿名用户字符串"AnonymousUser"
            if(authentication1 instanceof AnonymousAuthenticationToken){
                return true;
            }
            String principal = (String) authentication1.getPrincipal();
            UserInfo userInfo = JsonUtils.jsonToPojo(principal, UserInfo.class);
            //将用户信息存入ThreadLocal
            UserHolder.setUser(userInfo);
        } catch (Exception e) {
        }
        return true;
    }


    /**
     * 调用完目标方法将ThreadLocal中信息清除
     * @param request
     * @param response
     * @param handler
     * @param ex
     * @throws Exception
     */
    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
        UserHolder.remove();
    }
}