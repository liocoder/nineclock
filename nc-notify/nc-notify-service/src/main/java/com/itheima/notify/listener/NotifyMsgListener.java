package com.itheima.notify.listener;

import com.itheima.common.constants.RocketMQConstants;
import com.itheima.notify.dto.NotifyMessage;
import com.itheima.notify.service.NotifyMsgService;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.apache.rocketmq.spring.annotation.RocketMQMessageListener;
import org.apache.rocketmq.spring.core.RocketMQListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RocketMQMessageListener(
        consumerGroup = RocketMQConstants.CONSUMER.NOTIFY_MSG_CONSUMER,
        topic = RocketMQConstants.TOPIC.PUSH_TOPIC_NAME,
        //tag：消息二级分类
        selectorExpression = ""
)
public class NotifyMsgListener implements RocketMQListener<NotifyMessage> {

    @Autowired
    private NotifyMsgService notifyMsgService;

    /**
     * 监听推送消息，向指定设备发送消息
     *
     * @param message
     */
    @SneakyThrows
    @Override
    public void onMessage(NotifyMessage message) {
        try {
            log.info("【推送微服务】，接收到推送消息：{}", message);
            //推送消息
            notifyMsgService.sendPushMsgAndStore(message);
        } catch (Exception e) {
            log.error("【推送微服务】推送消息异常：", e);
        }

    }
}
