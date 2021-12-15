package com.itheima.sys.service.impl;

import com.itheima.sys.entity.Department;
import com.itheima.sys.mapper.DepartmentMapper;
import com.itheima.sys.service.IDepartmentService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 部门表 服务实现类
 * </p>
 *
 * @author itheima
 * @since 2021-11-23
 */
@Service
public class DepartmentServiceImpl extends ServiceImpl<DepartmentMapper, Department> implements IDepartmentService {

}
