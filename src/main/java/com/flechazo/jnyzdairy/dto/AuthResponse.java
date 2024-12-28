package com.flechazo.jnyzdairy.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * 认证响应DTO，用于封装认证成功后的响应信息。
 *
 * @author Flechazo
 */
@Data
@AllArgsConstructor
public class AuthResponse {

    /**
     * JWT令牌，用户认证通过后生成的访问令牌。
     */
    private String token;

    /**
     * 用户角色，表示用户在系统中的权限级别。
     */
    private String role;

    /**
     * 用户ID，标识用户的唯一编号。
     */
    private Long userId;
}