package com.flechazo.jnyzdairy.controller;

import com.flechazo.jnyzdairy.entity.User;
import com.flechazo.jnyzdairy.service.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

/**
 * 用户控制器
 */
@RestController
@RequestMapping("/api/users")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    /**
     * 获取当前用户信息
     */
    @GetMapping("/me")
    public ResponseEntity<User> getCurrentUser(@RequestAttribute("userId") Long userId) {
        User user = userService.findById(userId);
        return ResponseEntity.ok(user);
    }

    /**
     * 更新用户信息
     */
    @PutMapping("/{id}")
    @PreAuthorize("@securityService.isCurrentUser(#id) or hasRole('ADMIN')")
    public ResponseEntity<User> updateUser(@PathVariable Long id, @RequestBody User user) {
        User updatedUser = userService.updateUser(id, user);
        return ResponseEntity.ok(updatedUser);
    }

    /**
     * 更新用户头像
     */
    @PostMapping("/{id}/avatar")
    @PreAuthorize("@securityService.isCurrentUser(#id) or hasRole('ADMIN')")
    public ResponseEntity<User> updateAvatar(@PathVariable Long id, @RequestParam("file") MultipartFile file) {
        // TODO: 实现文件上传逻辑
        String avatarPath = ""; // 文件上传后的路径
        User updatedUser = userService.updateAvatar(id, avatarPath);
        return ResponseEntity.ok(updatedUser);
    }

    /**
     * 获取所有用户列表（仅管理员）
     */
    @GetMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<User>> getAllUsers() {
        List<User> users = userService.findAllUsers();
        return ResponseEntity.ok(users);
    }

    /**
     * 删除用户（仅管理员）
     */
    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> deleteUser(@PathVariable Long id) {
        userService.deleteUser(id);
        return ResponseEntity.ok().build();
    }
} 