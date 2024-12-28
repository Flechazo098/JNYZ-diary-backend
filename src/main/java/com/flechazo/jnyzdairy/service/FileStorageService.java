package com.flechazo.jnyzdairy.service;

import org.springframework.web.multipart.MultipartFile;
import java.io.IOException;
import java.time.LocalDateTime;

/**
 * 文件存储服务接口，提供文件上传、删除和验证等功能。
 *
 * @author flechazo
 */
public interface FileStorageService {

    /**
     * 将给定的文件存储到指定的目录，并返回文件的访问路径。
     *
     * @param file     需要存储的文件对象
     * @param directory 存储文件的目标目录
     * @return 文件的相对或绝对路径
     * @throws IOException 如果文件存储过程中发生IO异常
     */
    String storeFile(MultipartFile file, String directory) throws IOException;

    /**
     * 根据提供的文件路径删除文件。
     *
     * @param filePath 要删除的文件的路径
     * @throws IOException 如果删除文件时发生IO异常
     */
    void deleteFile(String filePath) throws IOException;

    /**
     * 保存用户头像文件，并返回该文件的访问路径。
     *
     * @param userId 用户标识符
     * @param file   用户上传的头像文件
     * @return 用户头像的相对或绝对路径
     * @throws IOException 如果保存头像过程中发生IO异常
     */
    String saveUserAvatar(Long userId, MultipartFile file) throws IOException;

    /**
     * 保存日记条目的图片，并返回该图片的访问路径。
     *
     * @param userId 用户标识符
     * @param date   日记条目日期
     * @param file   用户上传的图片文件
     * @return 日记图片的相对或绝对路径
     * @throws IOException 如果保存图片过程中发生IO异常
     */
    String saveDiaryImage(Long userId, LocalDateTime date, MultipartFile file) throws IOException;

    /**
     * 获取用户的根文件夹路径，通常用于构建文件存储位置。
     *
     * @return 用户文件的根路径
     */
    String getUserPath();

    /**
     * 验证提供的文件是否为有效的图片文件类型。
     *
     * @param file 需要验证的文件对象
     */
    void validateImageFile(MultipartFile file);
}