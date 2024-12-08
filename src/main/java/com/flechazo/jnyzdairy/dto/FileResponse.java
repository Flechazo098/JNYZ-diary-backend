package com.flechazo.jnyzdairy.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * 文件上传响应DTO
 */
@Data
@AllArgsConstructor
public class FileResponse {
    private String filePath;
} 