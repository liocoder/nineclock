package com.itheima.sys.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.itheima.common.threadlocals.UserHolder;
import com.itheima.sys.entity.Function;
import com.itheima.sys.entity.Role;
import com.itheima.sys.mapper.FunctionMapper;
import com.itheima.sys.service.IFunctionService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.apache.logging.log4j.util.Strings;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

/**
 * <p>
 * 权限表 服务实现类
 * </p>
 *
 * @author itheima
 * @since 2021-11-23
 */
@Service
public class FunctionServiceImpl extends ServiceImpl<FunctionMapper, Function> implements IFunctionService {

    @Override
    public List<Function> findFunctionByRole(Role role) {
        List<Function> functions = new ArrayList<>();
        String functionIds = role.getFunctionIds();
        if (!Strings.isBlank(functionIds)) {
            String[] split = functionIds.split(",");
            Collection<Function> functionList = this.listByIds(Arrays.asList(split));
            if (!CollectionUtils.isEmpty(functionList)) {
                functions.addAll(functionList);
            }
        }
        return functions;

    }

    @Override
    public Object findPermissionsByCompany() {
        Long companyId = UserHolder.getCompanyId();
        LambdaQueryWrapper<Function> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Function::getCompanyId, companyId);
        return this.list(wrapper);
    }
}
