package com.flechazo.jnyzdairy.controller;

import com.flechazo.jnyzdairy.dto.AuthRequest;
import com.flechazo.jnyzdairy.dto.AuthResponse;
import com.flechazo.jnyzdairy.dto.RegisterRequest;
import com.flechazo.jnyzdairy.exception.InvalidCaptchaException;
import com.flechazo.jnyzdairy.service.AuthenticationService;
import com.flechazo.jnyzdairy.service.CaptchaService;
import com.flechazo.jnyzdairy.service.UserService;
import com.flechazo.jnyzdairy.entity.User;
import com.flechazo.jnyzdairy.dto.ErrorResponse;
import com.flechazo.jnyzdairy.dto.UserDTO;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;

/**
 * 认证控制器，用于处理用户认证相关的HTTP请求。
 *
 * @author Flechazo
 */
@RestController
@RequestMapping("/api/auth")
@Slf4j
public class AuthController {

    /**
     * 认证服务，提供用户登录、注册等认证功能。
     */
    private final AuthenticationService authenticationService;

    /**
     * 用户服务，提供用户信息查询等功能。
     */
    private final UserService userService;

    /**
     * 验证码服务，提供验证码生成和验证功能。
     */
    private final CaptchaService captchaService;

    /**
     * 构造函数，注入依赖的服务。
     *
     * @param authenticationService 认证服务
     * @param userService           用户服务
     * @param captchaService        验证码服务
     */
    public AuthController(AuthenticationService authenticationService,
                          UserService userService,
                          CaptchaService captchaService) {
        this.authenticationService = authenticationService;
        this.userService = userService;
        this.captchaService = captchaService;
    }

    /**
     * 用户登录接口。
     * <p>
     * 该方法接收用户的登录请求，并返回一个包含JWT令牌的响应。
     *
     * @param request 登录请求数据
     * @return 包含认证结果的响应实体
     */
    @PostMapping("/login")
    public ResponseEntity<AuthResponse> login(@Valid @RequestBody AuthRequest request) {
        AuthResponse response = authenticationService.login(request);
        return ResponseEntity.ok(response);
    }

    /**
     * 用户注册接口。
     * <p>
     * 该方法接收用户的注册请求，并验证验证码的正确性，然后完成用户注册流程。
     *
     * @param request 注册请求数据
     * @param session HTTP会话对象
     * @return 包含认证结果的响应实体
     * @throws InvalidCaptchaException 如果验证码不匹配，则抛出此异常
     */
    @PostMapping("/register")
    public ResponseEntity<AuthResponse> register(@Valid @RequestBody RegisterRequest request, HttpSession session) {
        String savedCode = (String) session.getAttribute("CAPTCHA_CODE");
        if (!request.getCaptcha().equalsIgnoreCase(savedCode)) {
            throw new InvalidCaptchaException();
        }

        session.removeAttribute("CAPTCHA_CODE");
        AuthResponse response = authenticationService.register(request);
        return ResponseEntity.ok(response);
    }

    /**
     * 检查用户名是否可用接口。
     * <p>
     * 该方法检查给定的用户名是否已经被占用。
     *
     * @param username 待检查的用户名
     * @return 包含检查结果的响应实体
     */
    @GetMapping("/check-username")
    public ResponseEntity<Boolean> checkUsername(@RequestParam String username) {
        return ResponseEntity.ok(userService.isUsernameAvailable(username));
    }

    /**
     * 检查邮箱是否可用接口。
     * <p>
     * 该方法检查给定的邮箱地址是否已经被占用。
     *
     * @param email 待检查的邮箱地址
     * @return 包含检查结果的响应实体
     */
    @GetMapping("/check-email")
    public ResponseEntity<Boolean> checkEmail(@RequestParam String email) {
        return ResponseEntity.ok(userService.isEmailAvailable(email));
    }

    /**
     * 获取验证码图片接口。
     * <p>
     * 该方法生成并返回一张验证码图片，并将其对应的文本存储在会话中。
     *
     * @param request HTTP请求对象
     * @return 包含验证码图片的响应实体
     */
    @GetMapping("/captcha")
    public ResponseEntity<byte[]> getCaptcha(HttpServletRequest request) {
        try {
            /* 生成验证码文本 */
            String captchaText = captchaService.generateCaptchaText();
            log.debug("Generated captcha text: {}", captchaText);

            /* 生成验证码图片 */
            byte[] captchaImage = captchaService.generateCaptchaImage(captchaText);
            log.debug("Generated captcha image size: {} bytes", captchaImage.length);

            /* 将验证码存入 session */
            HttpSession session = request.getSession(true);
            session.setAttribute("CAPTCHA_CODE", captchaText);
            log.debug("Stored captcha in session with id: {}", session.getId());

            /* 返回图片 */
            return ResponseEntity.ok()
                    .contentType(MediaType.IMAGE_JPEG)
                    .header(HttpHeaders.CACHE_CONTROL, "no-cache, no-store, must-revalidate")
                    .header(HttpHeaders.PRAGMA, "no-cache")
                    .header(HttpHeaders.EXPIRES, "0")
                    .body(captchaImage);
        } catch (Exception e) {
            log.error("Failed to generate captcha", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(("Failed to generate captcha: " + e.getMessage()).getBytes());
        }
    }

    /**
     * 获取当前登录用户信息接口。
     * <p>
     * 该方法返回当前已认证用户的信息。
     *
     * @param userDetails 已认证用户详情
     * @return 包含用户信息的响应实体
     */
    @GetMapping("/current-user")
    public ResponseEntity<?> getCurrentUser(@AuthenticationPrincipal UserDetails userDetails) {
        try {
            if (userDetails == null) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
            }
            User user = userService.findByUsername(userDetails.getUsername());
            return ResponseEntity.ok(UserDTO.fromEntity(user));
        } catch (Exception e) {
            log.error("Failed to get current user", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ErrorResponse("获取用户信息失败"));
        }
    }
}