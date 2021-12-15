package com.itheima.sys.dto;

import lombok.Data;

/**
 * 接收页面提交参数
 */
@Data
public class MemberExcelRequest {

    private MemberExcelChineseDTO [] xlsxJson;

}