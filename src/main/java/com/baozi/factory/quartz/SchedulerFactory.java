package com.baozi.factory.quartz;

import com.baozi.entity.quartz.QuartzSchedulejob;
import com.baozi.entity.quartz.QuartzSchedulejobGroup;
import com.baozi.enums.StatusType;
import com.baozi.exception.BecatBootException;
import org.quartz.CronScheduleBuilder;
import org.quartz.CronTrigger;
import org.quartz.JobBuilder;
import org.quartz.JobDetail;
import org.quartz.JobKey;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.quartz.Trigger;
import org.quartz.TriggerBuilder;
import org.quartz.TriggerKey;
import org.quartz.impl.matchers.GroupMatcher;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

/**
 * 定时器工厂类
 * @author wenjunzhangp
 * @since 2018-08-01
 */
@Component
public class SchedulerFactory {

	private static  Logger logger =  LoggerFactory.getLogger(SchedulerFactory.class);

	public static CronScheduleBuilder verifyTrigger(QuartzSchedulejob scheduleJob){
		CronScheduleBuilder scheduleBuilder= null;
		try {
			scheduleBuilder = CronScheduleBuilder.cronSchedule(scheduleJob.getScheduleJobCronExpression());
		} catch (Exception e) {
			throw new BecatBootException("表达式不正确,请重新输入.");
		}
		return scheduleBuilder;
	}

	public static List<QuartzSchedulejob> list(Scheduler scheduler) {
		List<QuartzSchedulejob> jobList = new ArrayList<QuartzSchedulejob>();
		try {
			GroupMatcher<JobKey> matcher = GroupMatcher.anyJobGroup();
			Set<JobKey> jobKeys = scheduler.getJobKeys(matcher);
			for (JobKey jobKey : jobKeys) {
				List<? extends Trigger> triggers = scheduler.getTriggersOfJob(jobKey);
				for (Trigger trigger : triggers) {
					QuartzSchedulejob scheduleJob = new QuartzSchedulejob();
					scheduleJob.setScheduleJobId(Integer.parseInt(jobKey.getName()));
					scheduleJob.setQuartzSchedulejobGroup(new QuartzSchedulejobGroup(jobKey.getGroup()));
					scheduleJob.setScheduleJobDescription(String.valueOf(trigger.getKey()));
					Trigger.TriggerState triggerState = scheduler.getTriggerState(trigger.getKey());
					scheduleJob.setStatus("NORMAL".equals(triggerState.name())? StatusType.NORMAL.getValue():StatusType.CONGEAL.getValue());
					if (trigger instanceof CronTrigger) {
						CronTrigger cronTrigger = (CronTrigger) trigger;
						String cronExpression = cronTrigger.getCronExpression();
						scheduleJob.setScheduleJobCronExpression(cronExpression);
					}
					jobList.add(scheduleJob);
				}
			}
		} catch (Exception e) {
			logger.error("error",e);
			throw new BecatBootException("获取失败");
		}
		return jobList;
	}

	public synchronized static void add(QuartzSchedulejob scheduleJob, boolean start, Scheduler scheduler){
		//组合名称（定时器名称+分组名称）
		TriggerKey triggerKey = TriggerKey.triggerKey(String.valueOf(scheduleJob.getScheduleJobId()), String.valueOf(scheduleJob.getQuartzSchedulejobGroup().getScheduleJobGroupId()));
		// 获取trigger，即在spring配置文件中定义的 bean id="myTrigger"
		CronTrigger trigger=null;
		try {
			trigger = (CronTrigger) scheduler.getTrigger(triggerKey);
		} catch (SchedulerException e) {
			logger.error("error",e);
			throw new BecatBootException("表达式不正确,请重新输入.");
		}
		// 不存在，创建一个
		if (null == trigger) {
			JobDetail jobDetail = JobBuilder.newJob(QuartzJobFactory.class)
					.withIdentity(String.valueOf(scheduleJob.getScheduleJobId()), String.valueOf(scheduleJob.getQuartzSchedulejobGroup().getScheduleJobGroupId())).build();
			jobDetail.getJobDataMap().put("scheduleJob", scheduleJob);

			// 表达式调度构建器
			CronScheduleBuilder scheduleBuilder= verifyTrigger(scheduleJob);

			// 按新的cronExpression表达式构建一个新的trigger
			trigger = TriggerBuilder.newTrigger().withIdentity(String.valueOf(scheduleJob.getScheduleJobId()), scheduleJob.getQuartzSchedulejobGroup().getScheduleJobGroupId().toString())
					.withSchedule(scheduleBuilder).build();

			try {
				scheduler.scheduleJob(jobDetail, trigger);
				if(!start){
					stop(scheduleJob, scheduler);
				}
			} catch (SchedulerException e) {
				logger.error("error",e);
				throw new BecatBootException(e.getMessage());
			}
		} else {
			// Trigger已存在，那么更新相应的定时设置
			// 表达式调度构建器
			CronScheduleBuilder scheduleBuilder = verifyTrigger(scheduleJob);

			// 按新的cronExpression表达式重新构建trigger
			trigger = trigger.getTriggerBuilder().withIdentity(triggerKey).withSchedule(scheduleBuilder).build();
			// 按新的trigger重新设置job执行
			try {
				scheduler.rescheduleJob(triggerKey, trigger);
				if(!start){
					stop(scheduleJob, scheduler);
				}
			} catch (SchedulerException e) {
				logger.error("error",e);
				throw new BecatBootException("trigger执行失败.");
			}
		}
	}

	public static void stop(QuartzSchedulejob scheduleJob, Scheduler scheduler) {
		try {
			JobKey jobKey = JobKey.jobKey(String.valueOf(scheduleJob.getScheduleJobId()), String.valueOf(scheduleJob.getQuartzSchedulejobGroup().getScheduleJobGroupId()));
			scheduler.pauseJob(jobKey);
		} catch (Exception e) {
			logger.error("error",e);
			throw new BecatBootException("定时器停止失败.");
		}
	}

	public static void reStart(QuartzSchedulejob scheduleJob, Scheduler scheduler){
		try {
			JobKey jobKey = JobKey.jobKey(String.valueOf(scheduleJob.getScheduleJobId()), String.valueOf(scheduleJob.getQuartzSchedulejobGroup().getScheduleJobGroupId()));
			scheduler.resumeJob(jobKey);
		} catch (Exception e) {
			logger.error("error",e);
			throw new BecatBootException("定时器重启失败.");
		}
	}

	public static void del(QuartzSchedulejob scheduleJob, Scheduler scheduler) {
		try {
			JobKey jobKey = JobKey.jobKey(String.valueOf(scheduleJob.getScheduleJobId()), String.valueOf(scheduleJob.getQuartzSchedulejobGroup().getScheduleJobGroupId()));
			scheduler.deleteJob(jobKey);
		} catch (Exception e) {
			logger.error("error",e);
			throw new BecatBootException("定时器删除失败.");
		}
	}
}
