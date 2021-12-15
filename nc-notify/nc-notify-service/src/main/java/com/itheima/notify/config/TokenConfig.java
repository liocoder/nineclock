package com.itheima.notify.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.security.oauth2.provider.token.TokenStore;
import org.springframework.security.oauth2.provider.token.store.JwtAccessTokenConverter;
import org.springframework.security.oauth2.provider.token.store.JwtTokenStore;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.stream.Collectors;

@Configuration
public class TokenConfig {

    private String SIGNING_KEY = "itcast_auth";

    /**
     * 使用InMemoryTokenStore，生成一个普通的令牌。
     *
     * @return
     */
    @Bean
    public TokenStore tokenStore() {
        //JWT令牌需要修改为JwtTokenStore
        return new JwtTokenStore(accessTokenConverter());
    }
	/**
	 * 与授权服务器使用共同的密钥进行解析
	 */
    @Bean
    public JwtAccessTokenConverter accessTokenConverter() {
        JwtAccessTokenConverter converter = new JwtAccessTokenConverter();
//        converter.setSigningKey(SIGNING_KEY); //对称秘钥，资源服务器使用该秘钥来验证
        ClassPathResource classPathResource = new ClassPathResource("pub.rs");
        String publicKey = "";
        try {
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(classPathResource.getInputStream()));
            publicKey = bufferedReader.lines().collect(Collectors.joining("\n"));
        } catch (IOException e) {
            e.printStackTrace();
        }
        converter.setVerifierKey(publicKey);
        return converter;
    }
}