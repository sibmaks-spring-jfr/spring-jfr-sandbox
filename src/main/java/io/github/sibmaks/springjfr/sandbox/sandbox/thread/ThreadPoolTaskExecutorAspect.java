package io.github.sibmaks.springjfr.sandbox.sandbox.thread;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;

@Slf4j
@Aspect
@Component
public class ThreadPoolTaskExecutorAspect {

    @Around("execution(* org.springframework.core.task.AsyncTaskExecutor.submit(..))")
    public Object aroundSubmit(ProceedingJoinPoint pjp) throws Throwable {
        log.info("Submit new task");
        return pjp.proceed(pjp.getArgs());
    }

    @Around("execution(* org.springframework.core.task.AsyncTaskExecutor.execute(..))")
    public Object aroundExecute(ProceedingJoinPoint pjp) throws Throwable {
        log.info("Executes new task");
        return pjp.proceed(pjp.getArgs());
    }

}