package io.github.sibmaks.springjfr.sandbox.conf;

import lombok.extern.slf4j.Slf4j;
import org.flywaydb.core.Flyway;
import org.flywaydb.core.api.configuration.ClassicConfiguration;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.web.embedded.tomcat.TomcatServletWebServerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.TaskScheduler;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;

import javax.sql.DataSource;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;

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

    @Bean(name = "customThreadPool")
    public ThreadPoolTaskExecutor taskExecutor() {
        var executor = new ThreadPoolTaskExecutor();
        executor.setCorePoolSize(10); // Initial number of threads
        executor.setMaxPoolSize(50);  // Maximum number of threads
        executor.setQueueCapacity(100); // Queue capacity for holding tasks
        executor.setThreadNamePrefix("custom-exec-"); // Thread name prefix
        executor.initialize();
        return executor;
    }

    @Bean
    public TomcatServletWebServerFactory tomcatFactory() {
        var factory = new TomcatServletWebServerFactory();
        factory.addConnectorCustomizers(connector -> {
            var handler = connector.getProtocolHandler();
            var executor = handler.getExecutor();
            if(executor instanceof ThreadPoolExecutor) {
                var customExecutor = (ThreadPoolExecutor) executor;
                customExecutor.setThreadFactory(new CustomThreadFactory());
            }
        });
        return factory;
    }

    @Slf4j
    public static class CustomThreadFactory implements ThreadFactory {
        private final AtomicInteger threadNumber = new AtomicInteger(1);
        private final String namePrefix = "custom-http-thread-";

        @Override
        public Thread newThread(Runnable task) {
            var thread = new Thread(task, namePrefix + threadNumber.getAndIncrement());
            log.info("Создан новый поток: {}", thread.getName());
            return thread;
        }
    }

}
