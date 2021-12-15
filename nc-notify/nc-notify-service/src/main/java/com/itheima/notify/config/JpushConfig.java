package com.itheima.notify.config;

import cn.jpush.api.JPushClient;
import com.itheima.notify.properties.JpushProperties;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class JpushConfig {

    @Autowired
    private JpushProperties prop;

    @Bean
    public JPushClient jPushClient(){
        return new JPushClient(prop.getMasterSecret(), prop.getAppKey());
    }
}