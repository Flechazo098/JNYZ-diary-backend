package com.flechazo.jnyzdairy.controller;

import com.flechazo.jnyzdairy.entity.User;
import com.flechazo.jnyzdairy.service.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

/**
 * 用户控制器，用于处理用户信息相关的HTTP请求。
 *
 * @author Flechazo
 */
@RestController
@RequestMapping("/api/users")
public class UserController {

    /**
     * 用户服务，提供用户的创建、更新、查询和删除功能。
     */
    private final UserService userService;

    /**
     * 构造函数，注入用户服务实例。
     *
     * @param userService 用户服务实例
     */
    public UserController(UserService userService) {
        this.userService = userService;
    }

    /**
     * 获取当前用户信息。
     * <p>
     * 该方法用于获取当前已认证用户的信息。
     *
     * @param userId 用户ID
     * @return 包含用户信息的响应实体
     */
    @GetMapping("/me")
    public ResponseEntity<User> getCurrentUser(@RequestAttribute("userId") Long userId) {
        User user = userService.findById(userId);
        return ResponseEntity.ok(user);
    }

    /**
     * 更新用户信息。
     * <p>
     * 该方法根据提供的ID更新指定用户的个人信息。仅限用户本人或管理员操作。
     *
     * @param id   用户ID
     * @param user 包含更新信息的用户对象
     * @return 包含更新后用户信息的响应实体
     */
    @PutMapping("/{id}")
    @PreAuthorize("@securityService.isCurrentUser(#id) or hasRole('ADMIN')")
    public ResponseEntity<User> updateUser(@PathVariable Long id, @RequestBody User user) {
        User updatedUser = userService.updateUser(id, user);
        return ResponseEntity.ok(updatedUser);
    }

    /**
     * 更新用户头像。
     * <p>
     * 该方法根据提供的ID更新指定用户的头像图片。仅限用户本人或管理员操作。
     *
     * @param id   用户ID
     * @param file 待上传的文件
     * @return 包含更新后用户信息的响应实体
     */
    @PostMapping("/{id}/avatar")
    @PreAuthorize("@securityService.isCurrentUser(#id) or hasRole('ADMIN')")
    public ResponseEntity<User> updateAvatar(@PathVariable Long id, @RequestParam("file") MultipartFile file) {
        // TODO: 实现文件上传逻辑
        // 文件上传后的路径
        String avatarPath = "";
        User updatedUser = userService.updateAvatar(id, avatarPath);
        return ResponseEntity.ok(updatedUser);
    }

    /**
     * 获取所有用户列表（仅管理员）。
     * <p>
     * 该方法返回系统中所有用户的列表，仅限管理员访问。
     *
     * @return 包含用户列表的响应实体
     */
    @GetMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<User>> getAllUsers() {
        List<User> users = userService.findAllUsers();
        return ResponseEntity.ok(users);
    }

    /**
     * 删除用户（仅管理员）。
     * <p>
     * 该方法根据提供的ID删除指定用户，仅限管理员操作。
     *
     * @param id 用户ID
     * @return 空响应体表示操作成功
     */
    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> deleteUser(@PathVariable Long id) {
        userService.deleteUser(id);
        return ResponseEntity.ok().build();
    }
}