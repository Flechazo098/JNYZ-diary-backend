package com.flechazo.jnyzdairy.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * 用户已存在异常，当尝试创建一个已经存在的用户时抛出。
 *
 * @author Flechazo
 */
@ResponseStatus(HttpStatus.CONFLICT)
public class UserAlreadyExistsException extends RuntimeException {

    /**
     * 构造函数，用于初始化带有消息的用户已存在异常。
     *
     * @param message 异常的消息描述
     */
    public UserAlreadyExistsException(String message) {
        super(message);
    }

    /**
     * 构造函数，用于初始化带有消息和原因的用户已存在异常。
     *
     * @param message 异常的消息描述
     * @param cause   导致异常的原始错误或异常
     */
    public UserAlreadyExistsException(String message, Throwable cause) {
        super(message, cause);
    }
}