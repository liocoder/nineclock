package com.itheima.document.enums;

import lombok.Getter;

/**
 * 文档读写权限枚举类型
 */

@Getter
public enum  DocumentEnum {

    /*文档权限*/
    ALL_READ(0, "公共读"),
    ALL_READ_WRITE(1, "公共读写"),
    PRIVATE_READ_WRITE(2, "私有"),

    FILE_TYPE_LOCAL(1, "本地文档"),
    FILE_TYPE_CLOUD(2, "云文档"),

    /**
     * 相对于文档而言：用户类型
     */
    USER_TYPE_NONE(0, "路人甲"),
    USER_TYPE_AUTHOR(1, "拥有者"),
    USER_TYPE_COLLABORATION(2, "协作者"),

    /**
     * 文档状态
     */
    FILE_STATUS_DRAFT(0, "草稿"),
    FILE_STATUS_WAIT_APPROVE(1, "提交（待审核）"),
    FILE_STATUS_FAIL(2, "审核失败"),
    FILE_STATUS_APPROVE_ARTIFICIAL(3, "人工审核"),
    FILE_STATUS_PASS(8, "审核通过"),

    ;

    DocumentEnum(Integer val, String desc) {
        this.val = val;
        this.desc = desc;
    }

    private Integer val;
    private String desc;

}