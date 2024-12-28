package com.flechazo.jnyzdairy.entity;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * 日记实体类，用于表示存储在数据库中的日记条目信息。
 *
 * @author Flechazo
 */
@Data
@Entity
@Table(name = "diary_entries")
public class Diary {

    /**
     * 日记条目的唯一标识符。
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * 关联的用户，每个日记条目都属于一个特定的用户，不能为空。
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    /**
     * 日记条目的日期时间，记录日记创建的时间，不能为空。
     */
    @Column(nullable = false)
    private LocalDateTime date;

    /**
     * 日记内容，可以是较长的文本内容。
     */
    @Column(columnDefinition = "TEXT")
    private String content;

    /**
     * 与该日记条目关联的图片列表，使用级联操作以确保当删除日记时关联的图片也被删除。
     */
    @OneToMany(mappedBy = "diary", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<DiaryImage> images = new ArrayList<>();

    /**
     * 创建时间戳，记录日记条目创建的时间。
     */
    @Column(name = "created_at")
    private LocalDateTime createdAt;

    /**
     * 更新时间戳，记录日记条目最近一次更新的时间。
     */
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    /**
     * 在持久化之前自动设置创建时间和更新时间。
     */
    @PrePersist
    protected void onCreate() {
        /* 设置创建时间和更新时间为当前时间 */
        createdAt = LocalDateTime.now();
        updatedAt = LocalDateTime.now();
    }

    /**
     * 在更新之前自动设置更新时间。
     */
    @PreUpdate
    protected void onUpdate() {
        /* 更新时间为当前时间 */
        updatedAt = LocalDateTime.now();
    }
}