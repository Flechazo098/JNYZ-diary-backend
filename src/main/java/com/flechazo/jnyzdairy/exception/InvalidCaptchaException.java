package com.flechazo.jnyzdairy.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * 验证码异常，当验证码验证失败时抛出。
 *
 * @author Flechazo
 */
@ResponseStatus(HttpStatus.BAD_REQUEST)
public class InvalidCaptchaException extends RuntimeException {

    /**
     * 构造函数，用于初始化带有默认消息的验证码异常。
     */
    public InvalidCaptchaException() {
        super("验证码错误");
    }

    /**
     * 构造函数，用于初始化带有自定义消息的验证码异常。
     *
     * @param message 异常的消息描述
     */
    public InvalidCaptchaException(String message) {
        super(message);
    }

    /**
     * 构造函数，用于初始化带有消息和原因的验证码异常。
     *
     * @param message 异常的消息描述
     * @param cause   导致异常的原始错误或异常
     */
    public InvalidCaptchaException(String message, Throwable cause) {
        super(message, cause);
    }
}