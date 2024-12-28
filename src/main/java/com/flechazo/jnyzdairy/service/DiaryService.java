package com.flechazo.jnyzdairy.service;

import com.flechazo.jnyzdairy.entity.Diary;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;
import java.util.List;

/**
 * 日记服务接口，提供创建、更新、获取和删除日记及其图片的功能。
 *
 * @author Flechazo
 */
public interface DiaryService {

    /**
     * 创建新的日记条目。
     * <p>
     * 该方法用于创建一个新的日记条目，并将其保存到数据库中。如果提供了图片文件，
     * 则会将这些图片与日记关联并存储。
     *
     * @param userId 用户ID
     * @param content 日记内容
     * @param date 日记日期
     * @param images 日记相关的图片列表
     * @return 新创建的日记对象
     */
    Diary createDiary(Long userId, LocalDateTime date, String content, List<MultipartFile> images);

    /**
     * 更新现有日记条目。
     * <p>
     * 该方法允许更新指定ID的日记条目的内容以及关联的新图片。旧的日记图片不会被自动删除，
     * 如果需要移除旧图片，请调用 {@code deleteDiaryImage} 方法。
     *
     * @param diaryId 要更新的日记ID
     * @param content 更新后的日记内容
     * @param newImages 新添加的日记图片列表
     * @return 更新后的日记对象
     */
    Diary updateDiary(Long diaryId, String content, List<MultipartFile> newImages);

    /**
     * 获取指定ID的日记条目。
     * <p>
     * 该方法通过日记ID来检索特定的日记条目信息。
     *
     * @param diaryId 日记ID
     * @return 对应ID的日记对象
     */
    Diary getDiary(Long diaryId);

    /**
     * 根据用户ID和日期获取日记条目。
     * <p>
     * 该方法用于查找特定用户在某一天的日记条目。
     *
     * @param userId 用户ID
     * @param date 日记日期
     * @return 符合条件的日记对象
     */
    Diary getDiaryByDate(Long userId, LocalDateTime date);

    /**
     * 获取用户的所有日记条目。
     * <p>
     * 该方法返回给定用户的所有日记条目列表。
     *
     * @param userId 用户ID
     * @return 用户的所有日记条目列表
     */
    List<Diary> getUserDiaries(Long userId);

    /**
     * 获取用户在指定日期范围内的日记条目。
     * <p>
     * 该方法用于检索用户在开始日期和结束日期之间的所有日记条目。
     *
     * @param userId 用户ID
     * @param startDate 开始日期（包含）
     * @param endDate 结束日期（包含）
     * @return 在指定日期范围内的日记条目列表
     */
    List<Diary> getUserDiariesBetween(Long userId, LocalDateTime startDate, LocalDateTime endDate);

    /**
     * 删除指定ID的日记条目。
     * <p>
     * 该方法根据日记ID来删除对应的日记条目。请注意，这也将导致所有与该日记关联的图片被删除。
     *
     * @param diaryId 日记ID
     */
    void deleteDiary(Long diaryId);

    /**
     * 删除指定ID的日记图片。
     * <p>
     * 该方法用于从系统中移除特定的日记图片。
     *
     * @param imageId 图片ID
     */
    void deleteDiaryImage(Long imageId);
}