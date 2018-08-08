package com.baozi.util;

import sun.misc.BASE64Encoder;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Random;

/**
 * 验证用户真实性token工具类
 * @author wenjunzhangp
 * @since 2018-08-01
 */
public class TokenUtil {

    /**
     * 单例设计模式（保证类的对象在内存中只有一个）1、把类的构造函数私有2、自己创建一个类的对象3、对外提供一个公共的方法，返回类的对象
     */
    private TokenUtil() {}

    private static final TokenUtil INSTANCE = new TokenUtil();

    /**
     * 返回类的对象
     * @return
     */
    public static TokenUtil getInstance() {
        return INSTANCE;
    }

    /**
     * 生成Token Token：Nv6RRuGEVvmGjB+jimI/gw==
     * @return
     */
    public String makeToken() {
        String token = (System.currentTimeMillis() + new Random().nextInt(999999999)) + "";
        // 数据指纹 128位长 16个字节 md5
        try {
            MessageDigest md = MessageDigest.getInstance("md5");
            byte[] md5 = md.digest(token.getBytes());
            // base64编码--任意二进制编码明文字符
            BASE64Encoder encoder = new BASE64Encoder();
            String tokenId  = encoder.encode(md5);
            return  tokenId.replace("+", "H");
        } catch (NoSuchAlgorithmException e) {  
            throw new RuntimeException(e);  
        }  
    }

    public static void main(String[] args) {
       String tokenId=  TokenUtil.getInstance().makeToken();
        System.out.println(tokenId);
    }
} 