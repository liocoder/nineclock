package cn.itcast.aliyun.config;

import cn.itcast.aliyun.properties.GreenProperties;
import com.aliyuncs.DefaultAcsClient;
import com.aliyuncs.IAcsClient;
import com.aliyuncs.exceptions.ClientException;
import com.aliyuncs.profile.DefaultProfile;
import com.aliyuncs.profile.IClientProfile;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author: itheima
 * @create: 2021-05-31 00:48
 */
@Slf4j
@Configuration
@EnableConfigurationProperties(value = {GreenProperties.class}) //启动配置文件
public class GreenConfig {

    @Autowired
    private GreenProperties greenProperties;

    @Bean
    public IAcsClient iAcsClient(){
        try {
            IClientProfile profile = DefaultProfile
                    .getProfile("cn-shanghai", greenProperties.getAccessKeyID(), greenProperties.getAccessKeySecret());
            DefaultProfile
                    .addEndpoint("cn-shanghai", "cn-shanghai", "Green", "green.cn-shanghai.aliyuncs.com");
            IAcsClient client = new DefaultAcsClient(profile);
            return client;
        } catch (ClientException e) {
            e.printStackTrace();
            log.error("Green配置缺失，请补充!");
            return null;
        }
    }
}