package com.honestbite.www.configuration;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.persistence.autoconfigure.EntityScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@EntityScan(basePackages = {"com.honestbite.www.user.model"})
@EnableJpaRepositories(basePackages = {"com.honestbite.www.user.persistence"})
@SpringBootApplication(scanBasePackages = {"com.honestbite.www.user"})
public class HonestBiteApiApplication {

    public static void main(String[] args) {
        SpringApplication.run(HonestBiteApiApplication.class, args);
    }

}
