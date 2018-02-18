package com.globant.aspect;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.context.annotation.Configuration;

@Aspect
@Slf4j
@Configuration
public class TimingAspect {

    @Around(value = "@annotation(com.globant.aspect.annotation.Timer)")
    public Object around(ProceedingJoinPoint proceedingJoinPoint) throws Throwable {
        long startTime = System.nanoTime();

        Object result = proceedingJoinPoint.proceed();

        long endTime = System.nanoTime();
        log.info("Method executed in {}ns ", endTime - startTime);
        return result;
    }
}
