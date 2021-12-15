package com.itheima.gateway.config;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.web.cors.reactive.CorsUtils;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.server.WebFilter;
import org.springframework.web.server.WebFilterChain;
import reactor.core.publisher.Mono;

/**
 * 全局过滤器：跨域问题处理
 */
//@Configuration
public class CorsConfig implements WebFilter {

    //配置CORS规范
    //允许跨域访问的头信息
    private static final String ALLOWED_HEADERS = "*";
    //允许跨域访问的域名
    private static final String ALLOWED_ORIGIN = "*";
    //允许跨域访问的方法
    private static final String ALLOWED_METHODS = "*";
    //是否允许携带cookie
    private static final String ALLOWEB_COOKIE = "true";
    //设置预检请求有效期
    private static final String MAX_AGE = "1800";

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, WebFilterChain chain) {
        //获取到请求头信息
        ServerHttpRequest request = exchange.getRequest();
        // ORIGN!= null
        if (CorsUtils.isCorsRequest(request)) {
            //设置响应头
            ServerHttpResponse response = exchange.getResponse();
            HttpHeaders headers = response.getHeaders();
            headers.set("Access-Control-Allow-Origin", ALLOWED_ORIGIN);
            headers.add("Access-Control-Allow-Methods", ALLOWED_METHODS);
            headers.add("Access-Control-Max-Age", MAX_AGE);
            headers.add("Access-Control-Allow-Headers", ALLOWED_HEADERS);
            headers.add("Access-Control-Allow-Credentials", ALLOWEB_COOKIE);
            if(request.getMethod().equals(HttpMethod.OPTIONS)){
                response.setStatusCode(HttpStatus.OK);
                return Mono.empty();
            }
        }

        return chain.filter(exchange);
    }
}
