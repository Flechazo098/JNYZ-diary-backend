package com.flechazo.jnyzdairy.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * Web配置类，用于定义静态资源处理规则和跨域资源共享（CORS）过滤器。
 *
 * @author Flechazo
 */
@Configuration
public class WebConfig implements WebMvcConfigurer {

    /**
     * 添加静态资源处理器，以支持访问位于classpath下的静态资源文件。
     * <p>
     * 该方法重写了{@link WebMvcConfigurer#addResourceHandlers}，
     * 并配置了路径为"/static/**"的资源处理器，指向"classpath:/static/"目录。
     *
     * @param registry 资源处理器注册表
     */
    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler("/static/**")
                .addResourceLocations("classpath:/static/");
    }

    /**
     * 创建并返回一个自定义的CORS过滤器实例。
     * <p>
     * 该方法配置了允许的源、HTTP方法、头信息、是否允许发送身份凭证、暴露的响应头以及预检请求的有效期。
     *
     * @return CorsFilter对象
     */
    @Bean(name = "customCorsFilter")
    public CorsFilter corsFilter() {
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        CorsConfiguration config = new CorsConfiguration();

        // 配置允许的源
        /*
         * 前端开发服务器和生产环境的地址。
         * 注意：在实际部署时，请根据实际情况修改这些地址。
         */
        // 前端开发服务器
        config.addAllowedOrigin("http://localhost:5173");
        // 前端生产环境
        config.addAllowedOrigin("http://localhost:5174");

        // 配置允许的HTTP方法
        config.addAllowedMethod("*");

        // 配置允许的头信息
        config.addAllowedHeader("*");

        // 配置是否允许发送身份凭证（如cookies）
        config.setAllowCredentials(true);

        // 配置允许暴露的响应头
        config.addExposedHeader("Set-Cookie");

        // 配置预检请求的有效期（单位：秒）
        config.setMaxAge(3600L);

        source.registerCorsConfiguration("/**", config);
        return new CorsFilter(source);
    }
}