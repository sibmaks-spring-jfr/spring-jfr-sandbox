package io.github.sibmaks.springjfr.sandbox.sandbox.thread;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;

@Slf4j
@Aspect
@Component
public class TaskSchedulerAspect {

    @Around("execution(* org.springframework.scheduling.TaskScheduler.schedule(..))")
    public Object aroundSchedule(ProceedingJoinPoint pjp) throws Throwable {
        log.info("schedule new task");
        return pjp.proceed(pjp.getArgs());
    }

    @Around("execution(* org.springframework.scheduling.TaskScheduler.scheduleAtFixedRate(..))")
    public Object aroundScheduleAtFixedRate(ProceedingJoinPoint pjp) throws Throwable {
        log.info("scheduleAtFixedRate new task");
        return pjp.proceed(pjp.getArgs());
    }

    @Around("execution(* org.springframework.scheduling.TaskScheduler.scheduleWithFixedDelay(..))")
    public Object aroundScheduleWithFixedDelay(ProceedingJoinPoint pjp) throws Throwable {
        log.info("scheduleWithFixedDelay new task");
        return pjp.proceed(pjp.getArgs());
    }

}