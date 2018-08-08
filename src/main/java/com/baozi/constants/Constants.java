package com.baozi.constants;

/**
 * 系统常量定义类
 * @author wenjunzhangp
 * @since 2018-08-01
 */
@SuppressWarnings("ALL")
public interface Constants {

	/**
	 * 错误返回码
	 */
	int SUCCESS = 0;
	/**
	 * 默认错误
	 */
	int ERROE = 1;
	/**
	 * 参数错误
	 */
	int PARAMETER_VALIDATION_ERROR = 2;
	/**
	 * 逻辑业务处理异常
	 */
	int BUSINESS_ERROR = 3;
	/**
	 * 系统错误
	 */
	int SYSTEM_ERROR = 4;

	/**
	 * CACHE_TOKNID
	 */
	String CACHE_TOKNID = "becatBoot:tokenId_";
	/**
	 * CACHE_USER_ID
	 */
	String CACHE_USER_ID = "becatBoot:userId_";
	/**
	 * token有效期
	 */
	int CACHE_TOKENID_TIME = 30;

	/**
	 * 时间判断 30
	 */
	int DATE_HALF = 30;
}
