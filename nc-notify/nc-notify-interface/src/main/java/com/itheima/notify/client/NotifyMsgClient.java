package com.itheima.notify.client;

import com.itheima.common.vo.Result;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient("notify-service")
public interface NotifyMsgClient {

    /**
     * 修改推送记录状态
     * PUT/msg/message/status
     */
    @PutMapping("/message/status")
    public Result updateNotifyStatus(@RequestParam("id") String docMsgId, @RequestParam("status") Integer status);
}
