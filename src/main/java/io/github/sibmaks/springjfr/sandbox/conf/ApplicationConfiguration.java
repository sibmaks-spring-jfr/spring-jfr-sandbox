package io.github.sibmaks.springjfr.sandbox.conf;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import jdk.jfr.FlightRecorder;
import jdk.jfr.FlightRecorderListener;
import jdk.jfr.Recording;
import jdk.jfr.RecordingState;
import lombok.extern.slf4j.Slf4j;
import org.flywaydb.core.Flyway;
import org.flywaydb.core.api.configuration.ClassicConfiguration;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.web.embedded.tomcat.TomcatServletWebServerFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.TaskScheduler;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;

import javax.management.InstanceNotFoundException;
import javax.management.MalformedObjectNameException;
import javax.sql.DataSource;
import java.util.*;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @author sibmaks
 * @since 0.0.1
 */
@Slf4j
@Configuration
public class ApplicationConfiguration {
/*

    @Bean
    @Order(Ordered.HIGHEST_PRECEDENCE)
    public static BeanDefinitionRegistryPostProcessor proxyA(
            ApplicationContext context
    ) throws NoSuchFieldException, IllegalAccessException {
        var beanFactoryField = GenericApplicationContext.class.getDeclaredField("beanFactory");
        beanFactoryField.setAccessible(true);
        var beanFactory = beanFactoryField.get(context);

        if (AopUtils.isAopProxy(beanFactory)) {
            return new BeanDefinitionRegistryPostProcessor() {
                @Override
                public void postProcessBeanDefinitionRegistry(BeanDefinitionRegistry registry) throws BeansException {

                }

                @Override
                public void postProcessBeanFactory(ConfigurableListableBeanFactory beanFactory) throws BeansException {

                }
            };
        }

        var proxyFactory = new AspectJProxyFactory(beanFactory);
        proxyFactory.addAspect(new JavaFlightRecorderResolveDependencyEventProducer(() -> "test"));
        DefaultListableBeanFactory proxy = proxyFactory.getProxy();
        beanFactoryField.set(context, proxy);

        return new BeanDefinitionRegistryPostProcessor() {

            @Override
            public void postProcessBeanFactory(ConfigurableListableBeanFactory beanFactory) throws BeansException {

            }

            @Override
            public void postProcessBeanDefinitionRegistry(BeanDefinitionRegistry registry) throws BeansException {

            }
        };
    }
*/

    @Bean
    public static String setUp(
            ApplicationContext applicationContext,
            ConfigurableListableBeanFactory configurableListableBeanFactory
    ) throws MalformedObjectNameException, InstanceNotFoundException {
        FlightRecorder.addListener(new FlightRecorderListener() {
            @Override
            public void recordingStateChanged(Recording recording) {
                try {
                    log.info("Recorder stateChanged: {} {}", recording.getName(), recording.getState());
                    if (recording.getState() == RecordingState.RUNNING) {
                        var settings = recording.getSettings();
                        var beansEnabled = settings.get("spring#beans.enabled");

                        if ("true".equals(beansEnabled)) {
                            var contextBeanDefinitionNames = applicationContext.getBeanDefinitionNames();
                            var factoryBeanDefinitionNames = configurableListableBeanFactory.getBeanDefinitionNames();

                            if (!Arrays.deepEquals(contextBeanDefinitionNames, factoryBeanDefinitionNames)) {
                                log.warn("Bean names do not match");
                            }

                            var visited = new HashSet<String>();
                            var toVisit = new LinkedList<String>();
                            toVisit.addAll(List.of(contextBeanDefinitionNames));
                            toVisit.addAll(List.of(factoryBeanDefinitionNames));

                            var beansDependencies = new HashMap<String, Set<String>>();

                            while (!toVisit.isEmpty()) {
                                var name = toVisit.removeFirst();
                                if (visited.contains(name)) {
                                    continue;
                                }
                                visited.add(name);

                                var beanDependencies = beansDependencies.computeIfAbsent(name, k -> new HashSet<>());
                                if (configurableListableBeanFactory.containsBeanDefinition(name)) {
                                    var beanDefinition = configurableListableBeanFactory.getBeanDefinition(name);
                                    var dependsOn = Optional.ofNullable(beanDefinition.getDependsOn())
                                            .orElse(new String[0]);
                                    beanDependencies.addAll(List.of(dependsOn));
                                    toVisit.addAll(List.of(dependsOn));
                                }

                                var dependencies = configurableListableBeanFactory.getDependenciesForBean(name);
                                beanDependencies.addAll(List.of(dependencies));
                                toVisit.addAll(List.of(dependencies));

                                var dependents = configurableListableBeanFactory.getDependentBeans(name);
                                toVisit.addAll(List.of(dependents));

                                for (var dependent : dependents) {
                                    var otherBeanDependencies = beansDependencies.computeIfAbsent(dependent, k -> new HashSet<>());
                                    otherBeanDependencies.add(name);
                                }
                            }

                            var mapper = new ObjectMapper();
                            try {
                                log.info("Beans dependencies: {}", mapper.writeValueAsString(beansDependencies));
                            } catch (JsonProcessingException e) {
                                throw new RuntimeException(e);
                            }
                        }
                    }
                } catch (Exception e) {
                    log.error("Error encountered while enriching dependencies", e);
                }
            }
        });
        return "nice!";
    }

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
            if (executor instanceof ThreadPoolExecutor) {
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
