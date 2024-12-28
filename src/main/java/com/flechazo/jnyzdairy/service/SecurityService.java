package com.flechazo.jnyzdairy.service;

import com.flechazo.jnyzdairy.entity.Diary;
import com.flechazo.jnyzdairy.entity.DiaryImage;
import com.flechazo.jnyzdairy.repository.DiaryImageRepository;
import com.flechazo.jnyzdairy.repository.DiaryRepository;
import com.flechazo.jnyzdairy.util.SecurityUtils;
import org.springframework.stereotype.Service;

/**
 * 安全服务，用于权限验证。
 * <p>
 * 该服务提供了一系列方法来检查当前用户是否拥有特定资源的所有权或访问权限，
 * 包括日记和日记图片。此外，还提供了辅助方法来获取当前用户的ID。
 *
 * @author Flechazo
 */
@Service
public class SecurityService {

    private final DiaryRepository diaryRepository;
    private final DiaryImageRepository diaryImageRepository;

    /**
     * 构造函数，用于依赖注入。
     *
     * @param diaryRepository 日记仓库接口，用于与日记数据交互
     * @param diaryImageRepository 日记图片仓库接口，用于与日记图片数据交互
     */
    public SecurityService(DiaryRepository diaryRepository, DiaryImageRepository diaryImageRepository) {
        this.diaryRepository = diaryRepository;
        this.diaryImageRepository = diaryImageRepository;
    }

    /**
     * 检查当前用户是否是给定ID的日记条目的所有者。
     * <p>
     * 该方法通过日记ID查找对应的日记条目，并对比其关联的用户ID与当前用户的ID。
     * 如果两者匹配，则认为当前用户是该日记条目的所有者。
     *
     * @param diaryId 日记条目的唯一标识符
     * @return 如果当前用户是日记条目的所有者则返回 {@code true}，否则返回 {@code false}
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
     * 检查当前用户是否是给定ID的日记图片的所有者。
     * <p>
     * 该方法通过图片ID查找对应的日记图片，并对比其关联的日记条目所属用户ID与当前用户的ID。
     * 如果两者匹配，则认为当前用户是该日记图片的所有者。
     *
     * @param imageId 日记图片的唯一标识符
     * @return 如果当前用户是日记图片的所有者则返回 {@code true}，否则返回 {@code false}
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
     * 检查当前用户是否是指定用户。
     * <p>
     * 该方法比较给定的用户ID与当前用户的ID，如果两者相同，则表示当前用户是指定用户。
     *
     * @param userId 要检查的用户ID
     * @return 如果当前用户是指定用户则返回 {@code true}，否则返回 {@code false}
     */
    public boolean isCurrentUser(Long userId) {
        Long currentUserId = getCurrentUserId();
        return userId.equals(currentUserId);
    }

    /**
     * 获取当前用户的ID。
     * <p>
     * 该私有方法利用 {@code SecurityUtils} 工具类从安全上下文中提取当前用户的ID。
     *
     * @return 当前用户的唯一标识符
     */
    private Long getCurrentUserId() {
        // 从SecurityContextHolder中获取当前用户ID
        return SecurityUtils.getCurrentUserId();
    }
}