package com.itheima.sys.controller;

import com.itheima.common.vo.Result;
import com.itheima.sys.dto.RoleDTO;
import com.itheima.sys.service.IRoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

/**
 * 角色管理
 */
@RestController
public class RoleController {

    @Autowired
    private IRoleService roleService;
    /**
     * 权限管理-获取当前企业角色列表
     * GET/sys/company/role
     */
    @GetMapping("/company/role")
    public Result<List<RoleDTO>> findRolesByCompany() {
        List<RoleDTO> roleDTOList = roleService.findRolesByCompany();
        return Result.success(roleDTOList);
    }

    /**
     * 权限管理-编辑角色
     * PUT/sys/company/role
     */
    @PutMapping("/company/role")
    public Result editRole(@RequestBody Map map) {
        Long id = Long.parseLong(map.get("id").toString());
        String roleName = (String) map.get("roleName");
        List<String> functionIdList = (List<String>) map.get("functionIdList");
        roleService.editRole(id, roleName, functionIdList);
        return Result.successMessage("编辑角色成功");
    }
}
