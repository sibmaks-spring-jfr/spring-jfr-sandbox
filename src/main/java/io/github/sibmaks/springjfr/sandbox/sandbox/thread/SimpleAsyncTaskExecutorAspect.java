package io.github.sibmaks.springjfr.sandbox.sandbox.thread;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;

@Slf4j
@Aspect
@Component
public class SimpleAsyncTaskExecutorAspect {

    @Around("execution(* org.springframework.core.task.SimpleAsyncTaskExecutor.execute(..))")
    public Object aroundExecute(ProceedingJoinPoint pjp) throws Throwable {
        log.info("execute new task");
        return pjp.proceed(pjp.getArgs());
    }

    @Around("execution(* org.springframework.core.task.SimpleAsyncTaskExecutor.submit(..))")
    public Object aroundSubmit(ProceedingJoinPoint pjp) throws Throwable {
        log.info("submit new task");
        return pjp.proceed(pjp.getArgs());
    }

    @Around("execution(* org.springframework.core.task.SimpleAsyncTaskExecutor.submitListenable(..))")
    public Object aroundSubmitListenable(ProceedingJoinPoint pjp) throws Throwable {
        log.info("submit new task");
        return pjp.proceed(pjp.getArgs());
    }

}