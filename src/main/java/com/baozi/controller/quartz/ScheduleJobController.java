package com.baozi.controller.quartz;

import com.baozi.controller.base.ResponseBase;
import com.baozi.entity.quartz.QuartzSchedulejob;
import com.baozi.enums.StatusType;
import com.baozi.exception.BecatBootException;
import com.baozi.service.quartz.IQuartzSchedulejobGroupService;
import com.baozi.service.quartz.IQuartzSchedulejobService;
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
import java.util.HashMap;
import java.util.Map;

/**
 * 具体执行任务controller
 * @author wenjunzhangp
 * @since 2018-08-01
 */
@RestController
@RequestMapping(value="quartz")
@Api(tags = "定时任务controller", description = "定时任务controller")
public class ScheduleJobController {

    @Autowired
    IQuartzSchedulejobService schedulejobService;

    @Autowired
    IQuartzSchedulejobGroupService schedulejobGroupService;

    /**
     * 新增一个任务
     * @param schedulejob
     * @return
     */
    @PostMapping("add/quartz/schedulejob")
    @ApiOperation(value="新增定时任务", notes="新增定时任务")
    @ApiImplicitParam(name = "schedulejob", value = "定时任务实体", required = true, dataType = "QuartzSchedulejob")
    public ResponseBase addScheduleJob(QuartzSchedulejob schedulejob){
        schedulejob.setCreateTime(new Date());
        schedulejob.setStatus(StatusType.CONGEAL.getValue());
        boolean result = schedulejobService.insert(schedulejob);
        if (result) {
            return ResponseBase.success("新增成功");
        }else{
            return ResponseBase.error("新增失败");
        }
    }

    /**
     * 删除一个任务
     * @param scheduleJobId
     * @return
     */
    @PostMapping("del/quartz/schedulejob")
    @ApiOperation(value="删除定时任务", notes="删除定时任务")
    @ApiImplicitParam(name = "scheduleJobId", value = "定时任务标志ID", required = true, dataType = "Integer")
    public ResponseBase delAdminRole(Integer scheduleJobId){
        try {
            schedulejobService.delete(scheduleJobId);
            return ResponseBase.success("删除成功");
        } catch ( BecatBootException e ) {
            LoggerUtils.logError("删除【"+scheduleJobId+"】任务出现异常",e);
            return ResponseBase.error(e.getMessage());
        }
    }

    /**
     * 编辑一个任务
     * @param schedulejob
     * @return
     */
    @PostMapping("edit/quartz/schedulejob")
    @ApiOperation(value="编辑定时任务", notes="编辑定时任务")
    @ApiImplicitParam(name = "schedulejob", value = "定时任务实体", required = true, dataType = "QuartzSchedulejob")
    public ResponseBase editScheduleJob(QuartzSchedulejob schedulejob ){
        try {
            schedulejobService.update(schedulejob);
            return ResponseBase.success("编辑成功");
        } catch ( BecatBootException e ) {
            LoggerUtils.logError("编辑【"+schedulejob.getScheduleJobId()+"】任务出现异常",e);
            return ResponseBase.error(e.getMessage());
        }
    }

    /**
     * 启动某一个任务
     * @param scheduleJobId
     * @return
     */
    @PostMapping("start/quartz/schedulejob")
    @ApiOperation(value="启动定时任务", notes="启动定时任务")
    @ApiImplicitParam(name = "scheduleJobId", value = "定时任务标志ID", required = true, dataType = "Integer")
    public ResponseBase startScheduleJob(Integer scheduleJobId){
        try {
            schedulejobService.thaw(scheduleJobId);
            return ResponseBase.success("启动成功");
        } catch ( BecatBootException e ) {
            LoggerUtils.logError("启动【"+scheduleJobId+"】任务出现异常",e);
            return ResponseBase.error(e.getMessage());
        }
    }

    /**
     * 停止任务
     * @param scheduleJobId
     * @return
     */
    @PostMapping("stop/quartz/schedulejob")
    @ApiOperation(value="停止定时任务", notes="停止定时任务")
    @ApiImplicitParam(name = "scheduleJobId", value = "定时任务标志ID", required = true, dataType = "Integer")
    public ResponseBase stopScheduleJob(Integer scheduleJobId){
        try {
            schedulejobService.congeal(scheduleJobId);
            return ResponseBase.success("停止成功");
        } catch ( BecatBootException e ) {
            LoggerUtils.logError("停止【"+scheduleJobId+"】任务出现异常",e);
            return ResponseBase.error(e.getMessage());
        }
    }

    /**
     * 重启任务
     * @param scheduleJobId
     * @return
     */
    @PostMapping("reboot/quartz/schedulejob")
    @ApiOperation(value="重启定时任务", notes="重启定时任务")
    @ApiImplicitParam(name = "scheduleJobId", value = "定时任务标志ID", required = true, dataType = "Integer")
    public ResponseBase rebootScheduleJob(Integer scheduleJobId){
        try {
            schedulejobService.restart(scheduleJobId);
            return ResponseBase.success("重启成功");
        } catch ( BecatBootException e ) {
            LoggerUtils.logError("重启【"+scheduleJobId+"】任务出现异常",e);
            return ResponseBase.error(e.getMessage());
        }
    }

    /**
     * 同步所有任务
     * @return
     */
    @PostMapping("async/quartz/schedulejob")
    @ApiOperation(value="同步所有任务", notes="同步所有任务")
    public ResponseBase asyncScheduleJob(){
        try {
            schedulejobService.async();
            return ResponseBase.success("同步成功");
        } catch ( BecatBootException e ) {
            LoggerUtils.logError("同步任务出现异常",e);
            return ResponseBase.error(e.getMessage());
        }
    }

    /**
     * 任务分页列表查询
     * @return
     */
    @RequestMapping("query/quartz/schedulejobList")
    @ApiOperation(value="任务分页查询", notes="任务分页查询")
    public Map<String,Object> queryScheduleJob(HttpServletRequest request){
        try {
            Map<String,Object> paramMap = WebUtil.genRequestMapSingle(request);
            return ResponseBase.setResultMapOkByPage(schedulejobService.selectQuartzSchedulejobList(paramMap));
        } catch ( Exception e ) {
            LoggerUtils.logError("任务分页查询出现异常",e);
            return ResponseBase.setResultMapError(e);
        }
    }
}
