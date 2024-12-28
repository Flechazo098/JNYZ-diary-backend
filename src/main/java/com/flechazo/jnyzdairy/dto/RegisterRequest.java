package com.flechazo.jnyzdairy.dto;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

/**
 * 注册请求DTO，用于封装用户注册时提交的信息。
 *
 * @author Flechazo
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class RegisterRequest {

    /**
     * 用户名，不能为空且长度必须在2-20个字符之间。
     */
    @NotBlank(message = "用户名不能为空")
    @Size(min = 2, max = 20, message = "用户名长度必须在2-20之间")
    private String username;

    /**
     * 密码，不能为空且长度必须在6-20个字符之间。
     */
    @NotBlank(message = "密码不能为空")
    @Size(min = 6, max = 20, message = "密码长度必须在6-20之间")
    private String password;

    /**
     * 邮箱地址，不能为空且需符合邮箱格式。
     */
    @NotBlank(message = "邮箱不能为空")
    @Email(message = "邮箱格式不正确")
    private String email;

    /**
     * 显示名称，长度必须在2-20个字符之间。
     */
    @Size(min = 2, max = 20, message = "显示名称长度必须在2-20之间")
    private String displayName;

    /**
     * 验证码，不能为空，用于验证用户输入的验证码是否正确。
     */
    @NotBlank(message = "验证码不能为空")
    private String captcha;
}