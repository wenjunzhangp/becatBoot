package com.baozi.enums;

/**
 * 定时器状态枚举类
 * @author wenjunzhangp
 * @since 2018-08-01
 */
public enum StatusType {

	/**
	 * 运行状态
	 */
	NORMAL(0),
	/**
	 * 停止状态
	 */
	CONGEAL(1),
	/**
	 * 删除状态
	 */
	DELETE(2);

	private int value;

	private StatusType(int value){
		this.value = value;
	}

	public int getValue(){
		return this.value;
	}

	public static StatusType fromTo(int value)
	{
		StatusType[] values = StatusType.values();
		for (StatusType type : values)
		{
			if (type.getValue() == value)
			{
				return type;
			}
		}
		throw new IllegalArgumentException("类型不正确,不能识别为[" + value + "]的类型.");
	}
}
