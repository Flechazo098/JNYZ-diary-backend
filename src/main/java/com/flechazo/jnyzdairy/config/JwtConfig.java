package com.flechazo.jnyzdairy.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

/**
 * JWT配置类
 */
@Data
@Configuration
@ConfigurationProperties(prefix = "app.jwt")
public class JwtConfig {
    /**
     * JWT密钥
     */
    private String secret;
    
    /**
     * JWT过期时间(毫秒)
     */
    private long expiration;
} 