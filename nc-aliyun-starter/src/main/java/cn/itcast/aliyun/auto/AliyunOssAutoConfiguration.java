package cn.itcast.aliyun.auto;

import cn.itcast.aliyun.config.OssConfig;
import cn.itcast.aliyun.template.AliyunOssTemplate;
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
@Import(OssConfig.class)
@ConditionalOnProperty(prefix = "aliyun.oss", value = "enable", havingValue = "true")
public class AliyunOssAutoConfiguration {


    @Bean
    public AliyunOssTemplate aliyunOssTemplate() {
        return new AliyunOssTemplate();
    }

}