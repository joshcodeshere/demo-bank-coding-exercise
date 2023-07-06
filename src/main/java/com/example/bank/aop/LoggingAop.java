package com.example.bank.aop;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;

import lombok.extern.slf4j.Slf4j;

@Aspect
@Component
@Slf4j
public class LoggingAop {

    /**
     * {@link Pointcut} that logs around Spring {@link Service} annotations
     */
    @Pointcut("within(@org.springframework.stereotype.Service *)")
    public void springServices() {
    }

    /**
     * Handle all Spring {@link Service} exception logging via AOP.
     * 
     * @param joinPoint
     * @param exception
     */
    @AfterThrowing(pointcut = "springServices()", throwing = "exception")
    public void logAfterThrowingSpringServices(JoinPoint joinPoint, Throwable exception) {
        log.error("{}.{}({}) exception = {} message = {}", joinPoint.getSignature().getDeclaringTypeName(),
                joinPoint.getSignature().getName(), joinPoint.getArgs(), exception.getClass(),
                exception.getMessage() != null ? exception.getMessage() : "");
    }

}
