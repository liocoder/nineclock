package com.itheima.sys.controller;

import com.itheima.common.enums.ResponseEnum;
import com.itheima.common.exception.auth.NcException;
import com.itheima.common.vo.Result;
import com.itheima.sys.entity.UserDomain;
import com.itheima.sys.service.UserDomainService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

/**
 * 测试控制器
 * @author itheima
 */
@RestController
public class TestController {
    @Autowired
    private UserDomainService userDomainService;

    @GetMapping("/test")
    public Result<String> hello(){
        return new Result<>(true, 200, "请求成功！", "hello nineclock!");
    }

    /**
     * 全局异常模拟方法
     * save user
     */
    //拦截器方式配置访问权限，jack登录可以访问
    @PostMapping("/test/user")
    public Result<UserDomain> save(@RequestBody UserDomain userDomain) {
        if (userDomain.getAge() == null) {
            throw new NcException(ResponseEnum.AGE_NOT_NULL);
        }
        userDomain = userDomainService.saveUserDomain(userDomain);
        return Result.success("保存成功", userDomain);
    }

    /**
     * 认证测试方法
     */
    //@PreAuthorize("hasAuthority('p2')")  //用户访问该接口（资源）必须有p2权限 rose登录后才能访问
    @GetMapping("/test/auth")
    public Result auth() {
        return Result.success("认证通过");
    }
}