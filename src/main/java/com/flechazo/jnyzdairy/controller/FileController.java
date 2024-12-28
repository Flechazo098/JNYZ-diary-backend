package com.flechazo.jnyzdairy.controller;

import com.flechazo.jnyzdairy.dto.FileResponse;
import com.flechazo.jnyzdairy.service.FileStorageService;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import jakarta.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

/**
 * 文件控制器，用于处理文件上传和下载的HTTP请求。
 *
 * @author Flechazo
 */
@RestController
@RequestMapping("/api/files")
public class FileController {

    /**
     * 文件存储服务，提供文件上传和获取的功能。
     */
    private final FileStorageService fileStorageService;

    /**
     * 构造函数，注入文件存储服务。
     *
     * @param fileStorageService 文件存储服务实例
     */
    public FileController(FileStorageService fileStorageService) {
        this.fileStorageService = fileStorageService;
    }

    /**
     * 上传用户头像。
     * <p>
     * 该方法为指定用户ID上传用户头像，并返回上传后的文件路径。
     *
     * @param userId 用户ID
     * @param file   待上传的文件
     * @return 包含文件路径的响应实体
     */
    @PostMapping("/avatar")
    @PreAuthorize("@securityService.isCurrentUser(#userId)")
    public ResponseEntity<FileResponse> uploadAvatar(
            @RequestAttribute("userId") Long userId,
            @RequestParam("file") MultipartFile file) {
        try {
            String filePath = fileStorageService.saveUserAvatar(userId, file);
            return ResponseEntity.ok(new FileResponse(filePath));
        } catch (IOException e) {
            /* 如果上传过程中发生IO异常，则返回400错误响应 */
            return ResponseEntity.badRequest().build();
        }
    }

    /**
     * 获取文件。
     * <p>
     * 根据提供的用户ID和文件路径获取文件资源。
     *
     * @param userId 用户ID
     * @param request HTTP请求对象，用于提取文件路径信息
     * @return 包含文件资源的响应实体
     * @throws IOException 如果读取文件时发生错误，则抛出此异常
     */
    @GetMapping("/{userId}/**")
    public ResponseEntity<Resource> getFile(
            @PathVariable Long userId,
            HttpServletRequest request) throws IOException {

        /*
         * 提取文件路径并构建完整的文件系统路径。
         * 检查文件是否存在，并设置适当的响应头以返回文件内容。
         */
        String filePath = request.getRequestURI()
                .split("/api/files/" + userId + "/")[1];
        Path path = Paths.get(fileStorageService.getUserPath(), userId.toString(), filePath);

        if (!Files.exists(path)) {
            /* 如果文件不存在，则返回404错误响应 */
            return ResponseEntity.notFound().build();
        }

        String contentType = Files.probeContentType(path);
        if (contentType == null) {
            contentType = "application/octet-stream";
        }

        Resource resource = new org.springframework.core.io.UrlResource(path.toUri());
        return ResponseEntity.ok()
                .contentType(MediaType.parseMediaType(contentType))
                .header(HttpHeaders.CONTENT_DISPOSITION, "inline; filename=\"" + path.getFileName().toString() + "\"")
                .body(resource);
    }
}