package com.baozi.service.impl.wechat;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.baozi.config.Iconfig;
import com.baozi.config.WeChatConfig;
import com.baozi.service.wechat.WeChatPayService;
import com.baozi.util.DateUtil;
import com.baozi.util.JsonResult;
import com.baozi.util.LogUtils;
import com.github.wxpay.sdk.WXPay;
import com.github.wxpay.sdk.WXPayUtil;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.math.BigDecimal;
import java.net.URLEncoder;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import java.util.SortedMap;
import java.util.TreeMap;

/**
 * Copyright:   包子
 *
 * @author: zhangwenjun
 * @version: V1.0
 * @Date: 2019-01-02 11:14
 */
@Service
public class WeChatPayServiceImpl implements WeChatPayService {

    /**
     * 微信支付API
     */
    private WXPay wxpay;

    /**
     * 微信支付API 配置类
     */
    private WXPayConfigImpl config;

    /**
     * 单例模式：当前类实例
     */
    private static WeChatPayServiceImpl INSTANCE;

    public WeChatPayServiceImpl() throws Exception {
        config = WXPayConfigImpl.getInstance();
        wxpay = new WXPay(config);
    }

    public static WeChatPayServiceImpl getInstance() throws Exception {
        if (INSTANCE == null) {
            synchronized (WeChatPayServiceImpl.class) {
                if (INSTANCE == null) {
                    INSTANCE = new WeChatPayServiceImpl();
                }
            }
        }
        return INSTANCE;
    }

    /**
     * 得到10 位的时间戳
     * 如果在JAVA上转换为时间要在后面补上三个0
     * @return
     */
    public static String getTenTimes(){
        String t = new Date().getTime()+"";
        t = t.substring(0, t.length()-3);
        return t;
    }

    /**
     * 得到随机字符串
     * @return
     */
    public static String getRandomString(){
        int length = 32;
        String str = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
        Random random = new Random();
        StringBuffer sb = new StringBuffer();

        for(int i = 0; i < length; ++i){
            int number = random.nextInt(62);//[0,62)
            sb.append(str.charAt(number));
        }
        return sb.toString();
    }


    /**
     * 扫码支付：统一下单
     *
     * @param orderno 用户订单号（统一下单时所用的订单号）
     * @param request 获取下单客户IP
     * @return
     */
    @Override
    public JsonResult handleDoUnifiedOrder(String orderno, HttpServletRequest request) {
        try {
            HashMap<String, String> data = new HashMap<String, String>();
            data.put("body", "body");
            data.put("out_trade_no", "out_trade_no");
            data.put("total_fee", "total_fee");
            data.put("spbill_create_ip", "spbill_create_ip");
            data.put("notify_url", WeChatConfig.wechat_pc_pay_do_unifiedorder_notify_url);
            data.put("trade_type", WeChatConfig.wechat_pay_trade_type_pc);
            JSONObject back = new JSONObject();
            back.put("orderNo", "orderNo");
            data.put("attach", back.toJSONString());
            data.put("time_start", "time_start");
            data.put("time_expire", "time_expire");
            data.put("product_id", "product_id");
            Map<String, String> result = wxpay.unifiedOrder(data);
            if (StringUtils.isNotEmpty(result.get("return_code")) && "SUCCESS".equals(result.get("return_code")) &&
                    StringUtils.isNotEmpty(result.get("result_code")) && "SUCCESS".equals(result.get("result_code"))) {
                //根据这个字段生成二维码，展示到前端供用户扫码支付
                String binary = result.get("code_url");
                return new JsonResult().setSuccess(true).setMsg("下单成功请尽快支付").setObj(binary);
            } else {
                return new JsonResult().setSuccess(false).setMsg("微信下单失败[" + result.get("return_msg") + "]");
            }
        } catch (Exception e) {
            LogUtils.logError("微信扫码支付统一下单出现异常", e);
            return new JsonResult().setSuccess(false).setMsg("微信支付渠道发生未知问题，请您暂时更换其他支付渠道");
        }
    }

    /**
     * 微信WAP支付
     *
     * @param orderno 订单号
     * @param request
     * @return
     */
    @Override
    public JsonResult handleDoWapUnifiedOrder(String orderno, HttpServletRequest request) {
        try {
            HashMap<String, String> data = new HashMap<String, String>();
            data.put("body", "body");
            data.put("out_trade_no", "out_trade_no");
            data.put("total_fee", "total_fee");
            data.put("spbill_create_ip", "spbill_create_ip");
            data.put("notify_url", WeChatConfig.wechat_wap_pay_do_unifiedorder_notify_url);
            data.put("trade_type", WeChatConfig.wechat_pay_trade_type_wap);
            JSONObject back = new JSONObject();
            back.put("orderNo", "orderNo");
            String tradejson = back.toJSONString();
            data.put("attach", tradejson);
            data.put("time_start", "time_start");
            data.put("time_expire", "time_expire");
            data.put("scene_info", "{\"h5_info\": {\"type\":\"Wap\",\"wap_url\": \"M端线上url\",\"wap_name\": \"你的名称\"}}");
            Map<String, String> result = wxpay.unifiedOrder(data);
            if (StringUtils.isNotEmpty(result.get("return_code")) && "SUCCESS".equals(result.get("return_code")) &&
                    StringUtils.isNotEmpty(result.get("result_code")) && "SUCCESS".equals(result.get("result_code"))) {
                String callbackurl = result.get("mweb_url");
                return new JsonResult().setSuccess(true).setMsg("下单成功请尽快支付").setObj(callbackurl+"&redirect_url="+URLEncoder.encode("你的网址", "UTF-8"));
            } else {
                return new JsonResult().setSuccess(false).setMsg("微信下单失败【" + result.get("return_msg") + "】");
            }
        } catch (Exception e) {
            LogUtils.logError("WAP微信支付统一下单出现异常", e);
            return new JsonResult().setSuccess(false).setMsg("微信支付渠道发生未知问题，请您暂时更换其他支付渠道");
        }
    }

    /**
     * 微信APP支付
     *
     * @param orderno 订单号
     * @param request
     * @return
     */
    @Override
    public JsonResult handleDoAPPUnifiedOrder(String orderno, HttpServletRequest request) {
        try {
            HashMap<String, String> data = new HashMap<String, String>();
            data.put("body", "body");
            data.put("out_trade_no", "out_trade_no");
            data.put("total_fee", "total_fee");
            data.put("spbill_create_ip", "spbill_create_ip");
            data.put("notify_url", WeChatConfig.wechat_wap_pay_do_unifiedorder_notify_url);
            data.put("trade_type", "APP");
            JSONObject back = new JSONObject();
            back.put("orderNo", "orderNo");
            String tradejson = back.toJSONString();
            data.put("attach", tradejson);
            data.put("time_start", "starttime");
            data.put("time_expire", "time_expire");
            data.put("product_id", orderno);
            Map<String, String> result = wxpay.unifiedOrder(data);
            if (StringUtils.isNotEmpty(result.get("return_code")) && "SUCCESS".equals(result.get("return_code")) &&
                    StringUtils.isNotEmpty(result.get("result_code")) && "SUCCESS".equals(result.get("result_code"))) {
                //填充json实体
                JSONObject authcpay = new JSONObject();
                String time = getTenTimes();
                authcpay.put("appid",result.get("appid"));
                authcpay.put("partnerid",result.get("mch_id"));
                authcpay.put("prepayid",result.get("prepay_id"));
                authcpay.put("package","Sign=WXPay");
                authcpay.put("noncestr",result.get("nonce_str"));
                authcpay.put("timestamp",time);
                //根据商户key二次签名  生成二次签名串
                SortedMap<String, String> signParam = new TreeMap<String, String>();
                signParam.put("appid",result.get("appid"));
                signParam.put("partnerid",result.get("mch_id"));
                signParam.put("prepayid",result.get("prepay_id"));
                signParam.put("package","Sign=WXPay");
                signParam.put("noncestr",result.get("nonce_str"));
                signParam.put("timestamp",time);
                String signAgain = WXPayUtil.generateSignature(signParam,WeChatConfig.wechat_pay_key);
                authcpay.put("sign",signAgain);
                return new JsonResult().setSuccess(true).setMsg("下单成功请尽快支付").setObj(authcpay.toJSONString());
            } else {
                return new JsonResult().setSuccess(false).setMsg("微信下单失败【" + result.get("return_msg") + "】");
            }
        } catch (Exception e) {
            LogUtils.logError("微信app支付统一下单出现异常", e);
            return new JsonResult().setSuccess(false).setMsg("微信支付渠道发生未知问题，请您暂时更换其他支付渠道");
        }
    }

    /**
     * 统一关闭订单：关闭订单
     *
     * @param orderno 用户订单号（统一下单时所用的订单号）
     * @return
     */
    @Override
    public JsonResult doOrderClose(String orderno) {
        try {
            HashMap<String, String> data = new HashMap<String, String>();
            data.put("out_trade_no", orderno);
            Map<String, String> result = wxpay.closeOrder(data);
            if (StringUtils.isNotEmpty(result.get("return_code")) && "SUCCESS".equals(result.get("return_code")) &&
                    StringUtils.isNotEmpty(result.get("result_code")) && "SUCCESS".equals(result.get("result_code"))) {
                //TODO 内部系统业务逻辑处理 更新订单状态已关闭
                LogUtils.logInfo("微信返回关闭报文【"+JSON.toJSONString(result)+"】");
                return new JsonResult().setSuccess(true).setMsg("订单已关闭");
            } else {
                return new JsonResult().setSuccess(false).setMsg("订单关闭失败[" + result.get("return_msg") + "]");
            }
        } catch (Exception e) {
            LogUtils.logError("微信支付关闭订单出现异常", e);
            return new JsonResult().setSuccess(false).setMsg("关闭订单失败");
        }
    }
}
