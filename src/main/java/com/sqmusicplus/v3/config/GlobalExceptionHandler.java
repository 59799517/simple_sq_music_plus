package com.sqmusicplus.v3.config;

import com.sqmusicplus.v3.config.exception.SQException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * @Classname GlobalExceptionHandler
 * @Description 全局异常处理
 * @Version 1.0.0
 * @Date 2025/7/24 17:42
 * @Created by SQ
 */
@Slf4j
@ControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler(value = Exception.class)
    @ResponseBody
    public AjaxResult exceptionHandler(Exception e) {
        e.printStackTrace();
    // 日志记录异常信息
        log.error("全局异常捕获：", e);
    // 返回统一的错误响应格式
        return AjaxResult.error(e.getMessage());
    }


    @ResponseBody
    public AjaxResult bsqExceptionHandler(SQException e) {
        e.printStackTrace();
        return AjaxResult.error(e.getMsg());
    }

}
