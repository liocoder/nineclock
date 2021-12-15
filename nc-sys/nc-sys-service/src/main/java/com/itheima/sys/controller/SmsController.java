package com.itheima.sys.controller;

import com.itheima.common.vo.Result;
import com.itheima.sys.service.SmsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/sms")
public class SmsController {

    @Autowired
    private SmsService smsService;

    /**
     * 短信-发送手机验证码
     * GET/sys/sms/code
     * @param type 1：登录 2:注册 3：重置管理员 4：变更企业管理员
     */
    @RequestMapping("/code")
    public Result sendSmsCode(@RequestParam("mobile") String mobile, @RequestParam(defaultValue = "1") Integer type) {
        smsService.sendSmsCode(mobile, type);
        return Result.successMessage("验证码发送成功，请查收！");
    }
}
