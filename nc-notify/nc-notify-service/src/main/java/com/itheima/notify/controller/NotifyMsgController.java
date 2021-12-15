package com.itheima.notify.controller;

import com.itheima.common.vo.Result;
import com.itheima.notify.service.NotifyMsgService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class NotifyMsgController {
    @Autowired
    private NotifyMsgService notifyMsgService;

    @GetMapping("/test")
    public String test() {
        return "test ok";
    }
    /**
     * 查询用户推送的消息记录
     * GET/msg/message
     */
    @GetMapping("/message")
    public Result queryPushMessage(
            @RequestParam(value = "id", required = false) String id,
            @RequestParam(value = "type", required = false) String type,
            @RequestParam(value = "userId", required = false) String sponsor,
            @RequestParam(value = "targetId", required = false) String receiver) {
        return Result.success(notifyMsgService.queryPushMessage(id, type, sponsor, receiver));
    }

    /**
     * 修改推送记录状态
     * PUT/msg/message/status
     */
    @PutMapping("/message/status")
    public Result updateNotifyStatus(@RequestParam("id") String id, @RequestParam("status") Integer status) {
        notifyMsgService.updateNotifyStatus(id, status);
        return Result.success();
    }
}
