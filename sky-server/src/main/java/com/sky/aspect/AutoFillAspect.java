package com.sky.aspect;

import java.lang.reflect.Method;
import java.time.LocalDateTime;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.stereotype.Component;

import com.sky.annotation.AutoFill;
import com.sky.constant.AutoFillConstant;
import com.sky.context.BaseContext;
import com.sky.enumeration.OperationType;

import lombok.extern.slf4j.Slf4j;

/**
 * 自定义切面类
 * @author tyq
 * @date 2026年8月25日
 * @project_name sky-server
 * @package_name com.sky.aspect
 * @file_name AutoFillAspect.java
 * @classname AutoFillAspect
 * @version 2026年8月25日 下午8:55:06
 */
@Aspect
@Component
@Slf4j
public class AutoFillAspect {
	
	/**
	 * 切入点方法
	 * 自定义注解实现切入点
	 */
	@Pointcut("execution(* com.sky.mapper .*.* ( .. )) && @annotation(com.sky.annotation.AutoFill)")
	public void autoFillAspect() {
		
	}
	
	/**
	 * 前置通知，在新增修改之前赋值公共字段
	 * @param joinpoint
	 */
	@Before("autoFillAspect()")
	public void autoFill(JoinPoint joinpoint) {
		log.info("autoFill切入点前置通知开始");
		//获取注解类型，是新增还是修改
		MethodSignature methodSignature = (MethodSignature) joinpoint.getSignature();
		//获取当前提交方式
		Method method = methodSignature.getMethod();
		//通过当前的提交方式获取当前的注解内容
		OperationType value = method.getAnnotation (AutoFill.class).value();
		
		//获取当前传参对象
		Object[] args = joinpoint.getArgs () ;
		//判断传递的参数是否为空
		if (args == null || args.length == 0) {
			return;
		}
		//不为空获取当前传递对象
		Object object = args [0];
		//获取当期时间和当前登录用户
		LocalDateTime now = LocalDateTime. now() ;
		Long currentId = BaseContext.getCurrentId();
		
		//通过反射对数据赋值
		//1.当为新增操作时
		if (value == OperationType.INSERT) {
			try {
				//通过反射获取当前的创建时间类
				Method setCreateTime = object.getClass().getDeclaredMethod(AutoFillConstant.SET_CREATE_TIME, LocalDateTime.class);
				//获取当前的创建用户的方法
				Method setCreateUser = object.getClass().getDeclaredMethod (AutoFillConstant.SET_CREATE_USER, Long.class);
				//获取当前时间的更新的方法
				Method setUpdateTime = object.getClass().getDeclaredMethod(AutoFillConstant.SET_UPDATE_TIME, LocalDateTime.class);
				//当前用户更新
				Method setUpdateUser = object.getClass () .getDeclaredMethod(AutoFillConstant.SET_UPDATE_USER, Long.class);
				
				//进行赋值
				setCreateTime.invoke (object, now);
				setCreateUser.invoke (object, currentId);
				setUpdateTime.invoke (object, now);
				setUpdateUser.invoke (object, currentId) ;
			}catch (Exception e) {
				e.printStackTrace ();
			}
		}//2.当为修改操作时
		else if (value == OperationType.UPDATE) {
			try {
				//获取当前时间的更新的方法
				Method setUpdateTime = object.getClass().getDeclaredMethod(AutoFillConstant.SET_UPDATE_TIME, LocalDateTime.class);
				//当前用户更新
				Method setUpdateUser = object.getClass () .getDeclaredMethod(AutoFillConstant.SET_UPDATE_USER, Long.class);
				
				//进行赋值
				setUpdateTime.invoke (object, now);
				setUpdateUser.invoke (object, currentId) ;
			}catch (Exception e) {
				e.printStackTrace ();
			}
		}
	}
}
