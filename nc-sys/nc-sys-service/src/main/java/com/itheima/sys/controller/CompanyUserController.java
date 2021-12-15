package com.itheima.sys.controller;

import com.itheima.common.vo.Result;
import com.itheima.sys.dto.CompanyUserAdminDTO;
import com.itheima.sys.dto.CompanyUserDTO;
import com.itheima.sys.dto.UserJoinCompanyDTO;
import com.itheima.sys.entity.CompanyUser;
import com.itheima.sys.service.ICompanyUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class CompanyUserController {
    @Autowired
    private ICompanyUserService companyUserService;
    /**
     * 查询用户信息
     * GET/sys/user/query
     */
    @GetMapping("/user/query")
    public Result querySysUser(@RequestParam("username") String username) {
        return Result.success(companyUserService.querySysUser(username));
    }

    /**
     * 权限管理-获取子管理员列表
     * GET/sys/company/subAdmin
     */
    @GetMapping("/company/subAdmin")
    public Result companyAdmin() {
        List<CompanyUserDTO> companyUsers= companyUserService.findCompanyAdmins();
        return Result.success(companyUsers);
    }

    /**
     * 添加管理员-给用户设置管理员权限
     * POST/sys/company/subAdmin
     */
    @PostMapping("/company/subAdmin")
    public Result subAdmin(@RequestBody CompanyUserAdminDTO companyUserAdminDTO) {
        companyUserService.subAdmin(companyUserAdminDTO);
        return Result.success();
    }

    /**
     * 用户注册
     * POST/sys/user/register
     */
    @PostMapping("/user/register")
    public Result register(CompanyUser companyUser, @RequestParam("checkcode") String checkcode) {
        return Result.success(companyUserService.register(companyUser, checkcode));
    }

    /**
     * 申请加入职业
     * POST/sys/company/applyJoinCompany
     */
    @PostMapping("/company/applyJoinCompany")
    public Result applyJoinCompany(@RequestBody UserJoinCompanyDTO userJoinCompanyDTO) {
        companyUserService.applyJoinCompany(userJoinCompanyDTO);
        return Result.success("申请已经提交，请耐心等待");
    }

    /**
     * 审核员工加入公司
     * POST/sys/company/allowedJoinCompany
     */
    @PostMapping("/company/allowedJoinCompany")
    public Result allowedJoinCompany(
            @RequestParam("applyUserId") Long applyUserId,
            @RequestParam("approved") Boolean approved,
            @RequestParam(value = "remark", required = false) String remark,
            @RequestParam("notifyMsgId") String notifyMsgId) {
        companyUserService.allowedJonCompany(applyUserId, approved, remark, notifyMsgId);
        return Result.success();
    }

    /**
     * 根据用户id查询用户
     * GET /companyUser/queryByIds
     */
    @GetMapping("/companyUser/queryByIds")
    public Result<List<CompanyUserDTO>> queryCompanyUserByIds(@RequestParam("ids") List<Long> ids) {
        return Result.success(companyUserService.queryUsers(ids));
    }

    /**
     * 查询当前企业下所有员工
     * @return
     */
    @GetMapping("/companyUser/list")
    public Result<List<CompanyUserDTO>> queryAllCompanyUser(){
        return Result.success(companyUserService.queryAllCompanyUser());
    }


    /**
     * 用户积分变动
     * PUT/sys/companyUser/integral
     * @param userId 用户id
     * @param plusFlag  +/-
     * @param integral  分数
     * @return void
     */
    @PutMapping("/companyUser/integral")
    public void updateIntegral(@RequestParam("id") Long userId, @RequestParam("plusFlag") Boolean plusFlag, @RequestParam("integral") Integer integral) {
        companyUserService.updateIntegral(userId, plusFlag, integral);
    }
}
