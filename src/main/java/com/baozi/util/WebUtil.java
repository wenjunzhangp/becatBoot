package com.baozi.util;

import com.baomidou.mybatisplus.plugins.Page;
import com.baozi.controller.base.ResponseBase;
import com.baozi.util.json.JsonUtil;
import org.apache.commons.lang.StringUtils;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.context.request.ServletWebRequest;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.Map;

/**
 * 接收参数 组装参数工具类
 * @author wenjunzhangp
 * @since 2018-08-01
 */
public class WebUtil  {

    public static HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();

    public static <T> Page<T> getPage(int defaultSie) {
        String size = request.getParameter("limit");
        String index = request.getParameter("page");
        int sizeInt = StringUtils.isNotBlank(size)?Integer.parseInt(size):defaultSie;
        int current = StringUtils.isNotBlank(index)?Integer.parseInt(index):1;
        return new Page<>(current, sizeInt);
    }

    public static void responseWrit(ResponseBase responseBase){
        ServletWebRequest servletWebRequest= new ServletWebRequest(request);
        HttpServletResponse response=servletWebRequest.getResponse();
        response.setCharacterEncoding("UTF-8");
        response.setContentType("application/json; charset=utf-8");
        PrintWriter out = null;
        try {
            out = response.getWriter();
            out.append(JsonUtil.toJsonString(responseBase));
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (out != null) {
                out.close();
            }
        }
    }

    public static void responseWrit(HttpServletResponse response, ResponseBase responseBase){
        response.setCharacterEncoding("UTF-8");
        response.setContentType("application/json; charset=utf-8");
        PrintWriter out = null;
        try {
            out = response.getWriter();
            out.append(JsonUtil.toJsonString(responseBase));
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (out != null) {
                out.close();
            }
        }
    }

    /**
     * 获取请求属性封装为Map类型
     * @param request
     * @return
     */
    public static Map<String, Object> genRequestMapSingle(HttpServletRequest request) {
        Map<String, Object> conditions = new HashMap<>(256);
        Map map = request.getParameterMap();
        for (Object o : map.keySet()) {
            String key = (String) o;
            conditions.put(key, ((String[]) map.get(key))[0]);
        }
        return conditions;
    }

}
