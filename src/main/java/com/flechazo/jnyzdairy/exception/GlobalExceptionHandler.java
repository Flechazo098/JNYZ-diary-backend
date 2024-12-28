package com.flechazo.jnyzdairy.exception;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.resource.NoResourceFoundException;

import java.util.HashMap;
import java.util.Map;

/**
 * 全局异常处理器，用于捕获和处理应用程序中抛出的各种异常。
 *
 * @author Flechazo
 */
@RestControllerAdvice
public class GlobalExceptionHandler {

    private static final Logger logger = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    /**
     * 处理资源未找到异常（ResourceNotFoundException）。
     *
     * @param ex 资源未找到异常
     * @return 包含错误信息的 ResponseEntity 对象，状态码为 404 (Not Found)
     */
    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<?> handleResourceNotFoundException(ResourceNotFoundException ex) {
        Map<String, String> response = new HashMap<>(1);
        response.put("error", ex.getMessage());
        return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
    }

    /**
     * 处理认证异常（AuthenticationException）。
     *
     * @param e 认证异常
     * @return 包含错误信息的 ResponseEntity 对象，状态码为 401 (Unauthorized)
     */
    @ExceptionHandler(AuthenticationException.class)
    public ResponseEntity<Map<String, String>> handleAuthenticationException(AuthenticationException e) {
        Map<String, String> response = new HashMap<>();
        response.put("error", "认证失败：" + e.getMessage());
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(response);
    }

    /**
     * 处理访问被拒绝异常（AccessDeniedException）。
     *
     * @param e 访问被拒绝异常
     * @return 包含错误信息的 ResponseEntity 对象，状态码为 403 (Forbidden)
     */
    @ExceptionHandler(AccessDeniedException.class)
    public ResponseEntity<Map<String, String>> handleAccessDeniedException(AccessDeniedException e) {
        Map<String, String> response = new HashMap<>();
        response.put("error", "访问被拒绝：权限不足");
        return ResponseEntity.status(HttpStatus.FORBIDDEN).body(response);
    }

    /**
     * 处理未授权异常（UnauthorizedException）。
     *
     * @param e 未授权异常
     * @return 包含错误信息的 ResponseEntity 对象，状态码为 401 (Unauthorized)
     */
    @ExceptionHandler(UnauthorizedException.class)
    public ResponseEntity<Map<String, String>> handleUnauthorizedException(UnauthorizedException e) {
        Map<String, String> response = new HashMap<>();
        response.put("error", e.getMessage());
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(response);
    }

    /**
     * 处理资源不存在异常（NoResourceFoundException）。
     *
     * @param e 资源不存在异常
     * @return 包含错误信息的 ResponseEntity 对象，状态码为 404 (Not Found)
     */
    @ExceptionHandler(NoResourceFoundException.class)
    public ResponseEntity<Map<String, String>> handleNoResourceFoundException(NoResourceFoundException e) {
        Map<String, String> response = new HashMap<>();
        response.put("error", "资源不存在：" + e.getMessage());
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
    }

    /**
     * 处理非法参数异常（IllegalArgumentException）。
     *
     * @param e 非法参数异常
     * @return 包含错误信息的 ResponseEntity 对象，状态码为 400 (Bad Request)
     */
    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<Map<String, String>> handleIllegalArgumentException(IllegalArgumentException e) {
        Map<String, String> response = new HashMap<>();
        response.put("error", "参数错误：" + e.getMessage());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
    }

    /**
     * 处理数字格式异常（NumberFormatException）。
     *
     * @param e 数字格式异常
     * @return 包含错误信息的 ResponseEntity 对象，状态码为 400 (Bad Request)
     */
    @ExceptionHandler(NumberFormatException.class)
    public ResponseEntity<Map<String, String>> handleNumberFormatException(NumberFormatException e) {
        Map<String, String> response = new HashMap<>();
        response.put("error", "数字格式错误：" + e.getMessage());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
    }

    /**
     * 处理所有其他类型的异常。
     *
     * @param e 捕获到的异常
     * @return 包含错误信息的 ResponseEntity 对象，状态码为 500 (Internal Server Error)
     */
    @ExceptionHandler(Exception.class)
    public ResponseEntity<Map<String, String>> handleGlobalException(Exception e) {
        logger.error("Unexpected error occurred", e);
        Map<String, String> response = new HashMap<>();
        response.put("error", "服务器内部错误");
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
    }
}