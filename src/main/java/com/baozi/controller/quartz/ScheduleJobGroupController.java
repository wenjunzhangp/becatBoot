package com.baozi.controller.quartz;

import com.baozi.controller.base.ResponseBase;
import com.baozi.entity.quartz.QuartzSchedulejobGroup;
import com.baozi.enums.StatusType;
import com.baozi.exception.BecatBootException;
import com.baozi.service.quartz.IQuartzSchedulejobGroupService;
import com.baozi.util.LoggerUtils;
import com.baozi.util.WebUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * 任务组controller
 * @author wenjunzhangp
 * @since 2018-08-01
 */
@RestController
@RequestMapping(value="quartz")
@Api(tags = "定时器任务组controller", description = "定时器任务组controller")
public class ScheduleJobGroupController{

	@Autowired
	private IQuartzSchedulejobGroupService schedulejobGroupService;

	/**
	 * 新增一个定时器组 状态默认为停止
	 * @param schedulejobGroup
	 * @return
	 */
	@PostMapping("add/quartz/schedulejobGroup")
	@ApiOperation(value="新增定时器组", notes="新增定时器组")
	@ApiImplicitParam(name = "schedulejobGroup", value = "定时器组实体", required = true, dataType = "QuartzSchedulejobGroup")
	public ResponseBase addScheduleJobGroup(QuartzSchedulejobGroup schedulejobGroup){
		schedulejobGroup.setCreateTime(new Date());
		schedulejobGroup.setStatus(StatusType.CONGEAL.getValue());
		boolean result = schedulejobGroupService.insert(schedulejobGroup);
		if (result) {
			return ResponseBase.success("新增成功");
		}else{
			return ResponseBase.error("新增失败");
		}
	}

	/**
	 * 删除定时器组
	 * @param scheduleJobGroupId
	 * @return
	 */
	@PostMapping("del/quartz/schedulejobGroup")
	@ApiOperation(value="删除定时器组", notes="删除定时器组")
	@ApiImplicitParam(name = "scheduleJobGroupId", value = "定时器组标志ID", required = true, dataType = "Integer")
	public ResponseBase delScheduleJobGroup(Integer scheduleJobGroupId){
		try {
			schedulejobGroupService.deleteById(scheduleJobGroupId);
			return ResponseBase.success("删除成功");
		} catch ( BecatBootException e ){
			LoggerUtils.logError("删除【"+scheduleJobGroupId+"】任务出现异常",e);
			return ResponseBase.error(e.getMessage());
		}
	}

	/**
	 * 编辑定时器组
	 * @param schedulejobGroup
	 * @return
	 */
	@PostMapping("edit/quartz/scheduleJobGroup")
	@ApiOperation(value="编辑定时器组", notes="编辑定时器组")
	@ApiImplicitParam(name = "schedulejobGroup", value = "定时器组实体", required = true, dataType = "QuartzSchedulejobGroup")
	public ResponseBase editScheduleJobGroup(QuartzSchedulejobGroup schedulejobGroup ){
		boolean result = schedulejobGroupService.updateById(schedulejobGroup);
		if(result){
			return ResponseBase.success("编辑成功");
		}else {
			return ResponseBase.error("编辑失败");
		}
	}

	/**
	 * 分页查询定时器组 默认每页5个
	 * @param request
	 * @return
	 */
	@RequestMapping("query/quartz/scheduleJobGroupList")
	@ApiOperation(value="分页查询定时器组 默认每页5个", notes="分页查询定时器组 默认每页5个")
	@ApiImplicitParam(name = "schedulejobGroup", value = "定时器组实体", required = true, dataType = "QuartzSchedulejobGroup")
	public Map<String,Object> queryScheduleJobGroupList(HttpServletRequest request ){
		try {
			Map<String,Object> paramMap = WebUtil.genRequestMapSingle(request);
			return ResponseBase.setResultMapOkByPage(schedulejobGroupService.selectQuartzSchedulejobGroupList(paramMap));
		} catch ( Exception e ) {
			LoggerUtils.logError("分页查询定时器组出现异常",e);
			return ResponseBase.setResultMapError(e);
		}
	}

	/**
	 * 加载下拉框数据
	 * @return
	 */
	@RequestMapping("query/quartz/scheduleJobGroupAll")
	public ResponseBase<?> queryScheduleJobGroupAll(){
		ResponseBase<List<QuartzSchedulejobGroup> > responseMessage = new ResponseBase<>();
		List<QuartzSchedulejobGroup> list = schedulejobGroupService.selectList(null);
		responseMessage.setData(list);
		return responseMessage;
	}

	/**
	 * 启动定时器组
	 * @param scheduleJobGroupId
	 * @return
	 */
	@PostMapping("start/quartz/schedulejobGroup")
	@ApiOperation(value="启动定时器组", notes="启动定时器组")
	@ApiImplicitParam(name = "scheduleJobGroupId", value = "定时器组ID", required = true, dataType = "Integer")
	public ResponseBase startScheduleJobGroup( Integer scheduleJobGroupId){
		try {
			QuartzSchedulejobGroup quartzSchedulejobGroup = schedulejobGroupService.selectById(scheduleJobGroupId);
			quartzSchedulejobGroup.setStatus(StatusType.NORMAL.getValue());
			schedulejobGroupService.updateById(quartzSchedulejobGroup);
			return ResponseBase.success("启动成功");
		} catch ( BecatBootException e ){
			LoggerUtils.logError("启动定时器组【"+scheduleJobGroupId+"】任务出现异常",e);
			return ResponseBase.error("启动定时器组失败出现异常");
		}
	}

	/**
	 * 停止定时器组
	 * @param scheduleJobGroupId
	 * @return
	 */
	@PostMapping("stop/quartz/schedulejobGroup")
	@ApiOperation(value="停止定时器组", notes="停止定时器组")
	@ApiImplicitParam(name = "scheduleJobGroupId", value = "定时器组ID", required = true, dataType = "Integer")
	public ResponseBase stopScheduleJobGroup(Integer scheduleJobGroupId){
		try {
			schedulejobGroupService.congeal(scheduleJobGroupId);
			return ResponseBase.success("停止成功");
		} catch ( BecatBootException e ){
			LoggerUtils.logError("停止定时器组【"+scheduleJobGroupId+"】任务出现异常",e);
			return ResponseBase.error("停止定时器组失败出现异常");
		}
	}
}
