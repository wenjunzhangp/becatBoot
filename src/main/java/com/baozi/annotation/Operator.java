package com.baozi.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;


/**
 * 记录操作日志
 * @author wenjunzhangp
 * @since 2018-08-01
 */
@Target({ElementType.FIELD, ElementType.METHOD, ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
public @interface Operator {
    //执行方法的动作
    public String story() default "";
    //类名
    public String name() default "";
    //是否是标记日志类
    public boolean operatorClass() default true;
}
