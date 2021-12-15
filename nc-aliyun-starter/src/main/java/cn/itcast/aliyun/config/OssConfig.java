package cn.itcast.aliyun.config;

import cn.itcast.aliyun.properties.OssProperties;
import com.aliyun.oss.OSS;
import com.aliyun.oss.OSSClientBuilder;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;


/**
 * @author: itheima
 * @create: 2021-05-28 09:47
 */
@Slf4j
@Configuration
@EnableConfigurationProperties(value = {OssProperties.class}) //启动配置文件
public class OssConfig {

    @Autowired
    private OssProperties ossProperties;

    /**
     * 注册OSS客户端对象
     * @return
     */
    @Bean
    @ConditionalOnMissingBean
    public OSS ossClient() {
        OSS ossClient = new OSSClientBuilder().build(ossProperties.getEndpoint(), ossProperties.getAccessKeyId(), ossProperties.getAccessKeySecret());
        log.info("OSS配置成功，Bucket=" + ossProperties.getBucket() + "，Endpoint=" + ossProperties.getEndpoint());
        return ossClient;
    }
}