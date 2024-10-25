package io.github.sibmaks.springjfr.sandbox.conf;

import org.flywaydb.core.Flyway;
import org.flywaydb.core.api.configuration.ClassicConfiguration;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.TaskScheduler;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;

import javax.sql.DataSource;

/**
 * @author sibmaks
 * @since 0.0.1
 */
@Configuration
public class ApplicationConfiguration {


    @Bean
    @ConfigurationProperties("spring.flyway")
    public ClassicConfiguration sandboxFlywayConfiguration(DataSource dataSource) {
        var classicConfiguration = new ClassicConfiguration();
        classicConfiguration.setDataSource(dataSource);
        return classicConfiguration;
    }

    @Bean
    public Flyway sandboxFlyway(@Qualifier("sandboxFlywayConfiguration") ClassicConfiguration configuration) {
        var flyway = new Flyway(configuration);
        flyway.migrate();
        return flyway;
    }

    @Bean
    public TaskScheduler sandboxScheduledExecutor() {
        var scheduler = new ThreadPoolTaskScheduler();
        scheduler.setThreadNamePrefix("sandbox-");
        scheduler.setPoolSize(Runtime.getRuntime().availableProcessors());
        return scheduler;
    }
}
