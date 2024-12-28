package com.flechazo.jnyzdairy.entity;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * 日记图片实体类，用于表示存储在数据库中的日记图片信息。
 *
 * @author Flechazo
 */
@Data
@Entity
@Table(name = "diary_images")
public class DiaryImage {

    /**
     * 图片的唯一标识符。
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * 关联的日记条目，每个日记图片都属于一个特定的日记条目，不能为空。
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "diary_id", nullable = false)
    private Diary diary;

    /**
     * 图片的存储路径，指向图片文件在服务器上的位置，不能为空。
     */
    @Column(nullable = false)
    private String path;

    /**
     * 图片的描述或标题，可选字段，用户可以为图片添加说明文字。
     */
    private String caption;

    /**
     * 是否作为预览图，默认值为{@code false}。如果设置为{@code true}，则该图片将被用作日记条目的预览图。
     */
    @Column(name = "is_preview")
    private boolean isPreview;

    /**
     * 创建时间戳，记录图片创建的时间。
     */
    @Column(name = "created_at")
    private LocalDateTime createdAt;

    /**
     * 在持久化之前自动设置创建时间。
     */
    @PrePersist
    protected void onCreate() {
        /* 设置创建时间为当前时间 */
        createdAt = LocalDateTime.now();
    }
}