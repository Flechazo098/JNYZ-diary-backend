package com.flechazo.jnyzdairy.filter;

import com.flechazo.jnyzdairy.util.JwtUtil;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.lang.NonNull;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

/**
 * JWT认证过滤器，用于拦截请求并验证JWT令牌。
 *
 * @author Flechazo
 */
public class JwtAuthenticationFilter extends OncePerRequestFilter {
    /**
     * Bearer Token的前缀，用于在HTTP头部中标识JWT令牌
     */
    private static final String BEARER_PREFIX = "Bearer ";

    /**
     * 注入JwtUtil工具类，用于JWT令牌的解析和验证
     */
    private final JwtUtil jwtUtil;

    /**
     * 注入UserDetailsService接口的实现类，用于加载用户详细信息
     */
    private final UserDetailsService userDetailsService;

    /**
     * 日志记录器，用于记录系统日志
     */
    private static final Logger logger = LoggerFactory.getLogger(JwtAuthenticationFilter.class);

    /**
     * 构造函数，初始化JWT工具和用户详情服务。
     *
     * @param jwtUtil            用于解析和验证JWT的工具类
     * @param userDetailsService 用户详情服务，用于加载用户信息
     */
    public JwtAuthenticationFilter(JwtUtil jwtUtil, UserDetailsService userDetailsService) {
        this.jwtUtil = jwtUtil;
        this.userDetailsService = userDetailsService;
    }

    /**
     * 执行实际的过滤逻辑
     *
     * @param request 请求对象，用于获取请求头中的JWT令牌
     * @param response 响应对象，用于对请求进行响应
     * @param filterChain 过滤链，用于将请求传递给下一个过滤器或目标资源
     * @throws ServletException 如果过滤过程中发生Servlet异常
     * @throws IOException 如果过滤过程中发生IO异常
     */
    @Override
    protected void doFilterInternal(
            @NonNull HttpServletRequest request,
            @NonNull HttpServletResponse response,
            @NonNull FilterChain filterChain) throws ServletException, IOException {
        try {
            /* 从请求中提取JWT令牌 */
            String token = extractToken(request);
            /* 验证令牌有效性 */
            if (token != null && jwtUtil.validateToken(token)) {
                /* 从令牌中提取用户名 */
                String username = jwtUtil.extractUsername(token);
                /* 检查是否有有效用户且未被认证 */
                if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {
                    /* 加载用户详细信息 */
                    UserDetails userDetails = userDetailsService.loadUserByUsername(username);
                    /* 验证令牌与用户信息匹配 */
                    if (jwtUtil.validateToken(token, userDetails)) {
                        /* 创建认证令牌并设置用户权限 */
                        UsernamePasswordAuthenticationToken authentication =
                                new UsernamePasswordAuthenticationToken(
                                        userDetails,
                                        null,
                                        userDetails.getAuthorities()
                                );
                        /* 设置认证详细信息 */
                        authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                        /* 将认证信息存入安全上下文 */
                        SecurityContextHolder.getContext().setAuthentication(authentication);
                        /* 记录成功认证日志 */
                        logger.debug("Successfully authenticated user: {}", username);
                    }
                }
            }
        } catch (Exception e) {
            /* 记录认证失败日志 */
            logger.error("Cannot set user authentication", e);
        }

        /* 继续执行过滤链 */
        filterChain.doFilter(request, response);
    }


    /**
     * 从HTTP请求头中提取JWT令牌。
     *
     * @param request HTTP请求
     * @return 如果存在有效的授权头，则返回JWT令牌；否则返回null
     */
    private String extractToken(HttpServletRequest request) {
        String header = request.getHeader("Authorization");
        if (header != null && header.startsWith(BEARER_PREFIX)) {
            return header.substring(BEARER_PREFIX.length());
        }
        return null;
    }
}