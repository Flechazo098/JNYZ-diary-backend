package com.flechazo.jnyzdairy.controller;

import com.flechazo.jnyzdairy.entity.Diary;
import com.flechazo.jnyzdairy.service.DiaryService;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;
import java.util.List;

/**
 * 日记控制器
 */
@RestController
@RequestMapping("/api/diaries")
public class DiaryController {

private final DiaryService diaryService;

public DiaryController(DiaryService diaryService) {
    this.diaryService = diaryService;
}

    /**
     * 创建日记
     */
    @PostMapping
    public ResponseEntity<Diary> createDiary(
            @RequestAttribute("userId") Long userId,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime date,
            @RequestParam String content,
            @RequestParam(required = false) List<MultipartFile> images) {
        
        Diary diary = diaryService.createDiary(userId, date, content, images);
        return ResponseEntity.ok(diary);
    }

    /**
     * 更新日记
     */
    @PutMapping("/{id}")
    @PreAuthorize("@securityService.isOwner(#id)")
    public ResponseEntity<Diary> updateDiary(
            @PathVariable Long id,
            @RequestParam String content,
            @RequestParam(required = false) List<MultipartFile> newImages) {
        
        Diary diary = diaryService.updateDiary(id, content, newImages);
        return ResponseEntity.ok(diary);
    }

    /**
     * 获取日记
     */
    @GetMapping("/{id}")
    @PreAuthorize("@securityService.isOwner(#id)")
    public ResponseEntity<Diary> getDiary(@PathVariable Long id) {
        Diary diary = diaryService.getDiary(id);
        return ResponseEntity.ok(diary);
    }

    /**
     * 获取指定日期的日记
     */
    @GetMapping("/by-date")
    public ResponseEntity<Diary> getDiaryByDate(
            @RequestAttribute("userId") Long userId,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime date) {
        
        Diary diary = diaryService.getDiaryByDate(userId, date);
        return ResponseEntity.ok(diary);
    }

    /**
     * 获取日期范围内的日记
     */
    @GetMapping
    public ResponseEntity<List<Diary>> getDiaries(
            @RequestAttribute("userId") Long userId,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime startDate,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime endDate) {
        
        List<Diary> diaries;
        if (startDate != null && endDate != null) {
            diaries = diaryService.getUserDiariesBetween(userId, startDate, endDate);
        } else {
            diaries = diaryService.getUserDiaries(userId);
        }
        return ResponseEntity.ok(diaries);
    }

    /**
     * 删除日记
     */
    @DeleteMapping("/{id}")
    @PreAuthorize("@securityService.isOwner(#id)")
    public ResponseEntity<?> deleteDiary(@PathVariable Long id) {
        diaryService.deleteDiary(id);
        return ResponseEntity.ok().build();
    }

    /**
     * 删除日记图片
     */
    @DeleteMapping("/images/{imageId}")
    @PreAuthorize("@securityService.isImageOwner(#imageId)")
    public ResponseEntity<?> deleteImage(@PathVariable Long imageId) {
        diaryService.deleteDiaryImage(imageId);
        return ResponseEntity.ok().build();
    }
} 