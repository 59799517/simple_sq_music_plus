package com.sqmusicplus.v3.config.aspect;

import com.sqmusicplus.v3.config.GlobalStatic;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;

@Slf4j
@Aspect
@Component
public class TaskAspect {

    @Pointcut("execution(* com.sqmusicplus.v3.task..*.*(..)) && execution(* com.sqmusicplus.v3.task..excute*(..))")
    public void taskPointCut() {}

    @Around("taskPointCut()")
    public Object around(ProceedingJoinPoint point) throws Throwable{
        if(!GlobalStatic.IS_INIT){
            String methodName = point.getSignature().getName();
            log.warn("系统未初始化完成，跳过执行任务: {}", point.getSignature().getDeclaringType().getName()+":"+methodName);
            return null;
        }
        return point.proceed();
    }


}
