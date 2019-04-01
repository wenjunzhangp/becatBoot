package com.baozi.service.alipay;

import com.baozi.util.JsonResult;

import javax.servlet.http.HttpServletRequest;

/**
 * Copyright:   包子
 *  封装支付宝支付工具类
 * @author: zhangwenjun
 * @version: V1.0
 * @Date: 2018-11-19 19:17
 */
public interface AlipayService {

    /**
     * 统一收单下单并支付页面接口
     * alipay.trade.page.pay
     * @param orderno 订单号
     * @return
     */
    JsonResult pcPay(String orderno, HttpServletRequest request);

    /**
     * H5下单接口
     * @param orderno 订单号
     * @param request
     * @return
     */
    JsonResult wapPay(String orderno, HttpServletRequest request);

    /**
     * app 支付接口
     * alipay.trade.app.pay
     * @param orderno 订单号
     * @return
     */
    JsonResult appPay(String orderno, HttpServletRequest request);

    /**
     *  统一收单交易关闭接口
     * alipay.trade.close
     * @param orderno 商户订单号
     * @param alipayno 阿里订单号
     * @return
     */
    JsonResult handlePayClose(String orderno, String alipayno);

}
