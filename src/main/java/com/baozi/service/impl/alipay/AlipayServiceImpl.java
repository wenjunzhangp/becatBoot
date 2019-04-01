package com.baozi.service.impl.alipay;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.alipay.api.AlipayClient;
import com.alipay.api.DefaultAlipayClient;
import com.alipay.api.internal.util.AlipaySignature;
import com.alipay.api.request.AlipayTradeCloseRequest;
import com.alipay.api.request.AlipayTradePagePayRequest;
import com.alipay.api.request.AlipayTradeWapPayRequest;
import com.alipay.api.response.AlipayTradeCloseResponse;
import com.baozi.config.AlipayConfig;
import com.baozi.service.alipay.AlipayService;
import com.baozi.util.DateUtil;
import com.baozi.util.JsonResult;
import com.baozi.util.LogUtils;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.net.URLEncoder;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.TreeMap;


/**
 * Copyright:   包子
 * 封装支付宝支付工具类
 *
 * @author: zhangwenjun
 * @version: V1.0
 * @Date: 2018-11-19 19:19
 */
@Service
public class AlipayServiceImpl implements AlipayService {

    /**
     * 获得初始化的AlipayClient
     */
    private static AlipayClient alipayClient = null;

    static {
        alipayClient = new DefaultAlipayClient(AlipayConfig.gatewayUrl, AlipayConfig.app_id, AlipayConfig.merchant_private_key, "json", AlipayConfig.charset, AlipayConfig.alipay_public_key, AlipayConfig.sign_type);
    }


    /**
     * 统一收单下单并支付页面接口
     * alipay.trade.page.pay
     *
     * @param orderno 订单号
     * @param request
     * @return
     */
    @Override
    public JsonResult pcPay(String orderno, HttpServletRequest request) {
        try {
            //设置请求参数
            AlipayTradePagePayRequest alipayRequest = new AlipayTradePagePayRequest();
            alipayRequest.setReturnUrl(AlipayConfig.alipay_pc_return_url);
            alipayRequest.setNotifyUrl(AlipayConfig.alipay_pc_notify_url);
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("out_trade_no", "out_trade_no");
            jsonObject.put("product_code", "FAST_INSTANT_TRADE_PAY");
            jsonObject.put("total_amount", "total_amount");
            jsonObject.put("subject", "subject");
            jsonObject.put("body", "body");
            jsonObject.put("timeout_express", "timeout_express");
            //这个备用参数回调方法里会用，根据这个订单号找当前支付的订单，来修改订单状态
            JSONObject back = new JSONObject();
            back.put("orderNo", "orderNo");
            jsonObject.put("passback_params", URLEncoder.encode(back.toJSONString(), "UTF-8"));
            String bizcontent = jsonObject.toJSONString();
            alipayRequest.setBizContent(bizcontent);
            //唤起请求
            String result = alipayClient.pageExecute(alipayRequest).getBody();
            return new JsonResult().setSuccess(true).setObj(result);
        } catch (Exception e) {
            LogUtils.logError("支付宝下单业务出现异常", e);
            return new JsonResult().setSuccess(false).setMsg("支付宝支付渠道发生未知问题，请您暂时更换其他支付渠道").setCode("500");
        }
    }

    /**
     * H5下单接口
     *
     * @param orderno 订单号
     * @param request
     * @return
     */
    @Override
    public JsonResult wapPay(String orderno, HttpServletRequest request) {
        try {
            //设置请求参数
            AlipayTradeWapPayRequest alipayRequest = new AlipayTradeWapPayRequest();
            alipayRequest.setReturnUrl(AlipayConfig.alipay_wap_return_url);
            alipayRequest.setNotifyUrl(AlipayConfig.alipay_wap_notify_url);
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("subject", "subject");
            jsonObject.put("out_trade_no", "out_trade_no");
            jsonObject.put("total_amount", "total_amount");
            jsonObject.put("product_code", "QUICK_WAP_WAY");
            jsonObject.put("body", "body");
            jsonObject.put("timeout_express", "timeout_express");
            jsonObject.put("time_expire", "time_expire");
            jsonObject.put("goods_type", "1");
            String bizcontent = jsonObject.toJSONString();
            alipayRequest.setBizContent(bizcontent);
            //唤起请求
            String response = alipayClient.pageExecute(alipayRequest,"GET").getBody();
            return new JsonResult().setSuccess(true).setObj(response);
        } catch (Exception e) {
            LogUtils.logError("支付宝下单业务出现异常", e);
            return new JsonResult().setSuccess(false).setMsg("支付宝支付渠道发生未知问题，请您暂时更换其他支付渠道").setCode("500");
        }
    }

    /**
     * app 支付接口
     * alipay.trade.app.pay
     *
     * @param orderno 订单号
     * @param request
     * @return
     */
    @Override
    public JsonResult appPay(String orderno, HttpServletRequest request) {
        try {
            Map<String,String> map = new TreeMap<String, String>();
            Map<String,String> bizMap = new HashMap<String,String>();
            bizMap.put("subject", "subject");
            bizMap.put("out_trade_no", "out_trade_no");
            bizMap.put("total_amount", "total_amount");
            bizMap.put("product_code", "QUICK_MSECURITY_PAY");
            String bizStr = JSON.toJSONString(bizMap);
            map.put("app_id", AlipayConfig.app_id);
            map.put("method", "alipay.trade.app.pay");
            map.put("charset", "utf-8");
            map.put("sign_type", "RSA2");
            //时间格式参照官方给的中间有空格 yyyy-MM-dd HH:mm:ss
            map.put("timestamp", "timestamp");
            map.put("version", "1.0");
            map.put("notify_url", AlipayConfig.alipay_wap_notify_url);
            map.put("biz_content", bizStr);
            //拼接好的含有&的字符串
            String jsonStr = AlipaySignature.getSignContent(map);
            //获取签名（支付宝官方api）
            String sign = AlipaySignature.rsaSign(jsonStr,AlipayConfig.merchant_private_key,"utf-8","RSA2");
            map.put("sign", sign);
            //对value值进行url编码
            for(Map.Entry<String, String> map2:map.entrySet()){
                String key = map2.getKey();
                String value = map2.getValue();
                value = URLEncoder.encode(value);
                map.put(key, value);
            }
            //获取最终的json串（支付宝官方api）
            String realStr = AlipaySignature.getSignContent(map);
            return new JsonResult().setSuccess(true).setObj(realStr);
        } catch (Exception e) {
            LogUtils.logError("支付宝下单业务出现异常", e);
            return new JsonResult().setSuccess(false).setMsg("支付宝支付渠道发生未知问题，请您暂时更换其他支付渠道").setCode("500");
        }
    }

    /**
     * 统一收单交易关闭接口
     * alipay.trade.close
     *
     * @param orderno  商户订单号
     * @param alipayno 阿里订单号
     * @return
     */
    @Override
    public JsonResult handlePayClose(String orderno, String alipayno) {
        try {
            //设置请求参数
            AlipayTradeCloseRequest alipayRequest = new AlipayTradeCloseRequest();
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("out_trade_no", orderno);
            jsonObject.put("trade_no", alipayno);
            alipayRequest.setBizContent(jsonObject.toJSONString());
            AlipayTradeCloseResponse response = alipayClient.execute(alipayRequest);
            if (response.isSuccess()) {
                //TODO 内部系统业务逻辑处理 更新订单状态已关闭
                LogUtils.logInfo("支付宝返回关闭报文【" + JSON.toJSONString(response.getBody()) + "】");
                return new JsonResult().setSuccess(true).setMsg("订单已关闭");
            } else {
                return new JsonResult().setSuccess(false).setMsg("交易关闭接口调用失败["+response.getMsg()+"]");
            }
        } catch (Exception e) {
            LogUtils.logError("支付宝关闭订单接口调用异常", e);
            return new JsonResult().setSuccess(false).setMsg("支付宝关闭订单接口调用异常["+e.getMessage()+"]").setCode("500");
        }
    }
}
