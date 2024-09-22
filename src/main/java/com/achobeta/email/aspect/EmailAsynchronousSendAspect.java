package com.achobeta.email.aspect;

import com.achobeta.exception.GlobalServiceException;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;

/**
 * Created With Intellij IDEA
 * Description:
 * User: 马拉圈
 * Date: 2024-09-15
 * Time: 15:20
 */
@Component
@Aspect
@Slf4j
public class EmailAsynchronousSendAspect {

    @Pointcut("execution(* com.achobeta.email.EmailSender.*(..))")
    public void send() {}

    @Around("send()")
    public Object doAround(ProceedingJoinPoint joinPoint) {
        // 如果是自调用则不会触发切点
        EmailAsynchronousThreadPool.submit(() -> {
            try {
                log.info("异步发送邮件");
                joinPoint.proceed();
            } catch (Throwable e) {
                throw new GlobalServiceException(e.getMessage());
            }
        });
        return null;
    }

}
