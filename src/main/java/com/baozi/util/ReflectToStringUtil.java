package com.baozi.util;

import java.lang.reflect.Field;

/**
 * Copyright:   互融云
 * tostring工具类
 * 用法：return ReflectToStringUtil.toStringUtil(this,false);
 * @author: zhangwenjun
 * @version: V1.0
 * @Date: 2018-08-01 13:38
 */
public class ReflectToStringUtil {  
  
    static StringBuffer sb = new StringBuffer();  
  
    public static String toStringUtil(Object clazs,boolean isOutputNull) {  
        getParamAndValue(clazs, clazs.getClass(), isOutputNull);
        return sb.toString();
    }  
  
    private static void getParamAndValue(Object clazs, Class<?> clazz,boolean isOutputNull) {
        Class<?> sc = clazz.getSuperclass();  
        Field[] sfields = sc.getDeclaredFields();  
        if (sfields.length > 0) {  
            getParamAndValue(clazs, sc, isOutputNull);  
        }  
        Field[] fields = clazz.getDeclaredFields();  
        for (Field f : fields) {  
            f.setAccessible(true);  
            try {  
                if (null != f.get(clazs)||isOutputNull){  
                    sb.append(f.getName() + "=" + f.get(clazs) + "\n");  
                }  
            } catch (IllegalArgumentException | IllegalAccessException e) {  
                e.printStackTrace();  
            }  
        }  
    }  
  
} 