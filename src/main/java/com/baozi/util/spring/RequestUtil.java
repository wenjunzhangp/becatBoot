package com.baozi.util.spring;

import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.io.File;

/**
 * Copyright:   互融云
 * 从requet中取值
 * @author: zhangwenjun
 * @version: V1.0
 * @Date: 2018-08-01 13:38
 */
public class RequestUtil {

    /**
     * SpringMVC下获取 request
     */
    public static HttpServletRequest getRequest() {
        RequestAttributes requestAttributes = RequestContextHolder.getRequestAttributes();
        if (requestAttributes == null) {
            return null;
        }
        return ((ServletRequestAttributes)requestAttributes).getRequest();
    }

    /**
     * SpringMVC下获取session
     */
    public static HttpSession getSession() {
        HttpServletRequest request = getRequest();
        if (request == null) {
            return null;
        }
        return request.getSession();
    }

    /**
     * SpringMVC 下获取 Servlet RealPath 路径
     */
    public static String getServletRealPath(){
        return getSession().getServletContext().getRealPath(File.separator);
    }
}
