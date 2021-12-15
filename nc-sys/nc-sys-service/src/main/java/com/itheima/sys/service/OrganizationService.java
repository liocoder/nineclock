package com.itheima.sys.service;

import com.alibaba.excel.EasyExcel;
import com.alibaba.excel.ExcelReader;
import com.alibaba.excel.read.metadata.ReadSheet;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.itheima.common.threadlocals.UserHolder;
import com.itheima.common.util.BeanHelper;
import com.itheima.common.vo.PageResult;
import com.itheima.sys.dto.CompanyUserDTO;
import com.itheima.sys.dto.DepartmentDTO;
import com.itheima.sys.easyexcel.CompanyUserDataListener;
import com.itheima.sys.entity.CompanyUser;
import com.itheima.sys.entity.Department;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Service
public class OrganizationService {

    @Autowired
    private ICompanyUserService userService;
    @Autowired
    private IDepartmentService departmentService;

    public PageResult<CompanyUser> departmentMember(Integer page, Integer pageSize, Long departId) {
        Long companyId = UserHolder.getCompanyId();
        //构架分页对象
        IPage<CompanyUser> iPage = new Page<>(page, pageSize);
        //构造查询条件
        LambdaQueryWrapper<CompanyUser> wrapper = new LambdaQueryWrapper<>();
        //查询指定列
        wrapper.select(CompanyUser::getId, CompanyUser::getUsername);
        wrapper.eq(CompanyUser::getCompanyId, companyId);
        if (departId != null) {
            wrapper.eq(CompanyUser::getDepartmentId, departId);
        }
        //执行分页查询
        iPage = userService.page(iPage, wrapper);
        //处理结果
        return new PageResult<>(iPage.getTotal(), iPage.getPages(), iPage.getRecords());
    }

    public List<DepartmentDTO> organizationTree() {
        LambdaQueryWrapper<Department> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Department::getCompanyId, UserHolder.getCompanyId());
        wrapper.eq(Department::getParentId, 0);
        List<Department> parentDept = departmentService.list(wrapper);
        if (!CollectionUtils.isEmpty(parentDept)) {
            List<DepartmentDTO> departmentDTOS = BeanHelper.copyWithCollection(parentDept, DepartmentDTO.class);
            //下钻找子部门列表
            childrenDeptList(departmentDTOS);
            return departmentDTOS;
        }
        return null;
    }

    private void childrenDeptList(List<DepartmentDTO> departmentDTOS) {
        //1. 遍历上级部门集合
        for (DepartmentDTO departmentDTO : departmentDTOS) {
            LambdaQueryWrapper<Department> wrapper = new LambdaQueryWrapper<>();
            wrapper.eq(Department::getParentId, departmentDTO.getId());
            List<Department> list = departmentService.list(wrapper);
            if (!CollectionUtils.isEmpty(list)) {
                List<DepartmentDTO> departmentDTOSChild = BeanHelper.copyWithCollection(list, DepartmentDTO.class);
                departmentDTO.setChildren(departmentDTOSChild);
                //递归下钻
                childrenDeptList(departmentDTOSChild);
            }
        }
    }

    public PageResult<CompanyUserDTO> queryDeptMember(Integer page, Integer pageSize, Long departmentId, String keyword) {
        IPage<CompanyUser> iPage = new Page<>(page, pageSize);
        LambdaQueryWrapper<CompanyUser> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(CompanyUser::getCompanyId, UserHolder.getCompanyId());
        //只有认证成功的员工信息才能查询到
        wrapper.eq(CompanyUser::getEnable, true);
        IPage<CompanyUser> userIPage = null;
        //判断部门id
        if (departmentId == null || departmentId == 0) {
            //查询未分配部门的员工信息
            wrapper.isNull(CompanyUser::getDepartmentId);
            userIPage = userService.page(iPage, wrapper);
        } else {
            //根据部门查询员工信息
            wrapper.eq(CompanyUser::getDepartmentId, departmentId);
            userIPage = userService.page(iPage, wrapper);
            //TODO 根据部门查询员工信息，当部门有子部门时，查询父部门员工信息，显示其下所有子部门的员工信息
        }

        return new PageResult<>(userIPage.getTotal(), userIPage.getPages(), BeanHelper.copyWithCollection(userIPage.getRecords(), CompanyUserDTO.class));
    }

    public void uploadXls(MultipartFile file) {
        ExcelReader excelReader = null;
        try {
            //创建excel读取对象
            excelReader = EasyExcel.read(file.getInputStream(), CompanyUser.class, new CompanyUserDataListener(userService)).build();
            ReadSheet readSheet = EasyExcel.readSheet(0).build();
            excelReader.read(readSheet);

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (excelReader != null) {
                // 这里千万别忘记关闭，读的时候会创建临时文件，到时磁盘会崩的
                excelReader.finish();
            }
        }
    }
}
