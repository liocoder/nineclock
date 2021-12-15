package com.itheima.sys.service;

import com.itheima.sys.dto.RoleDTO;
import com.itheima.sys.entity.Role;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * <p>
 * 角色表  服务类
 * </p>
 *
 * @author itheima
 * @since 2021-11-23
 */
public interface IRoleService extends IService<Role> {

    List<RoleDTO> findRolesByCompany();

    void editRole(Long id, String roleName, List<String> functionIdList);
}
