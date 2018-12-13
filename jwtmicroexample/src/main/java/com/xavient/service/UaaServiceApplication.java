package com.xavient.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.cloud.stream.annotation.EnableBinding;
import org.springframework.context.annotation.Bean;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.xavient.service.model.Registration;
import com.xavient.service.model.TokenProperties;

@SpringBootApplication
@EnableEurekaClient
@EnableBinding(Registration.class)
@EnableScheduling
public class UaaServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(UaaServiceApplication.class, args);
    }

    @Bean
    @ConfigurationProperties(prefix = "security.token")
    public TokenProperties tokenProperties() {
        return new TokenProperties();
    }

    @Bean
    public PasswordEncoder passwordEncoder(@Value("${security.password.strength:10}") int strength) {
        return new BCryptPasswordEncoder(strength);
    }
}

