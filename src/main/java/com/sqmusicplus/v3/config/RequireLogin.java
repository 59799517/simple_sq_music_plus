package com.sqmusicplus.v3.config;

import java.lang.annotation.*;

/**
 * 需要登录认证注解
 * 用于标记需要用户登录才能访问的接口
 */
@Target({ElementType.METHOD, ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface RequireLogin {
    /**
     * 是否需要登录，默认为 true
     */
    boolean value() default true;
}
