package com.itheima.sys.service;

import com.itheima.sys.entity.Function;
import com.baomidou.mybatisplus.extension.service.IService;
import com.itheima.sys.entity.Role;

import java.util.List;

/**
 * <p>
 * 权限表 服务类
 * </p>
 *
 * @author itheima
 * @since 2021-11-23
 */
public interface IFunctionService extends IService<Function> {

    List<Function> findFunctionByRole(Role role);


    Object findPermissionsByCompany();
}
