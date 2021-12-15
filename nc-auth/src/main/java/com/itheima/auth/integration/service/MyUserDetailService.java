package com.itheima.auth.integration.service;

import com.itheima.auth.integration.authenticator.LoginHandler;
import com.itheima.common.threadlocals.UserHolder;
import com.itheima.common.util.BeanHelper;
import com.itheima.common.util.JsonUtils;
import com.itheima.common.vo.UserInfo;
import com.itheima.sys.dto.CompanyUserDTO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.oauth2.common.exceptions.OAuth2Exception;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
public class MyUserDetailService implements UserDetailsService {

    /**
     * 将IOC容器中LoginHandler类型对象注入到集合中
     */
    @Autowired
    private List<LoginHandler> loginHandlerList;


    /**
     * 框架调用该方法获取用户正确信息，用于校验客户端提交数据
     * @param username
     * @return
     * @throws UsernameNotFoundException
     */
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        log.info("自定义用户详情服务-loadUserByUsername方法执行了");
        //获取request
        RequestAttributes requestAttributes = RequestContextHolder.getRequestAttributes();
        ServletRequestAttributes servletRequestAttributes = (ServletRequestAttributes) requestAttributes;
        HttpServletRequest request = servletRequestAttributes.getRequest();
        //根据auth_type，选择自定义认证器对象,返回自己业务用户信息
        LoginHandler loginHandler = chooseLoginHandler(request);
        log.info("客户端提交的验证方式：{}", loginHandler);
        if (loginHandler == null) {
            throw new OAuth2Exception("暂不支持的验证类型");
        }
        //根据不同的验证方式去查询用户信息
        CompanyUserDTO companyUserDTO = loginHandler.queryUser(request);

        //获取用户权限
        List<GrantedAuthority> grantedAuthorities = getUserGrantedAuthority(companyUserDTO);
        if (CollectionUtils.isEmpty(grantedAuthorities)) {
            //查询的用户没有权限是配置 游客
            grantedAuthorities.add(new SimpleGrantedAuthority("ROLE_USER_TOURIST"));
        }
        //token中封装userInfo
        UserInfo userInfo = BeanHelper.copyProperties(companyUserDTO, UserInfo.class);
        //响应给客户端的result中需要封装用户信息，所有将用户信息封装到UserHolder中，增强TokenEndpoint中的postAccessToken方法，在返回结果中添加用户信息
        UserHolder.setUser(userInfo);
        //封装框架要求的用户信息
        return new User(JsonUtils.toJsonStr(userInfo), companyUserDTO.getPassword(), grantedAuthorities);
    }

    /**
     * 获取用户权限
     * @param companyUserDTO
     * @return
     */
    private List<GrantedAuthority> getUserGrantedAuthority(CompanyUserDTO companyUserDTO) {
        List<GrantedAuthority> grantedAuthorities = new ArrayList<>();
        //处理角色
        if (!CollectionUtils.isEmpty(companyUserDTO.getSysRoles())) {
            List<SimpleGrantedAuthority> collect = companyUserDTO.getSysRoles().stream().map(role -> {
                return new SimpleGrantedAuthority(role.getRoleName());
            }).collect(Collectors.toList());
            grantedAuthorities.addAll(collect);
        }
        //处理权限
        if (!CollectionUtils.isEmpty(companyUserDTO.getSysRoles())) {
            List<SimpleGrantedAuthority> collect = companyUserDTO.getSysFunctions().stream().map(function -> {
                return new SimpleGrantedAuthority(function.getName());
            }).collect(Collectors.toList());
            grantedAuthorities.addAll(collect);
        }
        return grantedAuthorities;
    }


    /**
     * 根据客户端提交的验证参数决定使用那种loginHandler
     * @param request
     * @return
     */
    private LoginHandler chooseLoginHandler(HttpServletRequest request) {

        for (LoginHandler loginHandler : loginHandlerList) {
            if (loginHandler.support(request)) {
                return loginHandler;
            }
        }
        return null;
    }
}
