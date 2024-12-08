package com.flechazo.jnyzdairy.constant;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

/**
 * 文件相关常量
 */
public class FileConstants {
    
    /**
     * 允许的图片文件类型
     */
    public static final Set<String> ALLOWED_IMAGE_TYPES = new HashSet<>(Arrays.asList(
        "image/jpeg",
        "image/png",
        "image/gif",
        "image/webp"
    ));

    /**
     * 最大文件大小（5MB）
     */
    public static final long MAX_FILE_SIZE = 5 * 1024 * 1024;

    /**
     * 文件名最大长度
     */
    public static final int MAX_FILENAME_LENGTH = 100;
} 