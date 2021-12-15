package com.itheima.auth.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;

/**
 * security安全配置类
 */
@Configuration
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {
    
      /**
     * 密码模式下需要：用户认证时需要的认证管理和用户信息来源
     * @return
     * @throws Exception
     */
    @Bean
    @Override
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }
   

}