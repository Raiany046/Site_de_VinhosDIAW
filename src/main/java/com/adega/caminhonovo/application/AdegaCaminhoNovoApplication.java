package com.adega.caminhonovo.application;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication(scanBasePackages = {"com.adega"})
public class AdegaCaminhoNovoApplication {

    public static void main(String[] args) {
        SpringApplication.run(AdegaCaminhoNovoApplication.class, args);
    }

}
