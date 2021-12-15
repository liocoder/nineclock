package com.itheima.document.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * <p>
 * 文件表 文档-文件表
 * </p>
 *
 * @author itheima
 * @since 2021-11-29
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("doc_file")
@ApiModel(value="File对象", description="文件表 文档-文件表")
public class File implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "id id")
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    @ApiModelProperty(value = "文件夹id 文件夹id")
    private Long folderId;

    @ApiModelProperty(value = "创建人id 创建人")
    private Long companyUserId;

    @ApiModelProperty(value = "文件名称 文件名")
    private String name;

    @ApiModelProperty(value = "文件路径 文件路径")
    private String filePath;

    @ApiModelProperty(value = "文档大小，单位为K")
    private String fileSize;

    @ApiModelProperty(value = "是否可用 1可用 0禁用")
    private Integer enable;

    @ApiModelProperty(value = "协作人文件权限 文件权限：0为公共读，1为公共读写，2为私有")
    private Integer authority;

    @ApiModelProperty(value = "创建时间")
    private LocalDateTime createTime;

    @ApiModelProperty(value = "更新时间")
    private LocalDateTime updateTime;

    private Long updateUserId;

    private Long companyId;

    @ApiModelProperty(value = "文档内容")
    private String content;

    @ApiModelProperty(value = "文档类型：1本地文档 2云文档")
    private Integer type;

    @ApiModelProperty(value = "文档分类ID")
    private Long categoryId;

    @ApiModelProperty(value = "当前状态 0：草稿  1：提交（待审核） 2：审核失败 3：人工审核  8：审核通过 ")
    private Integer status;

    @ApiModelProperty(value = "是否热点")
    private Boolean ifHot;


}
