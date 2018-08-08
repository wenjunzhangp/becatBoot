package com.baozi.vo;

import com.baozi.entity.quartz.QuartzSchedulejobGroup;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.util.Date;


/**
 * 任务实体类
 * @author wenjunzhangp
 * @since 2018-08-01
 */
@Data
public class QuartzSchedulejobVo {

	/** 任务id */
	private Integer scheduleJobId;
	/** 任务名称 */
	private String scheduleJobName;
	/** 关联任务组id */
	private Integer scheduleJobGroupId;
	/** 任务分组 */
	private QuartzSchedulejobGroup quartzSchedulejobGroup;
	/** 任务状态 */
	private Integer status;
	/** 任务描述 */
	private String scheduleJobDescription;
	/** 任务创建时间 */
	@JsonFormat(pattern="yyyy-MM-dd HH:mm:ss",timezone = "GMT+8")
	private Date createTime;
	/** 任务运行时间表达式 */
	private String scheduleJobCronExpression;
	/** 执行方法 */
	private String scheduleJobMethod;
	/** 任务具体的类 全路径 */
	private String scheduleJobClass;

}
