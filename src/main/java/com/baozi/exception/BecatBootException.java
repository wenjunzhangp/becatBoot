package com.baozi.exception;

/**
 * 自定义业务异常处理类
 * @author wenjunzhangp
 * @since 2018-08-01
 */
public class BecatBootException extends RuntimeException{

    private static final long serialVersionUID = 1L;

    public BecatBootException(String message) {
        super(message);
    }

}
