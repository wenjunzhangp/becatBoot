package com.baozi.util;

import com.baozi.constants.Constants;
import org.apache.commons.lang.StringUtils;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * [ 日期工具类 ]
 * @author wenjunzhangp
 * @since 2018-08-01
 */
public class DateUtil {

	public static final String FULL_TIME_FORMAT = "yyyy-MM-dd HH:mm:ss";

	/**
	 * 按照yyyy-MM-dd HH:mm:ss的格式，日期转字符串
	 *
	 * @param date
	 * @return yyyy-MM-dd HH:mm:ss
	 */
	public static String date2Str(Date date) {
		return date2Str(date, FULL_TIME_FORMAT);
	}

	/**
	 * 按照参数format的格式，日期转字符串
	 *
	 * @param date
	 * @param format
	 * @return
	 */
	public static String date2Str(Date date, String format) {
		if (date != null) {
			SimpleDateFormat sdf = new SimpleDateFormat(format);
			return sdf.format(date);
		} else {
			return "";
		}
	}

	public static Date str2Date(String str) {
		return str2Date(str, "yyyy-MM-dd");
	}

	public static Date str2Date(String str, String format) {
		if (StringUtils.isNotBlank(str)) {
			SimpleDateFormat sdf = new SimpleDateFormat(format);
			Date parseDate;
			try {
				parseDate = sdf.parse(str);
			} catch (Exception e) {
				parseDate = new Date();
			}
			return parseDate;
		}
		return new Date();
	}

	/**
	 * 根据用户生日计算年龄
	 */
	public static int getAgeByBirthday(Date birthday) {
		if (birthday == null) {
			return 0;
		}
		Calendar cal = Calendar.getInstance();
		if (cal.before(birthday)) {
			return 0;
		}

		int yearNow = cal.get(Calendar.YEAR);
		int monthNow = cal.get(Calendar.MONTH) + 1;
		int dayOfMonthNow = cal.get(Calendar.DAY_OF_MONTH);
		cal.setTime(birthday);
		int yearBirth = cal.get(Calendar.YEAR);
		int monthBirth = cal.get(Calendar.MONTH) + 1;
		int dayOfMonthBirth = cal.get(Calendar.DAY_OF_MONTH);
		int age = yearNow - yearBirth;

		if (monthNow <= monthBirth) {
			if (monthNow == monthBirth) {
				if (dayOfMonthNow < dayOfMonthBirth) {
					age--;
				}
			} else {
				age--;
			}
		}
		return age;
	}

	public static long nowSeconds() {
		return (long) (System.currentTimeMillis() / 1000);
	}

	/**
	 * 获得当前日期的前一天
	 *
	 * @param date
	 * @return
	 */
	public static Date getSpecifiedDayBefore(Date date) {
		if (date == null) {
			return null;
		}
		Calendar cal = Calendar.getInstance();
		cal.setTime(date);
		int day = cal.get(Calendar.DATE);
		cal.set(Calendar.DATE, day - 1);
		return cal.getTime();
	}

	/**
	 * 获取时间年月日时00,30
	 *
	 * @return 201609210900,201609210930
	 */
	public static Long getDateMinute() {
		SimpleDateFormat sdf = null;
		Calendar c = Calendar.getInstance();
		int minute = c.get(Calendar.MINUTE);
		if (minute >= Constants.DATE_HALF) {
			sdf = new SimpleDateFormat("yyyyMMddHH30");
		} else {
			sdf = new SimpleDateFormat("yyyyMMddHH00");
		}
		String ss = sdf.format(c.getTime());
		return Long.valueOf(ss);
	}

	/**
	 * 获取时间年月日时00,30
	 * 
	 * @param date
	 * @return 201609210900,201609210930
	 */
	public static Long getDateMinute(Date date) {
		SimpleDateFormat sdf = null;
		Calendar c = Calendar.getInstance();
		int minute = c.get(Calendar.MINUTE);
		if (minute >= Constants.DATE_HALF) {
			sdf = new SimpleDateFormat("yyyyMMddHH30");
		} else {
			sdf = new SimpleDateFormat("yyyyMMddHH00");
		}
		String ss = sdf.format(date);
		return Long.valueOf(ss);
	}
}
