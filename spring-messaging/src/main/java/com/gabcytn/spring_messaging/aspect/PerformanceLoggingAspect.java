package com.gabcytn.spring_messaging.aspect;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Aspect
@Component
public class PerformanceLoggingAspect {
    private final Logger LOGGER = LoggerFactory.getLogger(PerformanceLoggingAspect.class);

    @Around("execution(* com.gabcytn.spring_messaging.controller.AuthController.login(..))")
    public Object logPerformance (ProceedingJoinPoint joinPoint) throws Throwable {
        long initTime = System.currentTimeMillis();
        Object object = joinPoint.proceed();

        long finalTime = System.currentTimeMillis();
        long time = finalTime - initTime;

        LOGGER.info("{} took {} ms", joinPoint.getSignature().getName(), time);

        return object;
    }
}
