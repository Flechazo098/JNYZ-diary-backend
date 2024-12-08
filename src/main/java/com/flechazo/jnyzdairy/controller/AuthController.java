package com.flechazo.jnyzdairy.controller;

import com.flechazo.jnyzdairy.dto.AuthRequest;
import com.flechazo.jnyzdairy.dto.AuthResponse;
import com.flechazo.jnyzdairy.dto.CaptchaResult;
import com.flechazo.jnyzdairy.dto.RegisterRequest;
import com.flechazo.jnyzdairy.exception.InvalidCaptchaException;
import com.flechazo.jnyzdairy.service.AuthenticationService;
import com.flechazo.jnyzdairy.service.CaptchaService;
import com.flechazo.jnyzdairy.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;
import javax.validation.Valid;

/**
 * 认证控制器
 */
@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final AuthenticationService authenticationService;
    private final UserService userService;
    private final CaptchaService captchaService;

    @Autowired
    public AuthController(AuthenticationService authenticationService, UserService userService, CaptchaService captchaService) {
        this.authenticationService = authenticationService;
        this.userService = userService;
        this.captchaService = captchaService;
    }

    /**
     * 用户登录
     */
    @PostMapping("/login")
    public ResponseEntity<AuthResponse> login(@Valid @RequestBody AuthRequest request) {
        AuthResponse response = authenticationService.login(request);
        return ResponseEntity.ok(response);
    }

    /**
     * 用户注册
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
     * 检查用户名是否可用
     */
    @GetMapping("/check-username")
    public ResponseEntity<Boolean> checkUsername(@RequestParam String username) {
        return ResponseEntity.ok(userService.isUsernameAvailable(username));
    }

    /**
     * 检查邮箱是否可用
     */
    @GetMapping("/check-email")
    public ResponseEntity<Boolean> checkEmail(@RequestParam String email) {
        return ResponseEntity.ok(userService.isEmailAvailable(email));
    }

    @GetMapping("/captcha")
    public ResponseEntity<byte[]> getCaptcha(HttpSession session) {
        CaptchaResult captcha = captchaService.generateCaptcha();
        session.setAttribute("CAPTCHA_CODE", captcha.getCode());
        return ResponseEntity.ok()
                .contentType(MediaType.IMAGE_PNG)
                .body(captcha.getImageBytes());
    }
} 