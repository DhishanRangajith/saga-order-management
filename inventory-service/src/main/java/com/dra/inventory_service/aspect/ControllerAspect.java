package com.dra.inventory_service.aspect;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;

import lombok.extern.slf4j.Slf4j;

@Component
@Aspect
@Slf4j
public class ControllerAspect {
    
    @Around("@within(org.springframework.web.bind.annotation.RestController)")
    public Object logControllerInputOutput(ProceedingJoinPoint joinPoint) throws Throwable {
        String className = joinPoint.getTarget().getClass().getSimpleName();
        String methodName = joinPoint.getSignature().getName();
        Object[] args = joinPoint.getArgs();

        log.info("Entering [{}#{}] with arguments: {}", className, methodName, args);

        Object result;
        try {
            result = joinPoint.proceed(); // proceed with method execution
            log.info("Exiting [{}#{}] with result: {}", className, methodName, result);
        } catch (Throwable ex) {
            log.error("Exception in [{}#{}]: {}", className, methodName, ex.getMessage(), ex);
            throw ex;
        }

        return result;
    }

}
