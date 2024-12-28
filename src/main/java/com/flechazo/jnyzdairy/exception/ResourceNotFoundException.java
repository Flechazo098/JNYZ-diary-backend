package com.flechazo.jnyzdairy.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * 资源未找到异常，当请求的资源不存在时抛出。
 *
 * @author Flechazo
 */
@ResponseStatus(HttpStatus.NOT_FOUND)
public class ResourceNotFoundException extends RuntimeException {

    /**
     * 构造函数，用于初始化带有消息的资源未找到异常。
     *
     * @param message 异常的消息描述
     */
    public ResourceNotFoundException(String message) {
        super(message);
    }

    /**
     * 构造函数，用于初始化带有消息和原因的资源未找到异常。
     *
     * @param message 异常的消息描述
     * @param cause   导致异常的原始错误或异常
     */
    public ResourceNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }
}