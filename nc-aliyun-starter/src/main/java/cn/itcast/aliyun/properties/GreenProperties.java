package cn.itcast.aliyun.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * @author: itheima
 * @create: 2021-05-31 00:44
 */
@Data
@ConfigurationProperties("aliyun.green")
public class GreenProperties {
    /**
     * 账号
     */
    String accessKeyID;
    /**
     * 密钥
     */
    String accessKeySecret;

    /**
     * 场景
     */
    String scenes;
}