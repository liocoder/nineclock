package com.itheima.sys.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.itheima.common.enums.ResponseEnum;
import com.itheima.common.exception.auth.NcException;
import com.itheima.common.threadlocals.UserHolder;
import com.itheima.common.util.BeanHelper;
import com.itheima.sys.dto.RoleDTO;
import com.itheima.sys.entity.Function;
import com.itheima.sys.entity.Role;
import com.itheima.sys.mapper.RoleMapper;
import com.itheima.sys.service.IFunctionService;
import com.itheima.sys.service.IRoleService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.util.List;
import java.util.stream.Collectors;

/**
 * <p>
 * 角色表  服务实现类
 * </p>
 *
 * @author itheima
 * @since 2021-11-23
 */
@Service
public class RoleServiceImpl extends ServiceImpl<RoleMapper, Role> implements IRoleService {

    @Autowired
    private IFunctionService functionService;

    @Override
    public List<RoleDTO> findRolesByCompany() {
        //根据企业id查询企业role
        Long companyId = UserHolder.getCompanyId();
        LambdaQueryWrapper<Role> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Role::getCompanyId, companyId);
        List<Role> roles = this.list(wrapper);
        //封装角色包含权限
        if (CollectionUtils.isEmpty(roles)) {
            return null;
        }
        return roles.stream().map(role -> {
            //查询角色下包含的权限列表
            RoleDTO roleDTO = BeanHelper.copyProperties(role, RoleDTO.class);
            List<Function> functions = functionService.findFunctionByRole(role);
            roleDTO.setSysFunctionAbbrList(functions);
            return roleDTO;
        }).collect(Collectors.toList());
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void editRole(Long id, String roleName, List<String> functionIdList) {
        Role role = new Role();
        role.setId(id);
        role.setRoleName(roleName);

        if (!CollectionUtils.isEmpty(functionIdList)) {
//            String collect = functionIdList.stream().collect(Collectors.joining(","));
//            role.setFunctionIds(collect);
            String ids = StringUtils.join( functionIdList,",");//lang3
            role.setFunctionIds(ids);
            //role.setFunctionIds(String.ids(",", functionIdList));
        }

        boolean b = this.updateById(role);
        if (!b) {
            throw new NcException(ResponseEnum.UPDATE_OPERATION_FAIL);
        }

        /*StringBuilder sb = new StringBuilder();
        for (String functionId : functionIdList) {
            sb.append(functionId + ",");
        }
        String functionIds = sb.length() > 0 ? sb.deleteCharAt(sb.length() - 1).toString() : "";
        role.setFunctionIds(functionIds.trim());
        this.updateById(role);*/
    }

}
