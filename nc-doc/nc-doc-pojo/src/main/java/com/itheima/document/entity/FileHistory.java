package com.itheima.document.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import java.time.LocalDateTime;
import java.io.Serializable;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * <p>
 * 
 * </p>
 *
 * @author itheima
 * @since 2021-11-29
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("doc_file_history")
@ApiModel(value="FileHistory对象", description="")
public class FileHistory implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    @ApiModelProperty(value = "文档ID")
    private Long fileId;

    @ApiModelProperty(value = "文档名称")
    private String fileName;

    @ApiModelProperty(value = "创建或者更新者id")
    private Long companyUserId;

    @ApiModelProperty(value = "创建时间")
    private LocalDateTime createTime;

    @ApiModelProperty(value = "是否是创建文件 true代表创建 false代表编辑")
    private Boolean ifCreate;

    @ApiModelProperty(value = "文档内容")
    private String content;


}
