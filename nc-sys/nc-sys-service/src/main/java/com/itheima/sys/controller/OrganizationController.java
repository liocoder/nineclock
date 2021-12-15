package com.itheima.sys.controller;

import com.itheima.common.vo.PageResult;
import com.itheima.common.vo.Result;
import com.itheima.sys.entity.CompanyUser;
import com.itheima.sys.service.OrganizationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

/**
 * 组织架构
 */
@RestController
public class OrganizationController {

    @Autowired
    private OrganizationService organizationService;

    /**
     * 组织架构-分页获取部门成员简单列表（重置管理员使用
     * GET/sys/organization/members/simple
     */
    @GetMapping("/organization/members/simple")
    public Result departmentMember(@RequestParam(value = "page", defaultValue = "1") Integer page,
                                    @RequestParam(value = "pageSize", defaultValue = "10")Integer pageSize,
                                    @RequestParam(value = "id", required = false) Long departId) {
        PageResult<CompanyUser> pageResult = organizationService.departmentMember(page, pageSize, departId);
        return Result.success(pageResult);
    }

    /**
     * 获取组织架构列表
     * GET/sys/organization/department
     */
    @GetMapping("/organization/department")
    public Result organizationTree() {
        return Result.success(organizationService.organizationTree());
    }

    /**
     * 获取部门成员列表
     * GET/sys/organization/members
     */
    @GetMapping("/organization/members")
    public Result deptMember(@RequestParam(value = "departmentId", required = false) Long departmentId,
                             @RequestParam(value = "page", defaultValue = "1") Integer page,
                             @RequestParam(value = "pageSize", defaultValue = "20") Integer pageSize,
                             @RequestParam(value = "keyword", required = false) String keyword) {
        return Result.success(organizationService.queryDeptMember(page, pageSize, departmentId, keyword));
    }

    /**
     * 组织架构-excel报表-批量导入员工
     * POST/organization/members/uploadXls
     */
    @PostMapping("/organization/members/uploadXls")
    public Result uploadXls(MultipartFile file) {
        organizationService.uploadXls(file);
        return Result.success("批量导入成功");
    }
}
