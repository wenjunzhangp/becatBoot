package com.baozi.service.wechat;


import com.baozi.util.JsonResult;

import javax.servlet.http.HttpServletRequest;

/**
 * Copyright:   包子
 *
 * @author: zhangwenjun
 * @version: V1.0
 * @Date: 2019-01-02 10:45
 */
public interface WeChatPayService {

    /**
     * 扫码支付：统一下单
     * @param orderno 用户订单号（统一下单时所用的订单号）
     * @param request 获取下单客户IP
     * @return
     */
    JsonResult handleDoUnifiedOrder(String orderno, HttpServletRequest request);

    /**
     * 微信WAP支付
     * @param orderno 订单号
     * @param request
     * @return
     */
    JsonResult handleDoWapUnifiedOrder(String orderno, HttpServletRequest request);

    /**
     * 微信APP支付
     * @param orderno 订单号
     * @param request
     * @return
     */
    JsonResult handleDoAPPUnifiedOrder(String orderno, HttpServletRequest request);

    /**
     * 统一关闭订单：关闭订单
     * @param orderno 用户订单号（统一下单时所用的订单号）
     * @return
     */
    JsonResult doOrderClose(String orderno);

}
