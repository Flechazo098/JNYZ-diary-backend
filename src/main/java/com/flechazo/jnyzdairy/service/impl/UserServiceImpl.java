package com.flechazo.jnyzdairy.service.impl;

import com.flechazo.jnyzdairy.dto.RegisterRequest;
import com.flechazo.jnyzdairy.entity.User;
import com.flechazo.jnyzdairy.entity.UserRole;
import com.flechazo.jnyzdairy.entity.UserStatus;
import com.flechazo.jnyzdairy.exception.ResourceNotFoundException;
import com.flechazo.jnyzdairy.exception.UserAlreadyExistsException;
import com.flechazo.jnyzdairy.repository.UserRepository;
import com.flechazo.jnyzdairy.service.UserService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * 用户服务实现类，提供用户管理相关功能。
 *
 * @author Flechazo
 */
@Service
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    /**
     * 构造函数，用于依赖注入。
     *
     * @param userRepository 用户仓库接口，用于与数据库交互
     * @param passwordEncoder 密码加密器，用于对用户密码进行加密处理
     */
    public UserServiceImpl(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    /**
     * 根据用户ID查找用户。
     *
     * @param id 用户ID
     * @return 查找到的用户对象
     * @throws ResourceNotFoundException 如果未找到对应的用户
     */
    @Override
    public User findById(Long id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with id: " + id));
    }

    /**
     * 根据用户名查找用户。
     *
     * @param username 用户名
     * @return 查找到的用户对象
     * @throws ResourceNotFoundException 如果未找到对应的用户
     */
    @Override
    public User findByUsername(String username) {
        return userRepository.findByUsername(username)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with username: " + username));
    }

    /**
     * 根据邮箱查找用户。
     *
     * @param email 邮箱地址
     * @return 查找到的用户对象
     * @throws ResourceNotFoundException 如果未找到对应的用户
     */
    @Override
    public User findByEmail(String email) {
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with email: " + email));
    }

    /**
     * 创建新用户。
     *
     * @param registerRequest 注册请求数据传输对象，包含创建用户所需的信息
     * @return 创建成功的用户对象
     * @throws UserAlreadyExistsException 如果用户名或邮箱已经存在
     */
    @Override
    @Transactional(rollbackFor = {UserAlreadyExistsException.class})
    public User createUser(RegisterRequest registerRequest) {
        /* 检查用户名和邮箱是否已存在 */
        if (userRepository.existsByUsername(registerRequest.getUsername())) {
            throw new UserAlreadyExistsException("用户名已存在");
        }
        if (userRepository.existsByEmail(registerRequest.getEmail())) {
            throw new UserAlreadyExistsException("邮箱已存在");
        }

        /* 如果displayName为空，使用username作为默认值 */
        String displayName = registerRequest.getDisplayName();
        if (displayName == null || displayName.trim().isEmpty()) {
            displayName = registerRequest.getUsername();
        }

        User user = new User();
        user.setUsername(registerRequest.getUsername());
        user.setPassword(passwordEncoder.encode(registerRequest.getPassword()));
        user.setEmail(registerRequest.getEmail());
        user.setDisplayName(displayName);
        user.setRole(UserRole.USER);
        user.setStatus(UserStatus.ACTIVE);

        return userRepository.save(user);
    }

    /**
     * 更新指定用户的资料信息。
     *
     * @param id 用户ID
     * @param userDetails 包含更新信息的用户对象
     * @return 更新后的用户对象
     * @throws ResourceNotFoundException 如果未找到对应的用户
     */
    @Override
    @Transactional(rollbackFor = {RuntimeException.class})
    public User updateUser(Long id, User userDetails) {
        User user = findById(id);

        user.setDisplayName(userDetails.getDisplayName());
        user.setEmail(userDetails.getEmail());
        if (userDetails.getPassword() != null) {
            user.setPassword(passwordEncoder.encode(userDetails.getPassword()));
        }

        return userRepository.save(user);
    }

    /**
     * 删除指定用户，实际上是将用户状态设置为删除。
     *
     * @param id 用户ID
     * @throws ResourceNotFoundException 如果未找到对应的用户
     */
    @Override
    @Transactional(rollbackFor = {RuntimeException.class})
    public void deleteUser(Long id) {
        User user = findById(id);
        user.setStatus(UserStatus.DELETED);
        userRepository.save(user);
    }

    /**
     * 获取所有用户列表。
     *
     * @return 用户对象列表
     */
    @Override
    public List<User> findAllUsers() {
        return userRepository.findAll();
    }

    /**
     * 更新用户的头像路径。
     *
     * @param id 用户ID
     * @param avatarPath 新的头像文件路径
     * @return 更新后的用户对象
     * @throws ResourceNotFoundException 如果未找到对应的用户
     */
    @Override
    @Transactional(rollbackFor = {RuntimeException.class})
    public User updateAvatar(Long id, String avatarPath) {
        User user = findById(id);
        user.setAvatarPath(avatarPath);
        return userRepository.save(user);
    }

    /**
     * 检查用户名是否可用。
     *
     * @param username 待检查的用户名
     * @return 如果用户名不存在，则返回true；否则返回false
     */
    @Override
    public boolean isUsernameAvailable(String username) {
        return !userRepository.existsByUsername(username);
    }

    /**
     * 检查邮箱是否可用。
     *
     * @param email 待检查的邮箱地址
     * @return 如果邮箱不存在，则返回true；否则返回false
     */
    @Override
    public boolean isEmailAvailable(String email) {
        return !userRepository.existsByEmail(email);
    }
}