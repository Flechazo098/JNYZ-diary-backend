package com.flechazo.jnyzdairy.service.impl;

import com.flechazo.jnyzdairy.entity.Diary;
import com.flechazo.jnyzdairy.entity.DiaryImage;
import com.flechazo.jnyzdairy.entity.User;
import com.flechazo.jnyzdairy.exception.ResourceNotFoundException;
import com.flechazo.jnyzdairy.repository.DiaryRepository;
import com.flechazo.jnyzdairy.repository.DiaryImageRepository;
import com.flechazo.jnyzdairy.repository.UserRepository;
import com.flechazo.jnyzdairy.service.DiaryService;
import com.flechazo.jnyzdairy.service.FileStorageService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;

@Service
public class DiaryServiceImpl implements DiaryService {

    private static final Logger logger = LoggerFactory.getLogger(DiaryServiceImpl.class);

    private final DiaryRepository diaryRepository;
    private final DiaryImageRepository diaryImageRepository;
    private final UserRepository userRepository;
    private final FileStorageService fileStorageService;

    @Autowired
    public DiaryServiceImpl(DiaryRepository diaryRepository, DiaryImageRepository diaryImageRepository, UserRepository userRepository, FileStorageService fileStorageService) {
        this.diaryRepository = diaryRepository;
        this.diaryImageRepository = diaryImageRepository;
        this.userRepository = userRepository;
        this.fileStorageService = fileStorageService;
    }

    @Override
    @Transactional
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
            diary = diaryRepository.save(diary);
        }

        return diary;
    }

    @Override
    @Transactional
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

    @Override
    public Diary getDiary(Long diaryId) {
        return diaryRepository.findById(diaryId)
                .orElseThrow(() -> new ResourceNotFoundException("Diary not found"));
    }

    @Override
    public Diary getDiaryByDate(Long userId, LocalDateTime date) {
        return diaryRepository.findByUserIdAndDate(userId, date)
                .orElseThrow(() -> new ResourceNotFoundException("Diary not found for the specified date"));
    }

    @Override
    public List<Diary> getUserDiaries(Long userId) {
        return diaryRepository.findByUserIdOrderByDateDesc(userId);
    }

    @Override
    public List<Diary> getUserDiariesBetween(Long userId, LocalDateTime startDate, LocalDateTime endDate) {
        return diaryRepository.findByUserIdAndDateBetweenOrderByDateDesc(userId, startDate, endDate);
    }

    @Override
    @Transactional
    public void deleteDiary(Long diaryId) {
        Diary diary = getDiary(diaryId);
        
        // 删除相关的图片文件
        for (DiaryImage image : diary.getImages()) {
            try {
                fileStorageService.deleteFile(image.getPath());
            } catch (IOException e) {
                // 记录错误但继续执行
                logger.error("Failed to delete image file: {}", image.getPath(), e);
                throw new RuntimeException("Failed to delete image file", e);
            }
        }
        
        diaryRepository.delete(diary);
    }

    @Override
    @Transactional
    public void deleteDiaryImage(Long imageId) {
        DiaryImage image = diaryImageRepository.findById(imageId)
                .orElseThrow(() -> new ResourceNotFoundException("Diary image not found"));
        
        try {
            fileStorageService.deleteFile(image.getPath());
        } catch (IOException e) {
            throw new RuntimeException("Failed to delete image file", e);
        }
        
        diaryImageRepository.delete(image);
    }
} 