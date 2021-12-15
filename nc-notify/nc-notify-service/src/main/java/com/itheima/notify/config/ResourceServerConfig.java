package com.itheima.notify.config;

import com.itheima.notify.properties.PermitUrlProperties;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableResourceServer;
import org.springframework.security.oauth2.config.annotation.web.configuration.ResourceServerConfigurerAdapter;

import javax.servlet.http.HttpServletResponse;

/**
 * 资源服务器配置类
 */
@Configuration
@EnableResourceServer  //开启资源服务器
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)  //开启注解方式校验权限
public class ResourceServerConfig extends ResourceServerConfigurerAdapter {
    @Autowired
    private PermitUrlProperties permitUrlProperties;
    /**
     * 配置资源的访问规则（资源有什么权限才能访问资源）
     */
    @Override
    public void configure(HttpSecurity http) throws Exception {
        http.csrf().disable().exceptionHandling()
                .authenticationEntryPoint((request, response, authException) -> response.sendError(HttpServletResponse.SC_UNAUTHORIZED))
                .and()
                    .authorizeRequests()
                    .antMatchers(permitUrlProperties.getPermitUrlList().toArray(new String[]{})).permitAll()  //匿名访问

                //配置其他的rest接口 安全策略
                .anyRequest().authenticated()  //剩余其他的请求 需要认证后方可访问
                .and()
                .httpBasic();
    }
}