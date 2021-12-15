package com.itheima.common.vo;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

/**
 * @author : itheima
 * @description:
 **/
@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class UserInfo {

    /**
     * 用户信息
     */
    private Long id;
    private String username;
    private String roleDesc;

    private String mobile;
    private String imageUrl;
    private Boolean enable;
    /**
     *  公司用户相关信息
     */
    private Long companyId;
    private Long departmentId;
    private String companyName;
    private String departmentName;
    private String post;
    private String workNumber;

}