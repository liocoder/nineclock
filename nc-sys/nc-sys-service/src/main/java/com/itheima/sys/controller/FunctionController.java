package com.itheima.sys.controller;

import com.itheima.common.vo.Result;
import com.itheima.sys.service.IFunctionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 权限管理
 */
@RestController
public class FunctionController {

    @Autowired
    private IFunctionService functionService;

    /**
     * 权限管理-获取当前企业权限列表
     * GET/sys/company/config/permissions
     */
    @RequestMapping("/company/config/permissions")
    public Result findPermissionsByCompany() {
        return Result.success(functionService.findPermissionsByCompany());
    }
}
