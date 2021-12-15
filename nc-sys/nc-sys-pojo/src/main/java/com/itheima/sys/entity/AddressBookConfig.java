package com.itheima.sys.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;

/**
 * <p>
 * 企业通讯录字段表
 * </p>
 *
 * @author itheima
 * @since 2021-11-23
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("sys_address_book_config")
public class AddressBookConfig implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * 字段名称
     */
    private String name;

    /**
     * fixed为固定字段，dynamic为动态字段
     */
    private String type;

    /**
     * 字段类型
     */
    private String fieldType;

    /**
     * 1为可用 0为不可用
     */
    private Boolean status;

    /**
     * 企业ID
     */
    private Long companyId;


}
