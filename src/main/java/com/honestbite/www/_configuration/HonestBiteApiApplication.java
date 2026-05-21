package com.honestbite.www._configuration;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.persistence.autoconfigure.EntityScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@EntityScan(basePackages = {"com.honestbite.www"})
@EnableJpaRepositories(basePackages = {"com.honestbite.www"})
@SpringBootApplication(scanBasePackages = {"com.honestbite.www"})
public class HonestBiteApiApplication {

    public static void main(String[] args) {
        SpringApplication.run(HonestBiteApiApplication.class, args);
    }

}
