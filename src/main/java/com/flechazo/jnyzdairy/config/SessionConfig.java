package com.flechazo.jnyzdairy.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.session.jdbc.config.annotation.web.http.EnableJdbcHttpSession;
import org.springframework.session.web.context.AbstractHttpSessionApplicationInitializer;

/**
 * 会话配置类，用于启用基于JDBC的HTTP会话管理。
 * <p>
 * 该类继承自 {@link AbstractHttpSessionApplicationInitializer}，
 * 并通过{@code @EnableJdbcHttpSession}注解来启用基于JDBC的分布式会话管理。
 * 这使得应用程序能够将用户的会话信息存储在数据库中，从而支持水平扩展和高可用性。
 *
 * @author Flechazo
 */
@Configuration
@EnableJdbcHttpSession

public class SessionConfig extends AbstractHttpSessionApplicationInitializer {
    /*
     * 如果需要自定义session配置，可以在这里添加相应的逻辑。
     * 例如：设置会话超时时间、配置会话事件监听器等。
     * 注意：此区域预留用于添加自定义会话配置逻辑。
     * 如果不需要额外配置，则可以保持为空。
     */
}