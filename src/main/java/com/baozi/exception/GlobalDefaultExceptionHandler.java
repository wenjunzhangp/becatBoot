package com.baozi.exception;

import com.baozi.constants.Constants;
import com.baozi.controller.base.ResponseBase;
import com.baozi.util.DateUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.validation.BindException;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.ResponseBody;

import java.beans.PropertyEditorSupport;
import java.util.Date;
import java.util.List;

/**
 * 异常集中处理
 * @author wenjunzhangp
 * @since 2018-08-01
 */
@ControllerAdvice
public class GlobalDefaultExceptionHandler{

    private static final Logger logger = LoggerFactory.getLogger(GlobalDefaultExceptionHandler.class);

    /**
     * 全局异常拦截,统一返回错误消息
     * @param ex
     * @return
     */
    @ExceptionHandler({Exception.class})
    @ResponseBody
    public ResponseBase<String> exceptionHandle(Exception ex) {
        String message = ex.getMessage();
        ResponseBase<String> responseBaseVo = new ResponseBase<String>();
        if (ex instanceof BecatBootException) {
            responseBaseVo.setCode(Constants.BUSINESS_ERROR);
            logger.error(message);
        } else if (ex instanceof BindException){
            BindException bindException = (BindException) ex;
            message = getBindingErrors(bindException.getBindingResult());
            responseBaseVo.setCode(Constants.PARAMETER_VALIDATION_ERROR);
            logger.error(message);
        }else if (ex instanceof MethodArgumentNotValidException){
            MethodArgumentNotValidException methodArgumentNotValidException = (MethodArgumentNotValidException) ex;
            message = getBindingErrors(methodArgumentNotValidException.getBindingResult());
            responseBaseVo.setCode(Constants.PARAMETER_VALIDATION_ERROR);
            logger.error(message);
        } else {
            responseBaseVo.setCode(Constants.SYSTEM_ERROR);
            logger.error(message,ex);
        }
        responseBaseVo.setError(message);
        return responseBaseVo;
    }

    /**
     * 组装参数校验错误信息
     * @param bindingResult
     * @return
     */
    private String getBindingErrors(BindingResult bindingResult) {
        List<FieldError> fieldErrors = bindingResult.getFieldErrors();
        StringBuilder errorMsg = new StringBuilder();
        for (FieldError fieldError : fieldErrors) {
            errorMsg.append(fieldError.getDefaultMessage()).append(";");
        }
        return errorMsg.toString();
    }

    /**
     * 参数绑定
     * @param binder
     */
    @InitBinder
    protected void initBinder(WebDataBinder binder) {
        binder.registerCustomEditor(Date.class, new PropertyEditorSupport() {
            @Override
            public void setAsText(String text) {
                setValue(DateUtil.str2Date(text));
            }
        });
    }

}
