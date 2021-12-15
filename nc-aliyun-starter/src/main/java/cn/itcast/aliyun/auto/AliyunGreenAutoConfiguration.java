package cn.itcast.aliyun.auto;

import cn.itcast.aliyun.config.GreenConfig;
import cn.itcast.aliyun.template.AliyunGreenTemplate;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

/**
 * 自动配置类
 *
 * @author itheima
 */
@Configuration
@Import(GreenConfig.class)
@ConditionalOnProperty(prefix = "aliyun.green", value = "enable", havingValue = "true")
public class AliyunGreenAutoConfiguration {


    @Bean
    public AliyunGreenTemplate aliyunGreenTemplate() {
        return new AliyunGreenTemplate();
    }
}