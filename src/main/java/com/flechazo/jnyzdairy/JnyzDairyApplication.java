package com.flechazo.jnyzdairy;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.transaction.annotation.EnableTransactionManagement;

/**
 * 应用程序启动类。
 * <p>
 * 该类是Spring Boot应用程序的入口点，负责启动应用并初始化各种配置和组件。
 * 通过使用 {@code @SpringBootApplication} 注解，它自动启用了自动配置、组件扫描以及配置属性支持。
 * 此外，还启用了事务管理和调度任务的支持。
 *
 * @author Flechazo
 */
@SpringBootApplication
@EnableConfigurationProperties
@EnableTransactionManagement
@EnableScheduling
public class JnyzDairyApplication {

    /**
     * 应用程序的主方法，用于启动Spring Boot应用程序。
     * <p>
     * 该方法调用 {@code SpringApplication.run()} 来启动应用程序，并传递当前类和命令行参数作为参数。
     *
     * @param args 命令行参数
     */
    public static void main(String[] args) {
        SpringApplication.run(JnyzDairyApplication.class, args);
    }
}