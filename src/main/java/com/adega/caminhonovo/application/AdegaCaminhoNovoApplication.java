package com.adega.caminhonovo.application;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.persistence.autoconfigure.EntityScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

// A classe principal esta em um subpacote, entao os pacotes do model e do
// repository precisam ser informados para o Spring encontra-los.
@SpringBootApplication(scanBasePackages = "com.adega.caminhonovo")
@EntityScan(basePackages = "com.adega.caminhonovo.model")
@EnableJpaRepositories(basePackages = "com.adega.caminhonovo.repository")
public class AdegaCaminhoNovoApplication {

    public static void main(String[] args) {
        SpringApplication.run(AdegaCaminhoNovoApplication.class, args);
    }
}
