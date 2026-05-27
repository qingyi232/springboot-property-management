package com.property.config;

import com.property.utils.Result;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(BadCredentialsException.class)
    public Result<?> handleBadCredentials(BadCredentialsException e) {
        return Result.fail("用户名或密码错误");
    }

    @ExceptionHandler(AccessDeniedException.class)
    public Result<?> handleAccessDenied(AccessDeniedException e) {
        return Result.fail(403, "没有操作权限");
    }

    @ExceptionHandler(RuntimeException.class)
    public Result<?> handleRuntime(RuntimeException e) {
        return Result.fail(e.getMessage());
    }

    @ExceptionHandler(Exception.class)
    public Result<?> handleException(Exception e) {
        return Result.fail("系统异常：" + e.getMessage());
    }
}
