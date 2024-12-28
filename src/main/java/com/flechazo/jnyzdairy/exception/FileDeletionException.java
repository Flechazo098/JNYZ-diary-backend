package com.flechazo.jnyzdairy.exception;

/**
 * 文件删除异常，当文件删除操作失败时抛出。
 *
 * @author Flechazo
 */
public class FileDeletionException extends RuntimeException {

    /**
     * 构造函数，用于初始化带有消息和原因的文件删除异常。
     *
     * @param message 异常的消息描述
     * @param cause   导致异常的原始错误或异常
     */
    public FileDeletionException(String message, Throwable cause) {
        super(message, cause);
    }

    /**
     * 构造函数，仅用于初始化带有消息的文件删除异常。
     *
     * @param message 异常的消息描述
     */
    public FileDeletionException(String message) {
        super(message);
    }

    /**
     * 构造函数，仅用于初始化带有原因的文件删除异常。
     *
     * @param cause 导致异常的原始错误或异常
     */
    public FileDeletionException(Throwable cause) {
        super(cause);
    }
}