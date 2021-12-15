package com.itheima.sys.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * <p>
 * 企业表
 * </p>
 *
 * @author itheima
 * @since 2021-11-23
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("sys_company")
public class Company implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * 企业全称 全库唯一性校验
     */
    private String name;

    /**
     * 企业名称缩写
     */
    private String nameAbbr;

    /**
     * 企业简介
     */
    private String companyInfo;

    /**
     * 行业id
     */
    private Long industryId;

    /**
     * 行业名称
     */
    private String industryName;

    /**
     * 人员规模
     */
    private String companyScale;

    /**
     * 行政区域编码 所在地行政区域编码（区县级）
     */
    private String locationCode;

    /**
     * 行政区划名称
     */
    private String locationName;

    /**
     * logo
     */
    private String logo;

    /**
     * 企业状态 1有效、2注销
     */
    private Integer auditStatus;

    /**
     * 创建人id
     */
    private Long userId;

    /**
     * 创建时间
     */
    private LocalDateTime createTime;


}
