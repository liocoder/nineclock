package com.itheima.search.entity;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class Article {
    /**
     * 文档标识
     */
    private Long id;
    /**
     * 文档标题名称 文件名  - 根据关键字 匹配查询检索该域
     */
    private String name;
    /**
     * 文档内容
     */
    private String content;
    /**
     * 创建时间
     */
    private Long createTime;
    /**
     * 企业ID
     */
    private Long companyId;
    /**
     * 分类ID-过滤字段
     */
    private Long categoryId;

}