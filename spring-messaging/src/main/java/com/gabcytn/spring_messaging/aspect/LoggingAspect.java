package com.gabcytn.spring_messaging.aspect;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Aspect;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.Arrays;

@Aspect
@Component
public class LoggingAspect {
    private final Logger LOGGER = LoggerFactory.getLogger(LoggingAspect.class);

    @AfterThrowing(pointcut = "execution(* com.gabcytn.spring_messaging..*(..))", throwing = "exception")
    public void logOnError (JoinPoint joinPoint, Throwable exception) {
        LOGGER.error("Exception in method: {}. Class: {}. Arguments: {}. Exception: {}. Timestamp: {}",
                joinPoint.getSignature().getName(),
                joinPoint.getTarget().getClass().getName(),
                Arrays.toString(joinPoint.getArgs()),
                exception.getMessage(),
                LocalDateTime.now());    }
}
