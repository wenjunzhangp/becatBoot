package com.baozi.util;

import java.io.Serializable;

/**
 * Copyright:   西邻在线
 * 封装json结果集
 *
 * @author: zhangwenjun
 * @version: V1.0
 * @Date: 2018-10-19 10:42
 */
public class JsonResult implements Serializable {

    private Boolean success = false;// 返回是否成功
    private String msg = "";// 返回信息
    private Object data = null;// 返回其他对象信息
    private String code = "";//提示代码

    public Boolean getSuccess() {
        return success;
    }

    public JsonResult setSuccess(Boolean success) {
        this.success = success;
        return this;
    }

    public String getMsg() {
        return msg;
    }

    public JsonResult setMsg(String msg) {
        this.msg = msg;
        return this;
    }

    public Object getObj() {
        return data;
    }

    public JsonResult setObj(Object obj) {
        this.data = obj;
        return this;
    }

    /**
     * <p> TODO</p>
     *
     * @return: String
     */
    public String getCode() {
        return code;
    }

    /**
     * <p> TODO</p>
     *
     * @return: String
     */
    public JsonResult setCode(String code) {
        this.code = code;
        return this;
    }
}
