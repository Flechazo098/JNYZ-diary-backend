package com.flechazo.jnyzdairy.service;

import com.flechazo.jnyzdairy.entity.Diary;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;
import java.util.List;

/**
 * 日记服务接口
 */
public interface DiaryService {
    
    /**
     * 创建日记
     */
    Diary createDiary(Long userId, LocalDateTime date, String content, List<MultipartFile> images);
    
    /**
     * 更新日记
     */
    Diary updateDiary(Long diaryId, String content, List<MultipartFile> newImages);
    
    /**
     * 获取日记
     */
    Diary getDiary(Long diaryId);
    
    /**
     * 获取用户指定日期的日记
     */
    Diary getDiaryByDate(Long userId, LocalDateTime date);
    
    /**
     * 获取用户的所有日记
     */
    List<Diary> getUserDiaries(Long userId);
    
    /**
     * 获取用户指定日期范围的日记
     */
    List<Diary> getUserDiariesBetween(Long userId, LocalDateTime startDate, LocalDateTime endDate);
    
    /**
     * 删除日记
     */
    void deleteDiary(Long diaryId);
    
    /**
     * 删除日记图片
     */
    void deleteDiaryImage(Long imageId);
} 