package com.flechazo.jnyzdairy.repository;

import com.flechazo.jnyzdairy.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

/**
 * 用户数据访问接口，用于定义对用户实体进行数据库操作的方法。
 *
 * @author Flechazo
 */
@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    /**
     * 根据用户名查找用户。
     *
     * @param username 用户名
     * @return 包含匹配用户的Optional对象，如果没有找到则为空
     */
    Optional<User> findByUsername(String username);

    /**
     * 根据邮箱查找用户。
     *
     * @param email 邮箱地址
     * @return 包含匹配用户的Optional对象，如果没有找到则为空
     */
    Optional<User> findByEmail(String email);

    /**
     * 检查用户名是否存在。
     *
     * @param username 用户名
     * @return 如果存在返回true，否则返回false
     */
    boolean existsByUsername(String username);

    /**
     * 检查邮箱是否存在。
     *
     * @param email 邮箱地址
     * @return 如果存在返回true，否则返回false
     */
    boolean existsByEmail(String email);
}