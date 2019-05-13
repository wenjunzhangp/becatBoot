package com.baozi.config;

/**
 * Copyright:   张文君
 * 支付宝支付基础配置类
 *
 * @author: zhangwenjun
 * @version: V1.0
 * @Date: 2018-11-02 11:07:23
 */
public class AlipayConfig {

    // 应用ID,您的APPID，收款账号既是您的APPID对应支付宝账号
    public static String app_id = Iconfig.get("alipay_appid");

    // 商户私钥，您的PKCS8格式RSA2私钥
    public static String merchant_private_key = Iconfig.get("alipay_merchant_private_key");

    // 支付宝公钥,查看地址：https://openhome.alipay.com/platform/keyManage.htm 对应APPID下的支付宝公钥。
    public static String alipay_public_key = Iconfig.get("alipay_alipay_public_key");

    // 服务器异步通知页面路径  需http://格式的完整路径，不能加?id=123这类自定义参数，必须外网可以正常访问
    public static String alipay_pc_notify_url = Iconfig.get("alipay_pc_notify_url");

    // 页面跳转同步通知页面路径 需http://格式的完整路径，不能加?id=123这类自定义参数，必须外网可以正常访问
    public static String alipay_pc_return_url = Iconfig.get("alipay_pc_return_url");

    // 服务器异步通知页面路径  需http://格式的完整路径，不能加?id=123这类自定义参数，必须外网可以正常访问
    public static String alipay_wap_notify_url = Iconfig.get("alipay_wap_notify_url");

    // 页面跳转同步通知页面路径 需http://格式的完整路径，不能加?id=123这类自定义参数，必须外网可以正常访问
    public static String alipay_wap_return_url = Iconfig.get("alipay_wap_return_url");

    // 签名方式
    public static String sign_type = Iconfig.get("alipay_sign_type");

    // 字符编码格式
    public static String charset = Iconfig.get("alipay_charset");

    // 支付宝网关
    public static String gatewayUrl = Iconfig.get("alipay_gatewayUrl");

}

