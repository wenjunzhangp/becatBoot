package com.baozi.service.quartz;

import com.baomidou.mybatisplus.service.IService;
import com.baozi.entity.quartz.QuartzSchedulejob;
import com.github.pagehelper.PageInfo;

import java.util.Map;

/**
 * 具体执行任务的逻辑类
 * @author wenjunzhangp
 * @since 2018-08-01
 */
public interface IQuartzSchedulejobService extends IService<QuartzSchedulejob> {

     /**
      * 新增一个执行任务
      * @param scheduleJob
      * @return
      */
     @Override
     boolean insert(QuartzSchedulejob scheduleJob);

     /**
      * 修改任务
      * @param scheduleJob
      */
     void update(QuartzSchedulejob scheduleJob);

     /**
      * 根据主键删除任务
      * @param scheduleJobId
      * @return
      */
     int delete(Integer scheduleJobId);

     /**
      * 停止定时器
      * @param scheduleJobId
      */
     void congeal(Integer scheduleJobId);

     /**
      * 启动定时器
      * @param scheduleJobId
      */
     void thaw(Integer scheduleJobId);

     /**
      * 重启定时器
      * @param scheduleJobId
      */
     void restart(Integer scheduleJobId);

     /**
      * 同步定时器
      */
     void async();

     /**
      * 任务列表分页查询
      * @param paramMap
      * @return
      */
     PageInfo<QuartzSchedulejob> selectQuartzSchedulejobList(Map<String,Object> paramMap);
}
