package com.flechazo.jnyzdairy.constant;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

/**
 * 文件相关常量类，定义了文件上传过程中使用的各种常量。
 * <p>
 * 包含允许的图片文件类型、最大文件大小以及文件名的最大长度等配置。
 *
 * @author Flechazo
 */
public class FileConstants {

    /**
     * 允许的图片文件类型集合。
     * <p>
     * 该集合列出了系统接受的所有图片文件的MIME类型，用于验证上传文件的合法性。
     */
    public static final Set<String> ALLOWED_IMAGE_TYPES = new HashSet<>(Arrays.asList(
            "image/jpeg",
            "image/png",
            "image/gif",
            "image/webp"
    ));

    /**
     * 最大文件大小<b>（50MB）</b>。
     * <p>
     * 定义了用户上传单个文件的最大尺寸限制，以字节为单位。
     */
    public static final long MAX_FILE_SIZE = 50 * 1024 * 1024;

    /**
     * 文件名最大长度。
     * <p>
     * 定义了文件名的最大字符数限制，防止过长的文件名导致存储或处理问题。
     */
    public static final int MAX_FILENAME_LENGTH = 100;
}