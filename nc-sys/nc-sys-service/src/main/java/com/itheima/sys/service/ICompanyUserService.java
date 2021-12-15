package com.itheima.sys.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.itheima.sys.dto.CompanyUserAdminDTO;
import com.itheima.sys.dto.CompanyUserDTO;
import com.itheima.sys.dto.UserJoinCompanyDTO;
import com.itheima.sys.entity.CompanyUser;

import java.util.List;

/**
 * <p>
 * 企业员工表  服务类
 * </p>
 *
 * @author itheima
 * @since 2021-11-23
 */
public interface ICompanyUserService extends IService<CompanyUser> {

    CompanyUserDTO querySysUser(String username);

    Boolean checkMobile(String mobile);

    List<CompanyUserDTO> findCompanyAdmins();

    void subAdmin(CompanyUserAdminDTO companyUserAdminDTO);

    Long register(CompanyUser companyUser, String checkcode);

    void applyJoinCompany(UserJoinCompanyDTO userJoinCompanyDTO);

    void allowedJonCompany(Long applyUserId, Boolean approved, String remark, String notifyMsgId);

    List<CompanyUserDTO> queryUsers(List<Long> ids);

    List<CompanyUserDTO> queryAllCompanyUser();

    void updateIntegral(Long userId, Boolean plusFlag, Integer integral);
}
