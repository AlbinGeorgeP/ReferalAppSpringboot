package com.social.referral.aspect;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.Signature;
import org.aspectj.lang.annotation.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Configuration;

@Configuration
@Aspect
public class LoggingAspect {
    @Pointcut("@within(org.springframework.stereotype.Repository)"
            +"||@within(org.springframework.stereotype.Service)"
            +"||@within(org.springframework.web.bind.annotation.RestController)")
    public void springBeanPointCut()
    {}
    @Around(" springBeanPointCut()")
    public Object logAround(ProceedingJoinPoint joinPoint) throws Throwable
    {
        Object obj;
        Logger log=LoggerFactory.getLogger(joinPoint.getSignature().getName());
        log.info("Entered Method {}",joinPoint.getSignature().getName());
        obj= joinPoint.proceed();
        log.info("Exit from method {}",joinPoint.getSignature().getName());
        return obj;
    }

    @AfterThrowing(value = "springBeanPointCut()", throwing = "error")
    public void logAfterThrowing(JoinPoint joinPoint, Throwable error)
    {
        Signature sign=joinPoint.getSignature();
        final Logger log=LoggerFactory.getLogger(sign.getDeclaringTypeName());
        log.error("Exception occurred for method {} : Error is {}",sign.getName(),error.getMessage());
    }
}

