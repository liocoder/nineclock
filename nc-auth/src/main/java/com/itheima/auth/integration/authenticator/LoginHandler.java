package com.itheima.auth.integration.authenticator;

import com.itheima.sys.dto.CompanyUserDTO;

import javax.servlet.http.HttpServletRequest;

/**
 * @author heima
 * 登录认证器接口
 */
public interface LoginHandler {

    /**
     * 远程调用系统微服务得到用户信息
     *
     * @param request Http请求对象
     * @return 用户表实体
     */
    CompanyUserDTO queryUser(HttpServletRequest request);



    /**
     * 认证器各个实现类
     * 判断当前客户端提交认证类型是否支持当前认证器
     *
     * @param request 请求对象
     * @return true:采用当前认证器  false：不支持
     */
    boolean support(HttpServletRequest request);
}
