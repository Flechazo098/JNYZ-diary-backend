package com.flechazo.jnyzdairy.dto;

import lombok.Data;

import jakarta.validation.constraints.NotBlank;

/**
 * 认证请求DTO，用于封装认证请求中的用户名和密码。
 *
 * @author Flechazo
 */
@Data
public class AuthRequest {

    /**
     * 用户名，不能为空。
     */
    @NotBlank(message = "用户名不能为空")
    private String username;

    /**
     * 密码，不能为空。
     */
    @NotBlank(message = "密码不能为空")
    private String password;
}