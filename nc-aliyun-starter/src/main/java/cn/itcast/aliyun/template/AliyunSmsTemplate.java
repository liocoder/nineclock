package cn.itcast.aliyun.template;

import cn.itcast.aliyun.properties.SmsProperties;
import com.aliyun.dysmsapi20170525.models.SendSmsRequest;
import com.aliyun.dysmsapi20170525.models.SendSmsResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * @author itheima
 * 发送短信模板对象
 */

@Slf4j
public class AliyunSmsTemplate {

    @Autowired
    private SmsProperties smsProperties;

    @Autowired
    private com.aliyun.dysmsapi20170525.Client client;

    /**
     * 发送短信工具方法
     *
     * @param phone
     * @param templateCode
     * @param paramJson
     */
    public void sendSms(String phone, String templateCode, String paramJson) throws  Exception {
        SendSmsRequest sendSmsRequest = new SendSmsRequest()
        // 复制代码运行请自行打印 API 的返回值
                .setPhoneNumbers(phone)
                .setSignName(smsProperties.getSignName())
                .setTemplateCode(templateCode)
                .setTemplateParam(paramJson);
        SendSmsResponse sendSmsResponse = client.sendSms(sendSmsRequest);
        log.info("向" + phone + "发送短信结果：{}", sendSmsResponse.toString());
        if (sendSmsResponse.getBody().getCode() != null && sendSmsResponse.getBody().getCode().equals("OK")) {
            return;
        }
    }
}