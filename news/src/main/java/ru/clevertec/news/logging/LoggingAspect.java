package ru.clevertec.news.logging;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

@Slf4j
@Aspect
@Component
public class LoggingAspect {

    @Pointcut("@within(org.springframework.web.bind.annotation.RestController)")
    public void restControllerMethods() { }

    @Pointcut(
            """
            @annotation(org.springframework.web.bind.annotation.GetMapping) ||
            @annotation(org.springframework.web.bind.annotation.PostMapping) ||
            @annotation(org.springframework.web.bind.annotation.DeleteMapping) ||
            @annotation(org.springframework.web.bind.annotation.PatchMapping) ||
            @annotation(org.springframework.web.bind.annotation.RequestMapping)
            """
    )
    public void mapRequests() { }

    @Before("mapRequests()")
    public void logRequest(JoinPoint joinPoint) {
        String methodName = joinPoint.getSignature().getName();
        Object[] args = joinPoint.getArgs();
        log.info("Request for method: {}, args {}", methodName, args);
    }

    @AfterReturning(value = "restControllerMethods()", returning = "response")
    public void logResponse(JoinPoint joinPoint, ResponseEntity<?> response) {
        String methodName = joinPoint.getSignature().getName();
        log.info("Response for method {}: body {}, status code: {}",
                methodName, response.getBody(), response.getStatusCode());
    }

    @AfterThrowing(value = "restControllerMethods()", throwing = "exception")
    public void logException(JoinPoint joinPoint, Exception exception) {
        String methodName = joinPoint.getSignature().getName();
        log.error("Exception in method {}: {}", methodName, exception.getMessage());
    }
}