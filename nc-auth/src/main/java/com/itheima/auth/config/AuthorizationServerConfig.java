package com.itheima.auth.config;

import com.itheima.auth.integration.exception.WebResponseExceptionTranslator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.config.annotation.configurers.ClientDetailsServiceConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configuration.AuthorizationServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableAuthorizationServer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerEndpointsConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerSecurityConfigurer;
import org.springframework.security.oauth2.provider.ClientDetailsService;
import org.springframework.security.oauth2.provider.client.JdbcClientDetailsService;
import org.springframework.security.oauth2.provider.token.AuthorizationServerTokenServices;
import org.springframework.security.oauth2.provider.token.DefaultTokenServices;
import org.springframework.security.oauth2.provider.token.TokenStore;
import org.springframework.security.oauth2.provider.token.store.JwtAccessTokenConverter;

import javax.sql.DataSource;

/**
 * 认证服务器配置类
 * 开启认证服务器配置类
 *  1. 配置用户详情
 *  2. 配置客户端详情
 */
@Configuration
@EnableAuthorizationServer
public class AuthorizationServerConfig extends AuthorizationServerConfigurerAdapter {

    //令牌存储
    @Autowired
    private TokenStore tokenStore;

    //认证管理器，用于处理一个认证请求--密码模式
    @Autowired
    private AuthenticationManager authenticationManager;

    //为签名验证和解析提供转换器
    @Autowired
    private JwtAccessTokenConverter jwtAccessTokenConverter;


    /**
     * 注册bcrypt加密对象
     * @return
     */
    @Bean
    public PasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder();
    }


    /**
     * 定义用户详情服务：框架会调用该对象 UserDetailsService（封装用户名密码，权限）  查询用户信息用于判断用户认证信息合法。
     */
    /*@Bean
    public UserDetailsService userDetailsService(){
        //在内存中提供自定义的用户名 密码
        InMemoryUserDetailsManager manager = new InMemoryUserDetailsManager();
        //在内存中自定义用户名称：jack 密码：jack 权限：p1
        manager.createUser(User.withUsername("third").password(passwordEncoder().encode("third")).authorities("user:query").build());

        manager.createUser(User.withUsername("jack").password(passwordEncoder().encode("jack")).authorities("p1").build());
        manager.createUser(User.withUsername("rose").password(passwordEncoder().encode("rose")).authorities("p2").build());
        return manager;
    }*/

    @Autowired
    private DataSource dataSource;

    @Bean
    public ClientDetailsService clientDetailsService(){
        return new JdbcClientDetailsService(dataSource);
    }

    /**
     * 配置客户端详情：移动端、web端，第三方应用 定义客户端ID，秘钥
     * third_client：提供授权码模式测试客户端  适用其他的第三方系统
     * app_client，pc_client 密码模式     适用受信任客户端
     * @param clients
     * - clientId：（必须的）用来标识客户的Id（理解为第三方应用账户）
     * - secret：（需要值得信任的客户端）客户端安全码，如果有的话。
     * - scope：用来限制客户端的访问范围，可选值（read,write,all）如果为空（默认）的话，那么客户端拥有全部的访问范围。
     * - authorizedGrantTypes：此客户端可以使用的授权类型，默认为空。 可选值（ **`authorization_code`** ， **`password`,`implicit,client_credentials，refresh_token`** ）
     * - authorities：此客户端可以使用的权限（基于Spring Security authorities）。
     * - autoApproveScopes:  设置是否自动授权
     * - redirectUris 授权码模式中重定向地址
     * @throws Exception
     */
    @Override
    public void configure(ClientDetailsServiceConfigurer clients) throws Exception {
        //客户端信息暂存储在内存中,也可改为security提供的表存储
        //授权码模式下需要的客户端信息如下
        /*clients.inMemory()
                .withClient("third_client")  //客户端标识
                    .secret(passwordEncoder().encode("third_secret"))  //客户端的秘钥
                    .authorizedGrantTypes("authorization_code")
                    .autoApprove(true)
                    .scopes("all")
                    //.autoApprove("all")
                    .redirectUris("http://www.baidu.com")  //设置回调地址

                //密码模式需要的客户端信息
                .and()
                    .withClient("app_client")  //客户端标识
                    .secret(passwordEncoder().encode("app_secret"))  //客户端的秘钥
                    .authorizedGrantTypes("password", "refresh_token")
                    .scopes("all") //设置回调地址

                .and()
                    .withClient("pc_client")
                    .secret(passwordEncoder().encode("pc_secret"))
                    .authorizedGrantTypes("password", "refresh_token")
                    .scopes("all");*/
        clients.withClientDetails(clientDetailsService());
    }


    /**
     * 令牌服务支持
     */
    @Bean
    public AuthorizationServerTokenServices tokenService() {
        DefaultTokenServices service = new DefaultTokenServices();
        //是否支持刷新令牌
        service.setSupportRefreshToken(true);

        //指定令牌产生加强对象(改为jwt令牌)
        service.setTokenEnhancer(jwtAccessTokenConverter);

        //令牌存储
        service.setTokenStore(tokenStore);
        // 令牌有效期单位秒，默认默认12小时  设置为1周
        service.setAccessTokenValiditySeconds(604800);
        // 刷新令牌默认单位秒  默认30天 设置为3周
        service.setRefreshTokenValiditySeconds(1814400);
        return service;
    }

    //异常转换器
    @Autowired
    private WebResponseExceptionTranslator webResponseExceptionTranslator;
    /**
     * 注入令牌策略
     * @param endpoints
     */
    @Override
    public void configure(AuthorizationServerEndpointsConfigurer endpoints) {
        endpoints.authenticationManager(authenticationManager)
                .exceptionTranslator(webResponseExceptionTranslator)
                .tokenServices(tokenService())
        ;
    }


    /**
     * **令牌端点的安全约束，对密码模式表单提交允许**
     * @param security
     */
    @Override
    public void configure(AuthorizationServerSecurityConfigurer security) {
        security.allowFormAuthenticationForClients();  //支持密码模式下表单登录
    }
}
