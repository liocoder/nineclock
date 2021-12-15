package com.itheima.sys.service;

import cn.itcast.aliyun.template.AliyunSmsTemplate;
import com.itheima.common.enums.ResponseEnum;
import com.itheima.common.exception.auth.NcException;
import com.itheima.common.util.JsonUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.RandomStringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.concurrent.TimeUnit;

import static com.itheima.common.constants.Constant.*;

@Slf4j
@Service
public class SmsService {

    @Autowired
    private AliyunSmsTemplate smsTemplate;
    @Autowired
    private ICompanyUserService userService;
    @Autowired
    private StringRedisTemplate redisTemplate;
    //发送短信验证码
    public void sendSmsCode(String mobile, Integer type) {
        //1. 检验手机号是否存在
        Boolean exists = userService.checkMobile(mobile);
        if (type.equals(2) && exists) {
            //说明注册的同时，手机号已存在
            throw new NcException(ResponseEnum.USER_MOBILE_EXISTS);
        } else {
            //说明其他业务，登录/变更企业管理员
            if (!exists && !type.equals(2)) {
                throw new NcException(400, "请先注册！");
            }
        }
        //2. 判断验证码是否在有效期
        Boolean flag = redisTemplate.hasKey(getPrefix(type) + mobile);
        if (flag) {
            throw new NcException(400, "验证码在有效期内，请勿重复点击");
        }
        //3. 发送短信
        String checkcode = RandomStringUtils.randomNumeric(4);
        HashMap<String, String> paramsMap = new HashMap<>();
        paramsMap.put("code", checkcode);
        //aliyunSmsTemplate.sendSms(mobile, "SMS_119087143", JsonUtils.toJsonStr(paramsMap));
        //4. 将验证码保存到redis中
        redisTemplate.opsForValue().set(getPrefix(type) + mobile, JsonUtils.toJsonStr(paramsMap), 5, TimeUnit.MINUTES);
        log.info("发送短信验证码成功，mobile:{}, checkcode:{}", mobile, checkcode);
    }

    /**
     * 根据type类型获取redis key 前缀值
     *
     * @param type
     * @return
     */
    private static String getPrefix(Integer type) {
        String prefix = "";
        switch (type) {
            case 1:
                prefix = SMS_LOGIN_KEY_PREFIX;
                break;
            case 2:
                prefix = SMS_REGISTER_KEY_PREFIX;
                break;
            case 3:
                prefix = SMS_RESET_KEY_PREFIX;
                break;
            case 4:
                prefix = SMS_CHANGE_MANAGER_KEY_PREFIX;
                break;
            default:
                throw new NcException(ResponseEnum.INVALID_PARAM_ERROR);
        }
        return prefix;
    }
}
