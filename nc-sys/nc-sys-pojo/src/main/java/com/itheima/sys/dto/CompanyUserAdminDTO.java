package com.itheima.sys.dto;

import lombok.Data;

import java.util.List;

/**
 * @author itheima
 * 管理员新增DTO
 */
@Data
public class CompanyUserAdminDTO {

    /**
     * 员工ID
     */
    private Long userId;

    /**
     * 角色id集合
     */
    private List<String> roleIds;
}
