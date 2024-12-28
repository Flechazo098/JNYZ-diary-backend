package com.flechazo.jnyzdairy.util;

import com.flechazo.jnyzdairy.exception.UnauthorizedException;
import com.flechazo.jnyzdairy.security.CustomUserDetails;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

/**
 * 安全工具类，提供获取当前用户信息的方法。
 * <p>
 * 该工具类用于从Spring Security上下文中提取当前认证用户的信息，
 * 包括用户的ID和用户名。如果用户未登录或会话已过期，则抛出相应的异常。
 *
 * @author Flechazo
 */
public class SecurityUtils {

    /**
     * 获取当前用户ID。
     * <p>
     * 该方法从Spring Security的上下文中获取当前认证对象，并检查其是否为 {@code CustomUserDetails} 类型，
     * 然后返回用户的ID。如果用户未登录或会话已过期，则抛出 {@link UnauthorizedException} 异常。
     *
     * @return 当前用户的ID
     * @throws UnauthorizedException 如果用户未登录或会话已过期
     */
    public static Long getCurrentUserId() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.isAuthenticated()) {
            Object principal = authentication.getPrincipal();
            if (principal instanceof CustomUserDetails) {
                return ((CustomUserDetails) principal).getId();
            }
        }
        throw new UnauthorizedException("用户未登录或会话已过期");
    }

    /**
     * 获取当前用户名。
     * <p>
     * 该方法从Spring Security的上下文中获取当前认证对象，并检查其是否为 {@code CustomUserDetails} 类型，
     * 然后返回用户的用户名。如果用户未登录或会话已过期，则抛出 {@link UnauthorizedException} 异常。
     *
     * @return 当前用户的用户名
     * @throws UnauthorizedException 如果用户未登录、会话已过期或用户信息无效
     */
    public static String getCurrentUsername() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !authentication.isAuthenticated()) {
            throw new UnauthorizedException("用户未登录或会话已过期");
        }

        Object principal = authentication.getPrincipal();
        if (principal instanceof CustomUserDetails) {
            return ((CustomUserDetails) principal).getUsername();
        }
        throw new UnauthorizedException("无效的用户信息");
    }
}