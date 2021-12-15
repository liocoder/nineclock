package com.itheima.auth.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.security.oauth2.provider.token.TokenStore;
import org.springframework.security.oauth2.provider.token.store.JwtAccessTokenConverter;
import org.springframework.security.oauth2.provider.token.store.JwtTokenStore;
import org.springframework.security.oauth2.provider.token.store.KeyStoreKeyFactory;

import java.security.KeyPair;

/**
 * 令牌存储配置类 tokenStore
 */
@Configuration
public class TokenConfig {

    //对称加密采用秘钥: 加密和解密使用同一密钥
    private static final String secret = "itcast_auth";

    @Bean
    public JwtAccessTokenConverter jwtAccessTokenConverter(){

        JwtAccessTokenConverter jwtAccessTokenConverter = new JwtAccessTokenConverter();
//        jwtAccessTokenConverter.setSigningKey(secret);
        KeyPair keyPair = new KeyStoreKeyFactory(new ClassPathResource("oauth2.jks"), "itcast".toCharArray()).getKeyPair("oauth2");
        jwtAccessTokenConverter.setKeyPair(keyPair);
        return jwtAccessTokenConverter;
    }

    /**
     * 默认：InMemoryTokenStore，内存中生成一个普通的令牌。
     * - InMemoryTokenStore 将OAuth2AccessToken保存在内存(默认)
     * - JdbcTokenStore 将OAuth2AccessToken保存在数据库
     * - JwtTokenStore 将OAuth2AccessToken保存到JSON Web Token
     * - RedisTokenStore 将OAuth2AccessToken保存到Redis
     */
    @Bean
    public TokenStore tokenStore() {
        return new JwtTokenStore(jwtAccessTokenConverter());
    }
}