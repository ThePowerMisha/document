package com.thepowermisha.generator;

import com.thepowermisha.generator.service.GeneratorService;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.persistence.autoconfigure.EntityScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication(scanBasePackages = {
        "com.thepowermisha.generator",
        "com.thepowermisha.document"
})
@EntityScan(basePackages = "com.thepowermisha.document.entity")
@EnableJpaRepositories(basePackages = "com.thepowermisha.document.repository")
@RequiredArgsConstructor
public class Generator implements CommandLineRunner {

    private final GeneratorService generatorService;

    public static void main(String[] args) {
        SpringApplication.run(Generator.class);
    }

    @Override
    public void run(String... args) {
        generatorService.generateDocuments();
    }
}