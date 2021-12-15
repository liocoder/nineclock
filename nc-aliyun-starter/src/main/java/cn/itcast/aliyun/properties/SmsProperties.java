package cn.itcast.aliyun.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * 短信服务配置文件
 */
@Data
@ConfigurationProperties("aliyun.sms")
public class SmsProperties {

    /**
     * 账号
     */
    String accessKeyID;
    /**
     * 密钥
     */
    String accessKeySecret;
    /**
     * 短信签名
     */
    String signName;
    /**
     * 发送短信请求的域名
     */
    String domain;

}