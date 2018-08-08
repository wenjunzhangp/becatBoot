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
 * 任务组实体类
 * @author wenjunzhangp
 * @since 2018-08-01
 */
@TableName("quartz_schedulejob_group")
@Data
public class QuartzSchedulejobGroup extends Model<QuartzSchedulejobGroup> {

    private static final long serialVersionUID = 1L;

	/** 任务组id */
	@TableId(value = "schedule_job_groupId", type = IdType.AUTO)
	private Integer scheduleJobGroupId;
	/** 任务组名称 */
	@TableField("schedule_job_group_name")
	private String scheduleJobGroupName;
	/** 任务组描述 */
	@TableField("schedule_job_group_description")
	private String scheduleJobGroupDescription;
	/** 任务组状态 */
	@TableField("status")
	private Integer status;
	/** 任务创建时间 */
	@JsonFormat(pattern="yyyy-MM-dd HH:mm:ss",timezone = "GMT+8")
	@TableField("create_time")
	private Date createTime;

	@Override
	protected Serializable pkVal() {
		return this.scheduleJobGroupId;
	}

	public QuartzSchedulejobGroup(String scheduleJobGroupName){
		this.scheduleJobGroupName = scheduleJobGroupName;
	}

	public QuartzSchedulejobGroup() {}
}
