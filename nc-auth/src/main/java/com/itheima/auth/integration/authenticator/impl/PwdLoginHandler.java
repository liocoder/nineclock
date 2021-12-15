package com.itheima.auth.integration.authenticator.impl;

import com.itheima.auth.integration.authenticator.LoginHandler;
import com.itheima.common.vo.Result;
import com.itheima.sys.client.SysClient;
import com.itheima.sys.dto.CompanyUserDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.oauth2.common.exceptions.OAuth2Exception;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;

@Component
public class PwdLoginHandler implements LoginHandler {

    @Autowired
    private SysClient sysClient;

    @Override
    public CompanyUserDTO queryUser(HttpServletRequest request) {
        Result<CompanyUserDTO> result = sysClient.querySysUser(request.getParameter("username"));
        CompanyUserDTO companyUserDTO = result.getData();
        if (companyUserDTO == null) {
            throw new OAuth2Exception("查询的用户不存在");
        }

        return companyUserDTO;
    }

    /**
     * 是否支持密码模式
     * @param request 请求对象
     *                提交的认证参数 auth_type = null,为密码认证
     * @return
     */
    @Override
    public boolean support(HttpServletRequest request) {
        return request.getParameter("auth_type") == null;
    }
}
