package com.practice.flightbooking.aop;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.*;
import org.springframework.stereotype.Component;

@Aspect
@Component
@Slf4j
public class LoggingAspect {

    // ── Pointcut — all service methods ────────────────────────
    @Pointcut("execution(* com.practice.flightbooking.service.*.*(..))")
    public void serviceMethods() {}

    // ── Pointcut — all controller methods ─────────────────────
    @Pointcut("execution(* com.practice.flightbooking.controller.*.*(..))")
    public void controllerMethods() {}

    // ── Log every API request ──────────────────────────────────
    @Before("controllerMethods()")
    public void logApiRequest(JoinPoint joinPoint) {
        String methodName = joinPoint.getSignature().getName();
        String className = joinPoint.getTarget().getClass().getSimpleName();
        log.info("🌐 API Request → {}.{}()", className, methodName);
    }

    // ── Log before service method ──────────────────────────────
    @Before("serviceMethods()")
    public void logBefore(JoinPoint joinPoint) {
        String methodName = joinPoint.getSignature().getName();
        String className = joinPoint.getTarget().getClass().getSimpleName();
        log.info("▶ {}.{}() called", className, methodName);
    }

    // ── Log after service method succeeds ─────────────────────
    @AfterReturning("serviceMethods()")
    public void logAfterReturning(JoinPoint joinPoint) {
        String methodName = joinPoint.getSignature().getName();
        String className = joinPoint.getTarget().getClass().getSimpleName();
        log.info("✅ {}.{}() completed successfully", className, methodName);
    }

    // ── Log if service method throws exception ─────────────────
    @AfterThrowing(pointcut = "serviceMethods()", throwing = "exception")
    public void logAfterThrowing(JoinPoint joinPoint, Throwable exception) {
        String methodName = joinPoint.getSignature().getName();
        String className = joinPoint.getTarget().getClass().getSimpleName();
        log.error("❌ {}.{}() threw: {}", className, methodName, exception.getMessage());
    }

    // ── Log execution time ─────────────────────────────────────
    @Around("serviceMethods()")
    public Object logExecutionTime(ProceedingJoinPoint joinPoint) throws Throwable {
        String methodName = joinPoint.getSignature().getName();
        String className = joinPoint.getTarget().getClass().getSimpleName();

        long start = System.currentTimeMillis();
        Object result = joinPoint.proceed();
        long timeTaken = System.currentTimeMillis() - start;

        log.info("⏱ {}.{}() executed in {}ms", className, methodName, timeTaken);
        return result;
    }
}