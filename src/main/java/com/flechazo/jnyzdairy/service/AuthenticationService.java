package com.flechazo.jnyzdairy.service;

import com.flechazo.jnyzdairy.dto.AuthRequest;
import com.flechazo.jnyzdairy.dto.AuthResponse;
import com.flechazo.jnyzdairy.dto.RegisterRequest;
import com.flechazo.jnyzdairy.entity.User;
import com.flechazo.jnyzdairy.util.JwtUtil;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

/**
 * 认证服务，提供用户登录和注册功能。
 *
 * @author Flechazo
 */
@Service
public class AuthenticationService {

    private final AuthenticationManager authenticationManager;
    private final UserService userService;
    private final JwtUtil jwtUtil;

    /**
     * 构造函数，用于依赖注入。
     *
     * @param authenticationManager 用于执行认证操作的管理器
     * @param userService 用户服务接口，用于与用户数据交互
     * @param jwtUtil JWT工具类，用于生成和解析JWT令牌
     */
    public AuthenticationService(AuthenticationManager authenticationManager, UserService userService, JwtUtil jwtUtil) {
        this.authenticationManager = authenticationManager;
        this.userService = userService;
        this.jwtUtil = jwtUtil;
    }

    /**
     * 用户登录认证。
     * <p>
     * 该方法接收用户的认证请求，使用 {@code AuthenticationManager} 进行身份验证，
     * 验证成功后，通过 {@code JwtUtil} 生成JWT令牌并返回给客户端。
     *
     * @param request 包含用户名和密码的认证请求对象
     * @return 包含JWT令牌、用户角色和用户ID的响应对象
     */
    public AuthResponse login(AuthRequest request) {
        /* 进行认证 */
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.getUsername(), request.getPassword())
        );

        /* 生成JWT令牌 */
        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
        String token = jwtUtil.generateToken(userDetails);

        /* 获取用户信息 */
        User user = userService.findByUsername(request.getUsername());

        return new AuthResponse(token, user.getRole().toString(), user.getId());
    }

    /**
     * 用户注册。
     * <p>
     * 该方法接收用户的注册请求，创建新用户并通过 {@code JwtUtil} 生成JWT令牌，
     * 然后将包含令牌、用户角色和用户ID的响应对象返回给客户端。
     *
     * @param request 包含注册所需信息的请求对象
     * @return 包含JWT令牌、用户角色和用户ID的响应对象
     */
    public AuthResponse register(RegisterRequest request) {
        /* 创建新用户 */
        User user = userService.createUser(request);

        /* 生成JWT令牌 */
        UserDetails userDetails = org.springframework.security.core.userdetails.User
                .withUsername(user.getUsername())
                .password(user.getPassword())
                .authorities(user.getRole().toString())
                .build();

        String token = jwtUtil.generateToken(userDetails);

        return new AuthResponse(token, user.getRole().toString(), user.getId());
    }
}