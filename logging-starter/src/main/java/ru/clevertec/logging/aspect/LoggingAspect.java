package ru.clevertec.logging.aspect;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;

@Slf4j
@Aspect
public class LoggingAspect {

    /**
     * Handle function calls in classes marked as
     * {@link ru.clevertec.logging.annotation.Loggable Loggable}
     */
    @Pointcut("@within(ru.clevertec.logging.annotation.Loggable)")
    public void functionWithLoggableCall() {
    }

    @Before("functionWithLoggableCall()")
    public void logCall(JoinPoint joinPoint) {
        String methodName = joinPoint.getSignature().getName();
        Object[] args = joinPoint.getArgs();
        log.info("Method {} called with args '{}'", methodName, args);
    }

    @AfterReturning(value = "functionWithLoggableCall()", returning = "returnValue")
    public void logResponse(JoinPoint joinPoint, Object returnValue) {
        String methodName = joinPoint.getSignature().getName();
        log.info("Method {} returned {}", methodName, returnValue);
    }
}