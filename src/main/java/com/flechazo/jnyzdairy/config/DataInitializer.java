package com.flechazo.jnyzdairy.config;


import com.flechazo.jnyzdairy.entity.User;
import com.flechazo.jnyzdairy.entity.UserRole;
import com.flechazo.jnyzdairy.entity.UserStatus;
import com.flechazo.jnyzdairy.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

/**
 * 数据初始化器
 * 用于创建管理员账号
 */
@Component
public class DataInitializer implements CommandLineRunner {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public DataInitializer(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public void run(String... args) {
        // 检查管理员账号是否存在
        if (!userRepository.existsByUsername("admin")) {
            // 创建管理员账号
            User admin = new User();
            admin.setUsername("admin");
            admin.setPassword(passwordEncoder.encode("admin123"));
            admin.setEmail("admin@example.com");
            admin.setDisplayName("Administrator");
            admin.setRole(UserRole.ADMIN);
            admin.setStatus(UserStatus.ACTIVE);
            
            userRepository.save(admin);
        }
    }
} 