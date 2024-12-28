package com.flechazo.jnyzdairy.service;

import com.flechazo.jnyzdairy.dto.RegisterRequest;
import com.flechazo.jnyzdairy.entity.User;

import java.util.List;

/**
 * 用户服务接口，提供用户管理相关功能。
 *
 * @author Flechazo
 */
public interface UserService {

    /**
     * 根据ID查找用户。
     * <p>
     * 该方法用于通过用户的唯一标识符（ID）来检索用户信息。
     *
     * @param id 用户的唯一标识符
     * @return 包含用户详细信息的 {@code User} 对象，如果未找到则返回 {@code null}
     */
    User findById(Long id);

    /**
     * 根据用户名查找用户。
     * <p>
     * 该方法用于通过用户名来检索用户信息。
     *
     * @param username 用户名
     * @return 包含用户详细信息的 {@code User} 对象，如果未找到则返回 {@code null}
     */
    User findByUsername(String username);

    /**
     * 根据邮箱查找用户。
     * <p>
     * 该方法用于通过用户的电子邮箱地址来检索用户信息。
     *
     * @param email 用户的电子邮箱地址
     * @return 包含用户详细信息的 {@code User} 对象，如果未找到则返回 {@code null}
     */
    User findByEmail(String email);

    /**
     * 创建新用户。
     * <p>
     * 该方法接收一个包含注册所需信息的请求对象，并创建一个新的用户记录。
     *
     * @param registerRequest 包含用户注册信息的请求对象
     * @return 新创建的 {@code User} 对象
     */
    User createUser(RegisterRequest registerRequest);

    /**
     * 更新用户信息。
     * <p>
     * 该方法允许更新指定ID的用户信息。需要注意的是，此方法不会修改用户的密码。
     *
     * @param id 用户的唯一标识符
     * @param user 包含更新后用户信息的对象
     * @return 更新后的 {@code User} 对象
     */
    User updateUser(Long id, User user);

    /**
     * 删除用户。
     * <p>
     * 该方法用于删除指定ID的用户记录。请注意，这也将导致与该用户关联的所有数据被删除。
     *
     * @param id 用户的唯一标识符
     */
    void deleteUser(Long id);

    /**
     * 获取所有用户列表。
     * <p>
     * 该方法返回系统中所有用户的列表。
     *
     * @return 包含所有用户信息的 {@code User} 对象列表
     */
    List<User> findAllUsers();

    /**
     * 更新用户头像。
     * <p>
     * 该方法用于更新指定ID的用户头像路径。
     *
     * @param id 用户的唯一标识符
     * @param avatarPath 新的头像文件路径
     * @return 更新后的 {@code User} 对象
     */
    User updateAvatar(Long id, String avatarPath);

    /**
     * 检查用户名是否可用。
     * <p>
     * 该方法用于验证给定的用户名是否已经被使用。如果用户名未被使用，则认为是可用的。
     *
     * @param username 要检查的用户名
     * @return 如果用户名可用则返回 {@code true}，否则返回 {@code false}
     */
    boolean isUsernameAvailable(String username);

    /**
     * 检查邮箱是否可用。
     * <p>
     * 该方法用于验证给定的电子邮箱地址是否已经被使用。如果邮箱未被使用，则认为是可用的。
     *
     * @param email 要检查的电子邮箱地址
     * @return 如果邮箱可用则返回 {@code true}，否则返回 {@code false}
     */
    boolean isEmailAvailable(String email);
}