package com.flechazo.jnyzdairy.config;

import com.flechazo.jnyzdairy.entity.User;
import com.flechazo.jnyzdairy.entity.UserRole;
import com.flechazo.jnyzdairy.entity.UserStatus;
import com.flechazo.jnyzdairy.repository.UserRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

/**
 * 数据初始化器，在应用程序启动时用于创建必要的基础数据。
 * 特别是它会检查并创建一个管理员账号（如果该账号不存在），
 * 以确保系统至少有一个超级用户可以进行管理操作。
 *
 * @author Flechazo
 */
@Component
public class DataInitializer implements CommandLineRunner {

    /**
     * 管理员用户名的常量定义。
     */
    private static final String ADMIN_USERNAME = "admin";

    /**
     * 用户仓库依赖注入，用于与数据库交互。
     */
    private final UserRepository userRepository;

    /**
     * 密码编码器依赖注入，用于加密用户密码。
     */
    private final PasswordEncoder passwordEncoder;

    /**
     * 构造函数，通过构造器注入的方式获取所需的依赖。
     *
     * @param userRepository  用于执行用户的CRUD操作
     * @param passwordEncoder 用于加密用户密码
     */
    public DataInitializer(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    /**
     * 实现CommandLineRunner接口的run方法，在应用启动后执行此方法。
     * 方法的主要目的是检查是否存在预设的管理员账号，
     * 如果不存在，则创建一个新的管理员账号。
     *
     * @param args 启动参数，这里并未使用
     */
    @Override
    public void run(String... args) {
        /*
         * 检查是否已存在指定用户名的管理员账号。
         * 如果不存在则进入创建流程。
         */
        if (!userRepository.existsByUsername(ADMIN_USERNAME)) {
            // 创建管理员账号实例，并设置相关属性

            User admin = new User();
            // 设置管理员用户名
            admin.setUsername(ADMIN_USERNAME);
            // 对初始密码进行加密处理
            admin.setPassword(passwordEncoder.encode("admin123"));
            // 设置管理员邮箱
            admin.setEmail("admin@example.com");
            // 设置显示名称
            admin.setDisplayName("Administrator");
            // 分配管理员角色
            admin.setRole(UserRole.ADMIN);
            // 设置账户状态为激活
            admin.setStatus(UserStatus.ACTIVE);

            // 将新创建的管理员账号保存到数据库中
            userRepository.save(admin);
        }
    }
}