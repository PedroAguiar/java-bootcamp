package com.globant.aspect;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Configuration;

@Aspect
@Configuration
public class LoggingAspect {

    private Logger LOGGER = LoggerFactory.getLogger(this.getClass());

    @Around(value = "@annotation(com.globant.aspect.annotation.Timer)")
    public Object around(ProceedingJoinPoint proceedingJoinPoint) throws Throwable {
        long startTime = System.nanoTime();

        Object result = proceedingJoinPoint.proceed();

        long endTime = System.nanoTime();

        LOGGER.info("Method executed in {}ns ", endTime - startTime);
        return result;
    }
}
