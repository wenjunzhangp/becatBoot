package com.baozi.controller.base;

import com.baozi.constants.Constants;
import com.github.pagehelper.PageInfo;
import lombok.Data;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * 自定义返回响应体
 * @author wenjunzhangp
 * @since 2018-08-01
 */
@Data
public class ResponseBase<T> {

    /** 响应码 */
    private int code;
    /** 错误原因 */
    private String error;
    /** 响应数据 */
    private Object data;

    /**
     * 处理成功
     * @param t
     * @return
     */
    public ResponseBase<T> successSenior(T t){
        ResponseBase<T> responseBase = new ResponseBase<>();
        responseBase.setCode(Constants.SUCCESS);
        responseBase.setData(t);
        return responseBase;
    }
    public static ResponseBase success(String data){
        ResponseBase responseBase = new ResponseBase();
        responseBase.setCode(Constants.SUCCESS);
        responseBase.setData(data);
        return responseBase;
    }

    /**
     * 处理失败
     * @param msg
     * @return
     */
    public ResponseBase<T> errorSenior(String msg){
        ResponseBase<T> responseBase = new ResponseBase<>();
        responseBase.setCode(Constants.ERROE);
        responseBase.setError(msg);
        return responseBase;
    }
    public static ResponseBase error(String msg){
        ResponseBase responseBase = new ResponseBase<>();
        responseBase.setCode(Constants.ERROE);
        responseBase.setError(msg);
        return responseBase;
    }

    /**
     * 自定义返回处理失败的方法
     * @param msg
     * @param code
     * @return
     */
    public ResponseBase<T> error(String msg,int code){
        ResponseBase<T> responseBase = new ResponseBase<>();
        responseBase.setCode(code);
        responseBase.setError(msg);
        return responseBase;
    }

    /**
     * 自定义返回分页组装数据方法
     * @param pageInfo
     */
    public static Map<String, Object> setResultMapOkByPage(PageInfo pageInfo){
        Map<String, Object> resultMap = new LinkedHashMap<String, Object>(16);
        resultMap.put("code",Constants.SUCCESS);
        resultMap.put("msg","");
        resultMap.put("count",pageInfo.getTotal());
        resultMap.put("data",pageInfo.getList());
        return resultMap;
    }

    /**
     * 分页返回失败的响应体
     * @param e 异常信息
     */
    public static Map<String, Object> setResultMapError( Exception e ){
        Map<String, Object> resultMap = new LinkedHashMap<String, Object>(16);
        resultMap.put("code",Constants.ERROE);
        resultMap.put("msg",e.getMessage());
        return resultMap;
    }
}
