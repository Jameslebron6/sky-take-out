package com.sky.aspect;

import com.sky.annotation.AutoFill;
import com.sky.constant.AutoFillConstant;
import com.sky.context.BaseContext;
import com.sky.enumeration.OperationType;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.etsi.uri.x01903.v13.GenericTimeStampType;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;
import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * 自定义切面，实现公共字段自动填充处理器
 */
@Aspect
@Component
@Slf4j
public class AutoFillAspect {
    /**
     * 切入点
     */
    @Pointcut("execution(* com.sky.mapper.*.*(..))&& @annotation(com.sky.annotation.AutoFill)")
    public void autoFillPointCut(){}

    /**
     * 前置通知，在通知中进行公共字段的赋值
     */
    @Before("autoFillPointCut()")
    public void autoFill(JoinPoint joinPoint){
        log.info("开始进行公共字段自动填充。。");
        //获取到当前被拦截到方法上的数据库操作类型
        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        AutoFill autoFill=signature.getMethod().getAnnotation(AutoFill.class);
        OperationType value = autoFill.value();
        Object[] args = joinPoint.getArgs();
        if(args==null||args.length==0){
            return;
        }
        //获取到当前被拦截的方法的参数--实体对象
        Object objects=args[0];
        //准备赋值的数据
        LocalDateTime now= LocalDateTime.now();
        Long currentId=BaseContext.getCurrentId();
        //根据当前不同的数据类型，为对应的属性通过反射来赋值
        if(value==OperationType.INSERT){
            try {
                Method setCreateTime = objects.getClass().getDeclaredMethod(AutoFillConstant.SET_CREATE_TIME, LocalDateTime.class);
                Method setCreateUser = objects.getClass().getDeclaredMethod(AutoFillConstant.SET_CREATE_USER, Long.class);
                Method setUpdateTime = objects.getClass().getDeclaredMethod(AutoFillConstant.SET_UPDATE_TIME, LocalDateTime.class);
                Method setUpdateUser = objects.getClass().getDeclaredMethod(AutoFillConstant.SET_UPDATE_USER, Long.class);
                //通过反射为对象属性赋值
                setCreateTime.invoke(objects,now);
                setCreateUser.invoke(objects,currentId);
                setUpdateTime.invoke(objects,now);
                setUpdateUser.invoke(objects,currentId);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        } else if (value==OperationType.UPDATE) {
            try {
                Method setUpdateTime = objects.getClass().getDeclaredMethod(AutoFillConstant.SET_UPDATE_TIME, LocalDateTime.class);
                Method setUpdateUser = objects.getClass().getDeclaredMethod(AutoFillConstant.SET_UPDATE_USER, Long.class);
                //通过反射为对象属性赋值
                setUpdateTime.invoke(objects,now);
                setUpdateUser.invoke(objects,currentId);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }

        }

    }

}
