package com.itheima.common.exception;

import com.itheima.common.exception.auth.NcException;
import com.itheima.common.vo.Result;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

@ResponseBody
@ControllerAdvice
public class BasicExceptionHandler {

    @ExceptionHandler(RuntimeException.class)
    public Result handleRuntimeErr(RuntimeException e) {
        // 我们暂定返回状态码为400， 然后从异常中获取友好提示信息
        return Result.errorCodeMessage(500, e.getMessage());
    }

    /**
     * 处理自定义异常
     */
    @ExceptionHandler(NcException.class)
    public Result<String> handlerNcException(NcException e) {
        return Result.errorCodeMessage(e.getCode(), e.getMessage());
    }

    /**
     * 处理其他异常
     */
    @ExceptionHandler(Exception.class)
    public Result handlerException(Exception e) {
        return Result.errorCodeMessage(500, e.getMessage());
    }

}
