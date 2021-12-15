package com.itheima.test;

import com.itheima.NcNotifyApplication;
import com.itheima.notify.service.JPushService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.*;

@SpringBootTest(classes = NcNotifyApplication.class)
@RunWith(SpringRunner.class)
public class JPushTest {

    @Autowired
    private JPushService jPushService;

    @Test
    public void testSendByAlias() throws Exception {
        List<String> targets = Arrays.asList("13200132000");
        Map<String, String> extras = new HashMap<>();
        extras.put("videoUrl", "http://www.movies.com?id=12");
        boolean flag = jPushService.sendNotificationByAlias("午间新闻", "今日天津小雨-大雾，出行请做好防护", targets, extras);
        System.out.println(flag);
    }
}
