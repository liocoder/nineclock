package cn.itcast.aliyun.config;

import cn.itcast.aliyun.properties.SmsProperties;
import com.aliyun.teaopenapi.models.Config;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * 注册一些发送短信相关必要的Bean对象 ：Client对象
 *
 * @author: itheima
 * @create: 2021-11-03 10:20
 */
@Slf4j
@Configuration
@EnableConfigurationProperties({SmsProperties.class})
public class SMSConfig {

    @Autowired
    private SmsProperties smsProperties;

    @Bean
    public com.aliyun.dysmsapi20170525.Client client() {
        try {
            Config config = new Config()
                    // 您的AccessKey ID
                    .setAccessKeyId(smsProperties.getAccessKeyID())
                    // 您的AccessKey Secret
                    .setAccessKeySecret(smsProperties.getAccessKeySecret());
            // 访问的域名
            config.endpoint = "dysmsapi.aliyuncs.com";
            return new com.aliyun.dysmsapi20170525.Client(config);
        } catch (Exception e) {
            log.error("SMS服务自动装配失败：{}", e.getMessage());
            throw new RuntimeException("SMS服务自动装配失败");
        }
    }

}