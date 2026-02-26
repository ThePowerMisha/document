package com.thepowermisha.scheduler;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication(scanBasePackages = "com.thepowermisha")
public class Application {
    public static void main(String[] args) {
        SpringApplication.run(Application.class);
    }
}
