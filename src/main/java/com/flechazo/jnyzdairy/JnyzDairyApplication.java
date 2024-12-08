package com.flechazo.jnyzdairy;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.transaction.annotation.EnableTransactionManagement;

/**
 * 应用程序启动类
 */
@SpringBootApplication
@EnableConfigurationProperties
@EnableTransactionManagement
@EnableScheduling
public class JnyzDairyApplication {

    public static void main(String[] args) {
        SpringApplication.run(JnyzDairyApplication.class, args);
    }
}