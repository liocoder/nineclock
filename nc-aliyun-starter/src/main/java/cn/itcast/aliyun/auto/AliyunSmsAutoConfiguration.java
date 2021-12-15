package cn.itcast.aliyun.auto;

import cn.itcast.aliyun.config.SMSConfig;
import cn.itcast.aliyun.template.AliyunSmsTemplate;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;

/**
 * 阿里云短信服务自动配置类
 * @author: itheima
 * @create: 2021-07-27 10:14
 */
@Import(SMSConfig.class)
@ConditionalOnProperty(prefix = "aliyun.sms", value = "enable", havingValue = "true")  //yml配置文件中必须满足条件
public class AliyunSmsAutoConfiguration {

    @Bean
    public AliyunSmsTemplate aliyunSmsTemplate(){
        return new AliyunSmsTemplate();
    }
}