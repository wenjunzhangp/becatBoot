package com.baozi.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.util.Date;


/**
 * 任务组实体类
 * @author wenjunzhangp
 * @since 2018-08-01
 */
@Data
public class QuartzSchedulejobGroupVo {

	/** 任务组id */
	private Integer scheduleJobGroupId;
	/** 任务组名称 */
	private String scheduleJobGroupName;
	/** 任务组描述 */
	private String scheduleJobGroupDescription;
	/** 任务组状态 */
	private Integer status;
	/** 任务创建时间 */
	@JsonFormat(pattern="yyyy-MM-dd HH:mm:ss",timezone = "GMT+8")
	private Date createTime;

}
