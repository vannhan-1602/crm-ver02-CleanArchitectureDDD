package com.crm.api;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication(scanBasePackages = {"com.crm"})
@EnableJpaRepositories(basePackages = {"com.crm.persistence.repositories","com.crm.infrastructure.sanpham"})
@EntityScan(basePackages = {"com.crm.domain.entities"})
public class CrmApplication {
    public static void main(String[] args) {
        SpringApplication.run(CrmApplication.class, args);
    }
}