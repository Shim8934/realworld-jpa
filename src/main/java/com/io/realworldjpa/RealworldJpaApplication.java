package com.io.realworldjpa;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@SpringBootApplication
public class RealworldJpaApplication {

    public static void main(String[] args) {
        SpringApplication.run(RealworldJpaApplication.class, args);
    }

}
