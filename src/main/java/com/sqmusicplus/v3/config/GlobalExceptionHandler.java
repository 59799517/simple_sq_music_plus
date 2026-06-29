package com.sqmusicplus.v3.config;

import com.sqmusicplus.v3.config.exception.IgnoreDownloadException;
import com.sqmusicplus.v3.config.exception.SQException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.resource.NoResourceFoundException;

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

    /**
     * 处理静态资源不存在的异常 - 只记录 debug 日志，避免刷屏
     */
    @ExceptionHandler(value = NoResourceFoundException.class)
    @ResponseBody
    public AjaxResult noResourceFoundHandler(NoResourceFoundException e) {
        String path = e.getResourcePath();
        // .well-known 是 Chrome DevTools 的调试请求，favicon.ico 是浏览器图标
        // 这些是正常行为，不需要打印错误堆栈
        if (path != null && (path.contains(".well-known") || path.contains("favicon.ico"))) {
            log.debug("静态资源不存在(可忽略): {}", path);
        } else {
            log.warn("静态资源不存在: {}", path);
        }
        return AjaxResult.error("资源不存在");
    }


    @ResponseBody
    public AjaxResult bsqExceptionHandler(SQException e) {
        e.printStackTrace();
        return AjaxResult.error(e.getMsg());
    }

    @ResponseBody
    public AjaxResult bIgnoreDownloadExceptionHandler(IgnoreDownloadException e) {
        e.printStackTrace();
        return AjaxResult.error(e.getMessage());
    }
}
