package com.itheima;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication
@EnableDiscoveryClient
@EnableFeignClients //调用方要开启feign组件功能
public class NcAuthApplication {
    public static void main(String[] args) {
        SpringApplication.run(NcAuthApplication.class, args);
    }
}