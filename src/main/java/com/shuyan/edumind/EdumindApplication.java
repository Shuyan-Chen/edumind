package com.shuyan.edumind;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@SpringBootApplication
@EnableTransactionManagement
@EnableConfigurationProperties(value = { com.shuyan.edumind.configuration.property.SystemConfig.class})
public class EdumindApplication {
    public static void main(String[] args) {
        SpringApplication.run(EdumindApplication.class, args);
    }
}
