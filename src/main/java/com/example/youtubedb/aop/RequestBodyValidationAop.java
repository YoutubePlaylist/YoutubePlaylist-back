package com.example.youtubedb.aop;

import com.example.youtubedb.exception.DoNotChangePasswordException;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;
import org.springframework.validation.BindingResult;

import java.util.Arrays;
import java.util.Objects;

@Component
@Aspect
public class RequestBodyValidationAop {

    //TODO : 2안 Request Body validation AOP로 꺼내서 처리하기 - BindingResult 활용
    @Pointcut("execution(* com.example.youtubedb.controller.MemberController.*(..))")
    public void pointCut() {
    }


    @Before("pointCut()")
    public void testAop(JoinPoint joinPoint) {
        Object[] objs = joinPoint.getArgs();
        for (Object obj : objs) {
            if (obj instanceof BindingResult) {
                BindingResult result = (BindingResult) obj;
                if (result.hasErrors()) {
//                    if(result.getFieldError())
                    System.out.println("test");
                    System.out.println(result.getFieldError());

                }
            }
        }

        System.out.println("aopTest");
    }


}
