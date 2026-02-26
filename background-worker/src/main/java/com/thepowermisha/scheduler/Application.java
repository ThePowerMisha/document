package com.thepowermisha.scheduler;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.persistence.autoconfigure.EntityScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication(scanBasePackages = {
        "com.thepowermisha.scheduler",
        "com.thepowermisha.document"
})
@EntityScan(basePackages = "com.thepowermisha.document.entity")
@EnableJpaRepositories(basePackages = "com.thepowermisha.document.repository")
public class Application {
    public static void main(String[] args) {
        SpringApplication.run(Application.class);
    }
}
