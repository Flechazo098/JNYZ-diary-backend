package com.flechazo.jnyzdairy.util;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

/**
 * JWT工具类，用于生成和验证JWT令牌。
 * <p>
 * 该工具类提供了创建、解析、验证JSON Web Token（JWT）的方法，
 * 并支持从令牌中提取用户名和检查令牌是否过期等功能。
 *
 * @author Flechazo
 */
@Component
public class JwtUtil {

    /**
     * JWT签名密钥，用于加密和解密令牌。
     */
    @Value("${app.jwt.secret}")
    private String secret;

    /**
     * JWT过期时间，单位为毫秒。
     */
    @Value("${app.jwt.expiration}")
    private Long expiration;

    /**
     * 根据用户详情生成JWT令牌。
     * <p>
     * 该方法会创建一个包含用户信息的JWT令牌，并设置其有效期。
     *
     * @param userDetails 用户详情对象
     * @return 包含用户信息的JWT令牌字符串
     */
    public String generateToken(UserDetails userDetails) {
        Map<String, Object> claims = new HashMap<>();
        return createToken(claims, userDetails.getUsername());
    }

    /**
     * 创建JWT令牌。
     * <p>
     * 该私有方法负责构建并返回一个带有指定声明和主题的JWT令牌。
     *
     * @param claims 声明信息
     * @param subject 主题，通常是用户的唯一标识符
     * @return 构建好的JWT令牌字符串
     */
    private String createToken(Map<String, Object> claims, String subject) {
        return Jwts.builder()
                .setClaims(claims)
                .setSubject(subject)
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + expiration))
                .signWith(SignatureAlgorithm.HS256, secret)
                .compact();
    }

    /**
     * 验证JWT令牌的有效性。
     * <p>
     * 该方法通过比较令牌中的用户名与提供的用户详情对象中的用户名来验证令牌，
     * 并检查令牌是否已过期。
     *
     * @param token JWT令牌字符串
     * @param userDetails 用户详情对象
     * @return 如果令牌有效则返回 {@code true}，否则返回 {@code false}
     */
    public boolean validateToken(String token, UserDetails userDetails) {
        final String username = extractUsername(token);
        return (username.equals(userDetails.getUsername()) && !isTokenExpired(token));
    }

    /**
     * 从JWT令牌中提取用户名。
     * <p>
     * 该方法解析令牌并返回其中的主题字段，通常代表用户名。
     *
     * @param token JWT令牌字符串
     * @return 提取到的用户名
     */
    public String extractUsername(String token) {
        return extractClaim(token, Claims::getSubject);
    }

    /**
     * 从JWT令牌中提取过期时间。
     * <p>
     * 该方法解析令牌并返回其中的过期时间字段。
     *
     * @param token JWT令牌字符串
     * @return 提取到的过期时间
     */
    public Date extractExpiration(String token) {
        return extractClaim(token, Claims::getExpiration);
    }

    /**
     * 从JWT令牌中提取声明信息。
     * <p>
     * 该私有泛型方法负责解析令牌，并使用提供的解析器函数获取特定的声明信息。
     *
     * @param <T> 声明信息的类型
     * @param token JWT令牌字符串
     * @param claimsResolver 解析声明信息的函数
     * @return 解析后的声明信息
     */
    private <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = extractAllClaims(token);
        return claimsResolver.apply(claims);
    }

    /**
     * 解析JWT令牌的所有声明信息。
     * <p>
     * 该私有方法负责解析整个JWT令牌，并返回其中的所有声明信息。
     *
     * @param token JWT令牌字符串
     * @return 包含所有声明信息的{@code Claims}对象
     */
    private Claims extractAllClaims(String token) {
        return Jwts.parser()
                .setSigningKey(secret)
                .parseClaimsJws(token)
                .getBody();
    }

    /**
     * 检查JWT令牌是否已过期。
     * <p>
     * 该私有方法解析令牌并检查其过期时间是否在当前时间之前。
     *
     * @param token JWT令牌字符串
     * @return 如果令牌已过期则返回 {@code true}，否则返回 {@code false}
     */
    private Boolean isTokenExpired(String token) {
        return extractExpiration(token).before(new Date());
    }

    /**
     * 在过滤器中验证token的有效性。
     * <p>
     * 该方法尝试解析令牌并检查其是否过期。如果解析失败或令牌无效，则返回 {@code false}。
     *
     * @param token JWT令牌字符串
     * @return 如果令牌有效则返回 {@code true}，否则返回 {@code false}
     */
    public boolean validateToken(String token) {
        try {
            Jwts.parser()
                    .setSigningKey(secret)
                    .parseClaimsJws(token);
            return !isTokenExpired(token);
        } catch (Exception e) {
            return false;
        }
    }

    /**
     * 在过滤器中从token中获取用户名。
     * <p>
     * 该方法调用 {@link #extractUsername(String)} 方法来提取用户名。
     *
     * @param token JWT令牌字符串
     * @return 提取到的用户名
     */
    public String getUsernameFromToken(String token) {
        return extractUsername(token);
    }
}