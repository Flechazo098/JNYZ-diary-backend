package com.flechazo.jnyzdairy.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import com.fasterxml.jackson.annotation.JsonIgnore;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * 用户实体类，用于表示存储在数据库中的用户信息。
 *
 * @author Flechazo
 */
@Data
@Entity
@Table(name = "users")
@NoArgsConstructor
@AllArgsConstructor
public class User {

    /**
     * 用户的唯一标识符。
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * 用户名，作为用户的登录标识，不能为空且必须唯一。
     */
    @Column(nullable = false, unique = true)
    private String username;

    /**
     * 密码，用于用户认证，不能为空。
     */
    @Column(nullable = false)
    private String password;

    /**
     * 电子邮件地址，作为用户的联系信息，不能为空且必须唯一。
     */
    @Column(nullable = false, unique = true)
    private String email;

    /**
     * 显示名称，用户在系统中展示的名字，不能为空。
     */
    @Column(name = "display_name", nullable = false)
    private String displayName;

    /**
     * 头像路径，指向用户头像的存储位置。
     */
    @Column(name = "avatar_path")
    private String avatarPath;

    /**
     * 用户角色，表示用户在系统中的权限级别，默认为普通用户<b>（USER）</b>。
     */
    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private UserRole role = UserRole.USER;

    /**
     * 用户状态，表示用户的当前状态，默认为激活状态<b>（ACTIVE）</b>。
     */
    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private UserStatus status = UserStatus.ACTIVE;

    /**
     * 最后一次登录时间，记录用户最近一次登录的时间。
     */
    @Column(name = "last_login")
    private LocalDateTime lastLogin;

    /**
     * 创建时间戳，记录用户创建的时间。
     */
    @Column(name = "created_at")
    private LocalDateTime createdAt;

    /**
     * 更新时间戳，记录用户信息最近一次更新的时间。
     */
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    /**
     * 用户关联的日记条目列表，使用级联操作以确保当删除用户时关联的日记也被删除。
     * <p>
     * 使用 {@code @JsonIgnore} 注解防止序列化时出现循环引用。
     */
    @JsonIgnore
    @OneToMany(mappedBy = "user", fetch = FetchType.LAZY)
    private List<Diary> diaries = new ArrayList<>();

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