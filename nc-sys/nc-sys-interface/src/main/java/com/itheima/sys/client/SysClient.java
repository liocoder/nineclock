package com.itheima.sys.client;

import com.itheima.common.vo.Result;
import com.itheima.sys.dto.CompanyUserDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@FeignClient("sys-service")
public interface SysClient {

    @GetMapping("/user/query")
    public Result<CompanyUserDTO> querySysUser(@RequestParam("username") String username);

    @GetMapping("/companyUser/queryByIds")
    public Result<List<CompanyUserDTO>> queryCompanyUserByIds(@RequestParam("ids") List<Long> ids);

    /**
     * 查询当前企业下所有员工
     * @return
     */
    @GetMapping("/companyUser/list")
    public Result<List<CompanyUserDTO>> queryAllCompanyUser();

    /**
     * 用户积分变动
     * PUT/sys/companyUser/integral
     * @param userId 用户id
     * @param plusFlag  +/-
     * @param integral  分数
     * @return void
     */
    @PutMapping("/companyUser/integral")
    public void updateIntegral(@RequestParam("id") Long userId, @RequestParam("plusFlag") Boolean plusFlag, @RequestParam("integral") Integer integral);
    }
