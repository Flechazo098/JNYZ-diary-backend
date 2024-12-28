package com.flechazo.jnyzdairy.config;

import com.flechazo.jnyzdairy.filter.JwtAuthenticationFilter;
import com.flechazo.jnyzdairy.util.JwtUtil;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.filter.CorsFilter;

/**
 * 安全配置类，用于定义Spring Security相关的bean以及安全规则。
 * <p>
 * 该类通过@EnableWebSecurity启用Web安全配置，
 * 并通过@EnableMethodSecurity启用基于方法的安全性。
 *
 * @author Flechazo
 */
@Configuration
@EnableWebSecurity
@EnableMethodSecurity
public class SecurityConfig {

    /**
     * 用户详情服务，提供用户信息查询功能。
     */
    private final UserDetailsService userDetailsService;

    /**
     * JWT工具类，用于生成、解析和验证JWT令牌。
     */
    private final JwtUtil jwtUtil;

    /**
     * 跨域资源共享过滤器，用于处理CORS请求。
     */
    private final CorsFilter corsFilter;

    /**
     * 构造函数，注入依赖的服务和工具类。
     *
     * @param userDetailsService 用户详情服务
     * @param jwtUtil           JWT工具类
     * @param corsFilter        跨域资源共享过滤器
     */
    public SecurityConfig(UserDetailsService userDetailsService,
                          JwtUtil jwtUtil,
                          @Qualifier("customCorsFilter") CorsFilter corsFilter) {
        this.userDetailsService = userDetailsService;
        this.jwtUtil = jwtUtil;
        this.corsFilter = corsFilter;
    }

    /**
     * 配置Spring Security的过滤链。
     * <p>
     * 该方法配置了跨域请求、CSRF防护、会话管理、请求授权等安全规则，
     * 并添加了自定义的JWT认证过滤器。
     *
     * @param http HttpSecurity对象，用于配置安全规则
     * @return 配置好的SecurityFilterChain对象
     * @throws Exception 如果配置过程中发生错误
     */
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .addFilterBefore(corsFilter, UsernamePasswordAuthenticationFilter.class)
                // 禁用CSRF防护，因为本应用不使用基于表单的身份验证
                .csrf(AbstractHttpConfigurer::disable)
                // 设置会话创建策略为无状态，即不创建会话
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                // 配置请求授权规则
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers(
                                "/api/auth/login",
                                "/api/auth/register",
                                "/api/auth/captcha",
                                "/api/auth/check-username",
                                "/api/auth/check-email",
                                "/error"
                        ).permitAll() // 允许所有用户访问这些路径
                        .anyRequest().authenticated() // 所有其他请求都需要身份验证
                )
                // 在用户名密码认证过滤器之前添加JWT认证过滤器
                .addFilterBefore(jwtAuthenticationFilter(), UsernamePasswordAuthenticationFilter.class)
                // 异常处理：设置未认证请求的响应内容类型和状态码
                .exceptionHandling(exception -> exception
                        .authenticationEntryPoint((request, response, authException) -> {
                            response.setContentType("application/json;charset=UTF-8");
                            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                            response.getWriter().write("{\"error\":\"" + authException.getMessage() + "\"}");
                        })
                );

        return http.build();
    }

    /**
     * 创建并返回一个JWT认证过滤器实例。
     *
     * @return JwtAuthenticationFilter对象
     */
    @Bean
    public JwtAuthenticationFilter jwtAuthenticationFilter() {
        return new JwtAuthenticationFilter(jwtUtil, userDetailsService);
    }

    /**
     * 创建并返回一个密码编码器实例，用于加密和校验用户密码。
     *
     * @return PasswordEncoder对象
     */
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    /**
     * 获取认证管理器实例。
     *
     * @param config AuthenticationConfiguration对象
     * @return AuthenticationManager对象
     * @throws Exception 如果获取过程中发生错误
     */
    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
        return config.getAuthenticationManager();
    }
}