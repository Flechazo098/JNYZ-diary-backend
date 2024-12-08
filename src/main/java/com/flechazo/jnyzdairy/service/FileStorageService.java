package com.flechazo.jnyzdairy.service;

import com.flechazo.jnyzdairy.constant.FileConstants;
import com.flechazo.jnyzdairy.exception.FileStorageException;
import com.flechazo.jnyzdairy.util.ImageUtils;
import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Objects;
import java.util.UUID;

/**
 * 文件存储服务
 */
@Service
public class FileStorageService {

    private final ImageUtils imageUtils;

    public FileStorageService(ImageUtils imageUtils) {
        this.imageUtils = imageUtils;
    }

    @Value("${app.storage.root-path}")
    private String rootPath;

    /**
     * -- GETTER --
     *  获取用户文件路径
     */
    @Getter
    @Value("${app.storage.user-path}")
    private String userPath;


    /**
     * 保存用户头像
     */
    public String saveUserAvatar(Long userId, MultipartFile file) throws IOException {
        validateImageFile(file);
        String fileName = StringUtils.cleanPath(Objects.requireNonNull(file.getOriginalFilename()));
        String fileExtension = getFileExtension(fileName);
        String newFileName = "avatar" + fileExtension;

        Path userAvatarDir = Paths.get(userPath, userId.toString(), "avatar");
        Files.createDirectories(userAvatarDir);

        Path targetPath = userAvatarDir.resolve(newFileName);
        Files.copy(file.getInputStream(), targetPath, StandardCopyOption.REPLACE_EXISTING);

        return targetPath.toString();
    }

    /**
     * 保存日记图片
     */
    public String saveDiaryImage(Long userId, LocalDateTime date, MultipartFile file) throws IOException {
        return saveImage(userId, date, file, false, null);
    }

    /**
     * 保存日记图片（带水印）
     */
    public String saveDiaryImageWithWatermark(Long userId, LocalDateTime date, MultipartFile file) throws IOException {
        String watermarkText = date.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm"));
        return saveImage(userId, date, file, true, watermarkText);
    }

    /**
     * 保存图片
     */
    private String saveImage(Long userId, LocalDateTime date, MultipartFile file, boolean withWatermark, String watermarkText) throws IOException {
        validateImageFile(file);
        String fileName = StringUtils.cleanPath(Objects.requireNonNull(file.getOriginalFilename()));
        String fileExtension = getFileExtension(fileName);
        String newFileName = UUID.randomUUID().toString() + fileExtension;

        String dateStr = date.format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
        Path diaryImageDir = Paths.get(userPath, userId.toString(), "diary", dateStr);
        Files.createDirectories(diaryImageDir);

        Path targetPath = diaryImageDir.resolve(newFileName);
        Files.copy(file.getInputStream(), targetPath, StandardCopyOption.REPLACE_EXISTING);

        if (withWatermark) {
            // 添加文字水印（日期）
            imageUtils.addTextWatermark(targetPath, watermarkText);
        }

        return targetPath.toString();
    }

    /**
     * 删除文件
     */
    public void deleteFile(String filePath) throws IOException {
        Path path = Paths.get(filePath);
        validateFilePath(path);
        Files.deleteIfExists(path);
    }

    /**
     * 获取文件扩展名
     */
    private String getFileExtension(String fileName) {
        int dotIndex = fileName.lastIndexOf('.');
        return (dotIndex == -1) ? "" : fileName.substring(dotIndex);
    }

    /**
     * 验证图片文件
     */
    private void validateImageFile(MultipartFile file) {
        // 检查文件是否为空
        if (file.isEmpty()) {
            throw new FileStorageException("Failed to store empty file");
        }

        // 检查文件大小
        if (file.getSize() > FileConstants.MAX_FILE_SIZE) {
            throw new FileStorageException("File size exceeds maximum limit");
        }

        // 检查文件类型
        String contentType = file.getContentType();
        if (!FileConstants.ALLOWED_IMAGE_TYPES.contains(contentType)) {
            throw new FileStorageException("File type not allowed");
        }

        // 检查文件名长度
        String fileName = StringUtils.cleanPath(Objects.requireNonNull(file.getOriginalFilename()));
        if (fileName.length() > FileConstants.MAX_FILENAME_LENGTH) {
            throw new FileStorageException("File name too long");
        }

        // 检查文件名是否包含非法字符
        if (fileName.contains("..")) {
            throw new FileStorageException("Filename contains invalid path sequence");
        }
    }

    /**
     * 验证文件路径
     */
    private void validateFilePath(Path path) {
        // 检查路径是否在允许的目录下
        try {
            Path normalizedPath = path.normalize();
            Path rootDir = Paths.get(rootPath).normalize();
            if (!normalizedPath.startsWith(rootDir)) {
                throw new FileStorageException("Invalid file path");
            }
        } catch (Exception e) {
            throw new FileStorageException("Invalid file path", e);
        }
    }

}
