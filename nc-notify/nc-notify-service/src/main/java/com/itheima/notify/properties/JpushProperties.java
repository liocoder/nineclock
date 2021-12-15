package com.itheima.notify.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Data
@Component
@ConfigurationProperties("nc.jpush")
public class JpushProperties {

    //极光平台应用的唯一标识
    private String appKey;
    //用于服务器端 API 调用时与 AppKey 配合使用达到鉴权的目的
    private String masterSecret;

}