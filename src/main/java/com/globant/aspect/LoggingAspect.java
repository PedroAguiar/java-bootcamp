package com.globant.aspect;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.context.annotation.Configuration;

import java.util.Arrays;

/**
 * @author pedro.aguiar
 */
@Aspect
@Slf4j
@Configuration
public class LoggingAspect {

    @Around("@annotation(com.globant.aspect.annotation.Log)")
    public Object log(ProceedingJoinPoint joinPoint) throws Throwable {

        String methodName = joinPoint.getSignature().getName();
        String className = joinPoint.getSignature().getDeclaringTypeName();

        if (log.isDebugEnabled()) {
            log.debug("Received request {} at class {}", methodName, className, Arrays.toString(joinPoint.getArgs()));
        }

        Object result = joinPoint.proceed();

        if (log.isDebugEnabled()) {
            log.debug("Returning result {} for request {}", result.toString(), methodName);
        }

        return result;
    }
}
