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

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

/**
 * 文件控制器
 */
@RestController
@RequestMapping("/api/files")
public class FileController {

    private final FileStorageService fileStorageService;

    public FileController(FileStorageService fileStorageService) {
        this.fileStorageService = fileStorageService;
    }

    /**
     * 上传用户头像
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
            return ResponseEntity.badRequest().build();
        }
    }

    /**
     * 获取文件
     */
    @GetMapping("/{userId}/**")
    public ResponseEntity<Resource> getFile(
            @PathVariable Long userId,
            HttpServletRequest request) throws IOException {
        
        String filePath = request.getRequestURI()
                .split("/api/files/" + userId + "/")[1];
        
        Path path = Paths.get(fileStorageService.getUserPath(), userId.toString(), filePath);
        
        if (!Files.exists(path)) {
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