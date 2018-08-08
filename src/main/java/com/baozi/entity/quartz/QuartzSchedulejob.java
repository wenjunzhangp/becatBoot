package com.baozi.entity.quartz;

import com.baomidou.mybatisplus.activerecord.Model;
import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.annotations.TableName;
import com.baomidou.mybatisplus.enums.IdType;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;


/**
 * 任务实体类
 * @author wenjunzhangp
 * @since 2018-08-01
 */
@TableName("quartz_schedulejob")
@Data
public class QuartzSchedulejob extends Model<QuartzSchedulejob> {

    private static final long serialVersionUID = 1L;

	/** 任务id */
	@TableId(value = "schedule_job_id", type = IdType.AUTO)
	private Integer scheduleJobId;
	/** 任务名称 */
	@TableField("schedule_job_name")
	private String scheduleJobName;
	/** 关联任务组id */
	@TableField("schedule_job_group_id")
	private Integer scheduleJobGroupId;
	/** 任务分组 */
	@TableField(exist = false)
	private QuartzSchedulejobGroup quartzSchedulejobGroup;
	/** 任务状态 */
	@TableField("status")
	private Integer status;
	/** 任务描述 */
	@TableField("schedule_job_description")
	private String scheduleJobDescription;
	/** 任务创建时间 */
	@JsonFormat(pattern="yyyy-MM-dd HH:mm:ss",timezone = "GMT+8")
	@TableField("create_time")
	private Date createTime;
	/** 任务运行时间表达式 */
	@TableField("schedule_job_cron_expression")
	private String scheduleJobCronExpression;
	/** 执行方法 */
	@TableField("schedule_job_method")
	private String scheduleJobMethod;
	/** 任务具体的类 全路径 */
	@TableField("schedule_job_class")
	private String scheduleJobClass;

	@Override
	protected Serializable pkVal() {
		return this.scheduleJobId;
	}

	public QuartzSchedulejob() {}
}
