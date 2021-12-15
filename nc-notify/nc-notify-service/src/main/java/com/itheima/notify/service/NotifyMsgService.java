package com.itheima.notify.service;

import com.itheima.notify.dto.NotifyMessage;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;

import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
public class NotifyMsgService {
    @Autowired
    private JPushService jPushService;

    @Autowired
    private MongoTemplate mongoTemplate;

    /**
     * 给手机设备推送消息，将消息保存到mongodb
     */
    public void sendPushMsgAndStore(NotifyMessage message) throws Exception {
        //1. 给手机设备推送消息
        boolean b = jPushService.sendNotificationByAlias(message.getTitle(), message.getContent(), message.getTargets(), null);
        if (b) {
            log.info("调用极光推送API成功：");
        }
        // 2.将消息存储在MongoDB
        mongoTemplate.save(message, "notifyMessage");
    }

    /**
     *
     * @param id
     * @param type
     * @param sponsor 发起方
     * @param receiver  接收方
     * @return
     */
    public List<NotifyMessage> queryPushMessage(String id, String type, String sponsor, String receiver) {
        //封装条件
        Query query = new Query();
        if (StringUtils.isNotBlank(id)) {
            query.addCriteria(Criteria.where("_id").is(new ObjectId(id)));
        }
        if (StringUtils.isNotBlank(type)) {
            query.addCriteria(Criteria.where("messageType").is(type));
        }
        if (StringUtils.isNotBlank(sponsor)) {
            query.addCriteria(Criteria.where("applyUserId").is(Long.valueOf(sponsor)));
        }
        if (StringUtils.isNotBlank(receiver)) {
            //审核者id
            Criteria criteria = Criteria.where("approveUserId").is(Long.valueOf(receiver));
            //target手机号别名
            Criteria criteria1 = Criteria.where("targets").in(receiver);
            //条件为或者
            query.addCriteria(new Criteria().orOperator(criteria, criteria1));
        }
        return mongoTemplate.find(query, NotifyMessage.class, "notifyMessage");
    }

    public void updateNotifyStatus(String id, Integer status) {
        //1. 构建查询对象
        Query query = new Query();
        query.addCriteria(Criteria.where("_id").is(new ObjectId(id)));
        Update update = new Update();
        //已读
        update.set("useStatus", 1);
        //2：审核通过/3：拒绝
        update.set("approveStatus", status);
        mongoTemplate.updateFirst(query, update, "notifyMessage");
    }
}
