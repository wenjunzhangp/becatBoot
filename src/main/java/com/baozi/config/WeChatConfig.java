package com.baozi.config;


/**
 * Copyright:   包子
 * 微信扫码支付配置类
 * @author: zhangwenjun
 * @version: V1.0
 * @Date: 2019-01-02 10:28
 */
public class WeChatConfig {

    // 微信应用的appid
    public static String wechat_pay_appid = Iconfig.get("wechat_pay_appid");

    // 微信商家后台商户id
    public static String wechat_pay_mchid = Iconfig.get("wechat_pay_mchid");

    // 商户自主生成的32位私钥，线上交易切不可随意更改！
    public static String wechat_pay_key = Iconfig.get("wechat_pay_key");

    // 证书路径，某些微信接口需要API证书授权
    public static String wechat_pay_cert_path = Iconfig.get("wechat_pay_cert_path");

    // 证书路径，某些微信接口需要API证书授权
    public static String wechat_pay_pkcs8_path = Iconfig.get("wechat_pay_pkcs8_path");

    // 微信扫码支付异步回调地址
    public static String wechat_pc_pay_do_unifiedorder_notify_url = Iconfig.get("wechat_pc_pay_do_unifiedorder_notify_url");

    // 微信扫码支付异步回调地址
    public static String wechat_wap_pay_do_unifiedorder_notify_url = Iconfig.get("wechat_wap_pay_do_unifiedorder_notify_url");

    // 微信退款异步通知地址
    public static String wechat_pay_do_refundorder_notify_url = Iconfig.get("wechat_pay_do_refundorder_notify_url");

    // 支付方式，此处固定位扫码支付 NATIVE
    public static String wechat_pay_trade_type_pc = Iconfig.get("wechat_pay_trade_type_pc");

    // 支付方式，此处固定位扫码支付 NATIVE
    public static String wechat_pay_trade_type_wap = Iconfig.get("wechat_pay_trade_type_wap");

}
