package com.flechazo.jnyzdairy.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * 文件存储异常，当文件存储操作失败时抛出。
 *
 * @author Flechazo
 */
@ResponseStatus(HttpStatus.BAD_REQUEST)
public class FileStorageException extends RuntimeException {

    /**
     * 构造函数，用于初始化带有消息的文件存储异常。
     *
     * @param message 异常的消息描述
     */
    public FileStorageException(String message) {
        super(message);
    }

    /**
     * 构造函数，用于初始化带有消息和原因的文件存储异常。
     *
     * @param message 异常的消息描述
     * @param cause   导致异常的原始错误或异常
     */
    public FileStorageException(String message, Throwable cause) {
        super(message, cause);
    }
}