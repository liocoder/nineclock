package com.itheima.common.exception.auth;

import com.itheima.common.enums.ResponseEnum;
import lombok.Getter;

@Getter
public class NcException extends RuntimeException {

    private Integer code;

    /**
     * 通过构造方法注入code message
     */
    public NcException(Integer code, String message) {
        super(message);
        this.code = code;
    }

    /**
     * 自定义状态码信息，通过枚举类型
     */
    public NcException(ResponseEnum responseEnum) {
        super(responseEnum.getMessage());
        this.code = responseEnum.getCode();
    }
}
