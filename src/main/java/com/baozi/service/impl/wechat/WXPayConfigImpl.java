package com.baozi.service.impl.wechat;

import com.baozi.config.WeChatConfig;
import com.github.wxpay.sdk.WXPayConfig;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;

/**
 * 配置接口实现类
 *
 * @author zhangwenjun
 * @date 2017-12-18
 * @time 10:40
 */
public class WXPayConfigImpl implements WXPayConfig {

    private byte[] certData;
    private static WXPayConfigImpl INSTANCE;

    private WXPayConfigImpl() throws Exception {
        String certPath = WeChatConfig.wechat_pay_cert_path;
        File file = new File(certPath);
        InputStream certStream = new FileInputStream(file);
        this.certData = new byte[(int) file.length()];
        certStream.read(this.certData);
        certStream.close();
    }

    public static WXPayConfigImpl getInstance() throws Exception {
        if (INSTANCE == null) {
            synchronized (WXPayConfigImpl.class) {
                if (INSTANCE == null) {
                    INSTANCE = new WXPayConfigImpl();
                }
            }
        }
        return INSTANCE;
    }

    @Override
    public String getAppID() {
        return WeChatConfig.wechat_pay_appid;
    }

    @Override
    public String getMchID() {
        return WeChatConfig.wechat_pay_mchid;
    }

    @Override
    public String getKey() {
        return WeChatConfig.wechat_pay_key;
    }

    @Override
    public InputStream getCertStream() {
        ByteArrayInputStream certBis;
        certBis = new ByteArrayInputStream(this.certData);
        return certBis;
    }

    @Override
    public int getHttpConnectTimeoutMs() {
        return 10000;
    }

    @Override
    public int getHttpReadTimeoutMs() {
        return 30000;
    }
}
