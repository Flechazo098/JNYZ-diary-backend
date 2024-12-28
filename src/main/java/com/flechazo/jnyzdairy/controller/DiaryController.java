package com.flechazo.jnyzdairy.controller;

import com.flechazo.jnyzdairy.entity.Diary;
import com.flechazo.jnyzdairy.service.DiaryService;
import com.flechazo.jnyzdairy.util.SecurityUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;
import java.util.List;

/**
 * 日记控制器，用于处理日记相关的HTTP请求。
 *
 * @author Flechazo
 */
@RestController
@RequestMapping("/api/diaries")
@RequiredArgsConstructor
public class DiaryController {

    /**
     * 日记服务，提供日记的创建、更新、查询和删除功能。
     */
    private final DiaryService diaryService;

    /**
     * 创建日记。
     * <p>
     * 该方法接收日记内容及可选图片文件，为当前已认证用户创建新的日记条目。
     *
     * @param date   日记日期
     * @param content 日记内容
     * @param images  可选的日记图片列表
     * @return 包含新创建日记信息的响应实体
     */
    @PostMapping
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<Diary> createDiary(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime date,
            @RequestParam String content,
            @RequestParam(required = false) List<MultipartFile> images) {
        Long userId = SecurityUtils.getCurrentUserId();
        Diary diary = diaryService.createDiary(userId, date, content, images);
        return ResponseEntity.ok(diary);
    }

    /**
     * 更新日记。
     * <p>
     * 该方法根据提供的ID更新指定的日记条目内容及图片。
     *
     * @param id        日记ID
     * @param content   新的日志内容
     * @param newImages 新的日记图片列表
     * @return 包含更新后日记信息的响应实体
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
     * 获取日记。
     * <p>
     * 该方法根据提供的ID获取指定的日记条目。
     *
     * @param id 日记ID
     * @return 包含日记信息的响应实体
     */
    @GetMapping("/{id}")
    @PreAuthorize("@securityService.isOwner(#id)")
    public ResponseEntity<Diary> getDiary(@PathVariable Long id) {
        Diary diary = diaryService.getDiary(id);
        return ResponseEntity.ok(diary);
    }

    /**
     * 获取指定日期的日记。
     * <p>
     * 该方法根据提供的日期获取当前已认证用户的日记条目。
     *
     * @param date 指定的日记日期
     * @return 包含日记信息的响应实体
     */
    @GetMapping("/by-date")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<Diary> getDiaryByDate(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime date) {
        Long userId = SecurityUtils.getCurrentUserId();
        Diary diary = diaryService.getDiaryByDate(userId, date);
        return ResponseEntity.ok(diary);
    }

    /**
     * 获取日期范围内的日记。
     * <p>
     * 该方法根据提供的起始和结束日期获取当前已认证用户的日记列表。
     *
     * @param startDate 起始日期（可选）
     * @param endDate   结束日期（可选）
     * @return 包含日记列表的响应实体
     */
    @GetMapping
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<List<Diary>> getDiaries(
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime startDate,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime endDate) {
        /* 获取当前用户ID */
        Long userId = SecurityUtils.getCurrentUserId();
        List<Diary> diaries;
        /* 如果起始日期和结束日期都存在，则根据日期范围获取日记列表；否则，获取当前用户的所有日记列表 */
        if (startDate != null && endDate != null) {
            diaries = diaryService.getUserDiariesBetween(userId, startDate, endDate);
        } else {
            diaries = diaryService.getUserDiaries(userId);
        }
        /* 返回日记列表 */
        return ResponseEntity.ok(diaries);
    }

    /**
     * 删除日记。
     * <p>
     * 该方法根据提供的ID删除指定的日记条目。
     *
     * @param id 日记ID
     * @return 空响应体表示操作成功
     */
    @DeleteMapping("/{id}")
    @PreAuthorize("@securityService.isOwner(#id)")
    public ResponseEntity<?> deleteDiary(@PathVariable Long id) {
        diaryService.deleteDiary(id);
        return ResponseEntity.ok().build();
    }

    /**
     * 删除日记图片。
     * <p>
     * 该方法根据提供的图片ID删除指定的日记图片。
     *
     * @param imageId 图片ID
     * @return 空响应体表示操作成功
     */
    @DeleteMapping("/images/{imageId}")
    @PreAuthorize("@securityService.isImageOwner(#imageId)")
    public ResponseEntity<?> deleteImage(@PathVariable Long imageId) {
        diaryService.deleteDiaryImage(imageId);
        return ResponseEntity.ok().build();
    }
}