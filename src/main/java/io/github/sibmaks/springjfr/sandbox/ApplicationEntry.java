package io.github.sibmaks.springjfr.sandbox;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.scheduling.annotation.EnableAsync;

/**
 * Application entry point.
 *
 * @author sibmaks
 * @since 0.0.1
 */
@EnableAsync
@EnableJpaRepositories
@SpringBootApplication
public class ApplicationEntry {
    public static void main(String[] args) {
        SpringApplication.run(ApplicationEntry.class, args);
    }
}
