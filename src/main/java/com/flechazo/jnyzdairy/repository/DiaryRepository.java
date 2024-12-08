package com.flechazo.jnyzdairy.repository;

import com.flechazo.jnyzdairy.entity.Diary;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

/**
 * 日记数据访问接口
 */
@Repository
public interface DiaryRepository extends JpaRepository<Diary, Long> {
    
    /**
     * 根据用户ID和日期查找日记
     */
    Optional<Diary> findByUserIdAndDate(Long userId, LocalDateTime date);
    
    /**
     * 查找用户的所有日记
     */
    List<Diary> findByUserIdOrderByDateDesc(Long userId);
    
    /**
     * 查找指定日期范围内的日记
     */
    List<Diary> findByUserIdAndDateBetweenOrderByDateDesc(
        Long userId, LocalDateTime startDate, LocalDateTime endDate);
} 