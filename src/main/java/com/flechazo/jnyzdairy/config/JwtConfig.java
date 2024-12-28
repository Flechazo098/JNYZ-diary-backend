package com.flechazo.jnyzdairy.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

/**
 * JWT配置类，用于从<b>application.yml</b>文件件中加载JWT相关的配置。
 * <p>该类通过@ConfigurationProperties注解指定前缀来绑定配置文件中的属性。
 *
 * @author Flechazo
 */
@Data
@Configuration
@ConfigurationProperties(prefix = "app.jwt")
public class JwtConfig {

    /**
     * JWT密钥，用于签名和验证JWT令牌。
     * <p>此密钥应当保密并妥善保管，以防止未授权的访问。
     */
    private String secret;

    /**
     * JWT过期时间（毫秒），定义了令牌的有效期限。
     * <p>超过此时间后，令牌将被视为无效，需要重新生成。
     */
    private long expiration;
}