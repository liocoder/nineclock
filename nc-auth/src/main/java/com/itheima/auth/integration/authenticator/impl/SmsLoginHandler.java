package com.itheima.auth.integration.authenticator.impl;

import com.itheima.auth.integration.authenticator.LoginHandler;
import com.itheima.common.constants.Constant;
import com.itheima.common.util.JsonUtils;
import com.itheima.common.vo.Result;
import com.itheima.sys.client.SysClient;
import com.itheima.sys.dto.CompanyUserDTO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.common.exceptions.OAuth2Exception;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;

@Slf4j
@Component
public class SmsLoginHandler implements LoginHandler {

    @Autowired
    private SysClient sysClient;
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private StringRedisTemplate redisTemplate;


    @Override
    public CompanyUserDTO queryUser(HttpServletRequest request) {
        String mobile = request.getParameter("username");
        String checkCode = request.getParameter("password");
        Result<CompanyUserDTO> result = sysClient.querySysUser(mobile);
        CompanyUserDTO companyUserDTO = result.getData();
        if (companyUserDTO == null) {
            throw new OAuth2Exception("查询的用户不存在");
        }

        //将用户提交的password作为验证码对待
        String jsonCode = redisTemplate.opsForValue().get(Constant.SMS_LOGIN_KEY_PREFIX + mobile);
        String realCheckCode = (String) JsonUtils.readJsonToMap(jsonCode).get("code");
        if (!realCheckCode.equals(checkCode)) {
            throw new OAuth2Exception("验证码错误");
        }
        //验证通过再删除验证码
        redisTemplate.delete(Constant.SMS_LOGIN_KEY_PREFIX+mobile);
        log.info("验证码验证通过");
        companyUserDTO.setPassword(passwordEncoder.encode(realCheckCode));
        return companyUserDTO;
    }

    /**
     * 是否支持验证码模式
     * @param request 请求对象
     *                提交的认证请求参数auth_type 为 sms,为验证码认证
     * @return
     */
    @Override
    public boolean support(HttpServletRequest request) {
        return "sms".equals(request.getParameter("auth_type"));
    }
}
