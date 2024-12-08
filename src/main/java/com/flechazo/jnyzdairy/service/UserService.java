package com.flechazo.jnyzdairy.service;

import com.flechazo.jnyzdairy.entity.User;
import com.flechazo.jnyzdairy.dto.RegisterRequest;
import java.util.List;

/**
 * 用户服务接口
 */
public interface UserService {
    
    /**
     * 根据ID查找用户
     */
    User findById(Long id);
    
    /**
     * 根据用户名查找用户
     */
    User findByUsername(String username);
    
    /**
     * 创建新用户
     */
    User createUser(RegisterRequest registerRequest);
    
    /**
     * 更新用户信息
     */
    User updateUser(Long id, User user);
    
    /**
     * 删除用户
     */
    void deleteUser(Long id);
    
    /**
     * 获取所有用户列表
     */
    List<User> findAllUsers();
    
    /**
     * 更新用户头像
     */
    User updateAvatar(Long id, String avatarPath);
    
    /**
     * 检查用户名是否可用
     */
    boolean isUsernameAvailable(String username);
    
    /**
     * 检查邮箱是否可用
     */
    boolean isEmailAvailable(String email);
} 