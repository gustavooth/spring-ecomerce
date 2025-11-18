package me.gustavoo.ecomerce;

import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.SpringApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@SpringBootApplication
@EnableJpaAuditing
public class Api {
    public static void main(String[] args) {
        SpringApplication.run(Api.class, args);
    }
}
