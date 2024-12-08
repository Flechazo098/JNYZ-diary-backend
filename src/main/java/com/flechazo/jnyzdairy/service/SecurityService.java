package com.flechazo.jnyzdairy.service;

import com.flechazo.jnyzdairy.entity.Diary;
import com.flechazo.jnyzdairy.entity.DiaryImage;
import com.flechazo.jnyzdairy.repository.DiaryImageRepository;
import com.flechazo.jnyzdairy.repository.DiaryRepository;
import com.flechazo.jnyzdairy.util.SecurityUtils;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * 安全服务，用于权限验证
 */
@Service
public class SecurityService {

    private final DiaryRepository diaryRepository;
    private final DiaryImageRepository diaryImageRepository;

    @Autowired
    public SecurityService(DiaryRepository diaryRepository, DiaryImageRepository diaryImageRepository) {
        this.diaryRepository = diaryRepository;
        this.diaryImageRepository = diaryImageRepository;
    }

    /**
     * 检查当前用户是否是日记的所有者
     */
    public boolean isOwner(Long diaryId) {
        Diary diary = diaryRepository.findById(diaryId).orElse(null);
        if (diary == null) {
            return false;
        }
        Long currentUserId = getCurrentUserId();
        return diary.getUser().getId().equals(currentUserId);
    }

    /**
     * 检查当前用户是否是图片的所有者
     */
    public boolean isImageOwner(Long imageId) {
        DiaryImage image = diaryImageRepository.findById(imageId).orElse(null);
        if (image == null) {
            return false;
        }
        Long currentUserId = getCurrentUserId();
        return image.getDiary().getUser().getId().equals(currentUserId);
    }

    /**
     * 检查当前用户是否是指定用户
     */
    public boolean isCurrentUser(Long userId) {
        Long currentUserId = getCurrentUserId();
        return userId.equals(currentUserId);
    }

    /**
     * 获取当前用户ID
     */
    private Long getCurrentUserId() {
        // 从SecurityContextHolder中获取当前用户ID
        return SecurityUtils.getCurrentUserId();
    }
} 