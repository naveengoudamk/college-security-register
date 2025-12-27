package com.college.security;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class SecurityRegisterApplication {
    public static void main(String[] args) {
        SpringApplication.run(SecurityRegisterApplication.class, args);
    }
}
