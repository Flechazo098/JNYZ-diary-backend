package com.flechazo.jnyzdairy.service;

import com.flechazo.jnyzdairy.dto.AuthRequest;
import com.flechazo.jnyzdairy.dto.AuthResponse;
import com.flechazo.jnyzdairy.dto.RegisterRequest;
import com.flechazo.jnyzdairy.entity.User;
import com.flechazo.jnyzdairy.util.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

/**
 * 认证服务
 */
@Service
public class AuthenticationService {

    private final AuthenticationManager authenticationManager;
    private final UserService userService;
    private final JwtUtil jwtUtil;

    @Autowired
    public AuthenticationService(AuthenticationManager authenticationManager, UserService userService, JwtUtil jwtUtil){
        this.authenticationManager = authenticationManager;
        this.userService = userService;
        this.jwtUtil = jwtUtil;
    }

    /**
     * 用户登录
     */
    public AuthResponse login(AuthRequest request) {
        // 进行认证
        Authentication authentication = authenticationManager.authenticate(
            new UsernamePasswordAuthenticationToken(request.getUsername(), request.getPassword())
        );

        // 生成JWT令牌
        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
        String token = jwtUtil.generateToken(userDetails);

        // 获取用户信息
        User user = userService.findByUsername(request.getUsername());

        return new AuthResponse(token, user.getRole().toString(), user.getId());
    }

    /**
     * 用户注册
     */
    public AuthResponse register(RegisterRequest request) {
        // 创建新用户
        User user = userService.createUser(request);

        // 生成JWT令牌
        UserDetails userDetails = org.springframework.security.core.userdetails.User
            .withUsername(user.getUsername())
            .password(user.getPassword())
            .authorities(user.getRole().toString())
            .build();

        String token = jwtUtil.generateToken(userDetails);

        return new AuthResponse(token, user.getRole().toString(), user.getId());
    }
} 