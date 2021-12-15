package com.itheima.auth.integration.exception;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.oauth2.common.exceptions.OAuth2Exception;
import org.springframework.security.oauth2.provider.error.DefaultWebResponseExceptionTranslator;
import org.springframework.stereotype.Component;

/**
 * 异常转换器
 */
@Component
@Slf4j
public class WebResponseExceptionTranslator extends DefaultWebResponseExceptionTranslator {

    /**
     * @param e spring security内部异常
     * @return 经过处理的异常信息
     * @throws Exception 通用异常
     */
    @Override
    public ResponseEntity<OAuth2Exception> translate(Exception e) throws Exception {
        log.error("认证失败了 : " + e.getMessage());
        MyOAuth2Exception exception = new MyOAuth2Exception(e.getMessage());
        return super.translate(exception);
    }
}