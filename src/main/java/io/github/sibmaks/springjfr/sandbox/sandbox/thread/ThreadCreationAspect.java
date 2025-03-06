package io.github.sibmaks.springjfr.sandbox.sandbox.thread;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;

import java.util.concurrent.ThreadFactory;
import java.util.concurrent.ThreadPoolExecutor;

@Slf4j
@Aspect
@Component
public class ThreadCreationAspect {

    // Перехват метода newThread() у ThreadFactory
    @Around("threadFactoryCreation() && target(factory)")
    public Object aroundThreadCreation(ProceedingJoinPoint pjp, ThreadFactory factory) throws Throwable {
        Object result = pjp.proceed(); // Создаем поток
        Thread thread = (Thread) result;

        // Определяем пул, к которому относится фабрика (если возможно)
        String poolName = resolvePoolName(factory);

        // Публикуем событие
        log.info("Thread created: {} (pool: {})", thread.getName(), poolName);

        return result;
    }

    // Pointcut для вызова ThreadFactory.newThread()
    @Pointcut("execution(* java.util.concurrent.ThreadFactory.newThread(..))")
    private void threadFactoryCreation() {
    }

    // Метод для определения имени пула по ThreadFactory (пример для HikariCP и стандартных пулов)
    private String resolvePoolName(ThreadFactory factory) {
        // Пример для пулов, где фабрика является внутренним классом (например, HikariCP)
        String factoryClassName = factory.getClass().getName();
        if (factoryClassName.contains("HikariPool")) {
            return "HikariCP Connection Pool";
        } else if (factoryClassName.contains("ThreadPoolExecutor")) {
            return "Application Thread Pool";
        }
        return factoryClassName; // Игнорируем системные фабрики
    }

    // Мониториг метода execute() у ThreadPoolExecutor
    @After("execution(* java.util.concurrent.ThreadPoolExecutor.execute(*)) && target(executor)")
    public void afterExecute(ThreadPoolExecutor executor) {
        log.info("Thread executed: {}", executor);
    }
}