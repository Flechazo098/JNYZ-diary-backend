package com.flechazo.jnyzdairy.service.impl;

import com.flechazo.jnyzdairy.entity.Diary;
import com.flechazo.jnyzdairy.entity.DiaryImage;
import com.flechazo.jnyzdairy.entity.User;
import com.flechazo.jnyzdairy.exception.FileDeletionException;
import com.flechazo.jnyzdairy.exception.ResourceNotFoundException;
import com.flechazo.jnyzdairy.repository.DiaryImageRepository;
import com.flechazo.jnyzdairy.repository.DiaryRepository;
import com.flechazo.jnyzdairy.repository.UserRepository;
import com.flechazo.jnyzdairy.service.DiaryService;
import com.flechazo.jnyzdairy.service.FileStorageService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;

/**
 * 日记服务实现类，提供创建、更新、获取、删除日记及其图片的功能。
 *
 * @author Flechazo
 */
@Service
public class DiaryServiceImpl implements DiaryService {

    private static final Logger logger = LoggerFactory.getLogger(DiaryServiceImpl.class);

    private final DiaryRepository diaryRepository;
    private final DiaryImageRepository diaryImageRepository;
    private final UserRepository userRepository;
    private final FileStorageService fileStorageService;

    /**
     * 构造函数用于依赖注入。
     */
    public DiaryServiceImpl(DiaryRepository diaryRepository, DiaryImageRepository diaryImageRepository, UserRepository userRepository, FileStorageService fileStorageService) {
        this.diaryRepository = diaryRepository;
        this.diaryImageRepository = diaryImageRepository;
        this.userRepository = userRepository;
        this.fileStorageService = fileStorageService;
    }

    /**
     * 创建新的日记条目。
     *
     * @param userId 用户ID
     * @param date   日记日期
     * @param content 日记内容
     * @param images 日记关联的图片列表
     * @return 创建的日记对象
     * @throws ResourceNotFoundException 如果用户未找到
     */
    @Override
    @Transactional(rollbackFor = {ResourceNotFoundException.class})
    public Diary createDiary(Long userId, LocalDateTime date, String content, List<MultipartFile> images) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));

        Diary diary = new Diary();
        diary.setUser(user);
        diary.setDate(date);
        diary.setContent(content);

        diary = diaryRepository.save(diary);

        if (images != null && !images.isEmpty()) {
            for (MultipartFile image : images) {
                try {
                    String imagePath = fileStorageService.saveDiaryImage(userId, date, image);
                    DiaryImage diaryImage = new DiaryImage();
                    diaryImage.setDiary(diary);
                    diaryImage.setPath(imagePath);
                    diary.getImages().add(diaryImage);
                } catch (IOException e) {
                    throw new RuntimeException("Failed to save diary image", e);
                }
            }
            /* 保存包含图片信息的日志实体 */
            diary = diaryRepository.save(diary);
        }

        return diary;
    }

    /**
     * 更新现有的日记条目。
     *
     * @param diaryId 日记ID
     * @param content 新的日记内容
     * @param newImages 新添加的图片列表
     * @return 更新后的日记对象
     */
    @Override
    @Transactional(rollbackFor = {RuntimeException.class})
    public Diary updateDiary(Long diaryId, String content, List<MultipartFile> newImages) {
        Diary diary = getDiary(diaryId);
        diary.setContent(content);

        if (newImages != null && !newImages.isEmpty()) {
            for (MultipartFile image : newImages) {
                try {
                    String imagePath = fileStorageService.saveDiaryImage(
                            diary.getUser().getId(), diary.getDate(), image);
                    DiaryImage diaryImage = new DiaryImage();
                    diaryImage.setDiary(diary);
                    diaryImage.setPath(imagePath);
                    diary.getImages().add(diaryImage);
                } catch (IOException e) {
                    throw new RuntimeException("Failed to save diary image", e);
                }
            }
        }

        return diaryRepository.save(diary);
    }

    /**
     * 根据日记ID获取日记条目。
     *
     * @param diaryId 日记ID
     * @return 日记对象
     * @throws ResourceNotFoundException 如果日记未找到
     */
    @Override
    public Diary getDiary(Long diaryId) {
        return diaryRepository.findById(diaryId)
                .orElseThrow(() -> new ResourceNotFoundException("Diary not found"));
    }

    /**
     * 根据用户ID和日期获取日记条目。
     *
     * @param userId 用户ID
     * @param date 日记日期
     * @return 日记对象
     * @throws ResourceNotFoundException 如果日记未找到
     */
    @Override
    public Diary getDiaryByDate(Long userId, LocalDateTime date) {
        return diaryRepository.findByUserIdAndDate(userId, date)
                .orElseThrow(() -> new ResourceNotFoundException("Diary not found for the specified date"));
    }

    /**
     * 获取指定用户的全部日记条目，按日期降序排列。
     *
     * @param userId 用户ID
     * @return 日记对象列表
     */
    @Override
    public List<Diary> getUserDiaries(Long userId) {
        return diaryRepository.findByUserIdOrderByDateDesc(userId);
    }

    /**
     * 获取指定用户在特定时间段内的日记条目，按日期降序排列。
     *
     * @param userId 用户ID
     * @param startDate 开始日期
     * @param endDate 结束日期
     * @return 日记对象列表
     */
    @Override
    public List<Diary> getUserDiariesBetween(Long userId, LocalDateTime startDate, LocalDateTime endDate) {
        return diaryRepository.findByUserIdAndDateBetweenOrderByDateDesc(userId, startDate, endDate);
    }

    /**
     * 删除指定ID的日记条目，并删除其关联的所有图片文件。
     *
     * @param diaryId 日记ID
     * @throws ResourceNotFoundException 如果日记未找到
     */
    @Override
    @Transactional(rollbackFor = {RuntimeException.class})
    public void deleteDiary(Long diaryId) {
        Diary diary = getDiary(diaryId);

        /* 删除相关的图片文件 */
        for (DiaryImage image : diary.getImages()) {
            try {
                fileStorageService.deleteFile(image.getPath());
            } catch (IOException e) {
                logger.error("Failed to delete image file: {}", image.getPath(), e);
                throw new RuntimeException("Failed to delete image file", e);
            }
        }

        diaryRepository.delete(diary);
    }

    /**
     * 删除指定ID的日记图片，并从文件系统中移除对应的文件。
     *
     * @param imageId 图片ID
     * @throws ResourceNotFoundException 如果图片未找到
     * @throws FileDeletionException 如果文件删除失败
     */
    @Override
    @Transactional(rollbackFor = {ResourceNotFoundException.class, FileDeletionException.class})
    public void deleteDiaryImage(Long imageId) {
        DiaryImage image = diaryImageRepository.findById(imageId)
                .orElseThrow(() -> new ResourceNotFoundException("Diary image not found"));

        try {
            fileStorageService.deleteFile(image.getPath());
        } catch (IOException e) {
            throw new FileDeletionException("Failed to delete image file", e);
        }

        diaryImageRepository.delete(image);
    }
}