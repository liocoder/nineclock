package com.itheima.document.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
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
 * 文件夹表 文档-文件夹表
 * </p>
 *
 * @author itheima
 * @since 2021-11-29
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("doc_folder")
@ApiModel(value="Folder对象", description="文件夹表 文档-文件夹表")
public class Folder implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "id id")
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    @ApiModelProperty(value = "企业id 企业id")
    private Long companyId;

    @ApiModelProperty(value = "父目录id")
    private Long parentId;

    @ApiModelProperty(value = "文件夹名称 文件夹名称")
    private String name;

    @TableField("`order`")
    @ApiModelProperty(value = "排序 排序：最小为0")
    private Integer order;

    @ApiModelProperty(value = "创建人 创建人")
    private Long companyUserId;

    @ApiModelProperty(value = "创建时间")
    private LocalDateTime createTime;

    @ApiModelProperty(value = "使用状态 使用状态：1为可用，0为不可用")
    private Integer status;


}
