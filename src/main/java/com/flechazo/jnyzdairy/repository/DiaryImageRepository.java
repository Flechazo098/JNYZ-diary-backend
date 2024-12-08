package com.flechazo.jnyzdairy.repository;

import com.flechazo.jnyzdairy.entity.DiaryImage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * 日记图片数据访问接口
 */
@Repository
public interface DiaryImageRepository extends JpaRepository<DiaryImage, Long> {
    
    /**
     * 查找日记的所有图片
     */
    List<DiaryImage> findByDiaryId(Long diaryId);
    
    /**
     * 删除日记的所有图片
     */
    void deleteByDiaryId(Long diaryId);
} 