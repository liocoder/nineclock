package com.itheima.auth.integration.exception;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import org.springframework.security.oauth2.common.exceptions.OAuth2Exception;

/**
* @Description 自定义异常格式
* @Param
* @Return
*/
@JsonSerialize(using = MyOAuthExceptionJacksonSerializer.class)
public class MyOAuth2Exception extends OAuth2Exception {
    public MyOAuth2Exception(String msg, Throwable t) {
        super(msg, t);

    }
    public MyOAuth2Exception(String msg) {
        super(msg);
    }
}
