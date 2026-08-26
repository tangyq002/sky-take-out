package com.sky.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import com.sky.enumeration.OperationType;


/**
 * 自定义注解，公共字段自动填充
 * @author tyq
 * @date 2026年8月25日
 * @project_name sky-server
 * @package_name com.sky.annotation
 * @file_name AutoFill.java
 * @classname AutoFill
 * @version 2026年8月25日 下午8:47:18
 */
//指定注解作用的位置，方法上
@Target(ElementType.METHOD)
//注解保留到程序运行的时候
@Retention(RetentionPolicy.RUNTIME)
public @interface AutoFill {
	/**
	 * 获取数据库字段信息,是insert还是update
	 * @return
	 */
	OperationType value();
}
