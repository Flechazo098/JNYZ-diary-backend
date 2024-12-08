package com.flechazo.jnyzdairy.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * 认证响应DTO
 */
@Data
@AllArgsConstructor
public class AuthResponse {
    /**
     * JWT令牌
     */
    private String token;
    
    /**
     * 用户角色
     */
    private String role;
    
    /**
     * 用户ID
     */
    private Long userId;
} 