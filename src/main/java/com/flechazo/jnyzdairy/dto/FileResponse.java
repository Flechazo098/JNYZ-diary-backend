package com.flechazo.jnyzdairy.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * 文件上传响应DTO，用于封装文件上传成功后的响应信息。
 *
 * @author Flechazo
 */
@Data
@AllArgsConstructor
public class FileResponse {

    /**
     * 文件的存储路径，表示文件在服务器上的位置。
     */
    private String filePath;
}