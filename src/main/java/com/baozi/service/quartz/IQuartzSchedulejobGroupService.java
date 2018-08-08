package com.baozi.service.quartz;

import com.baomidou.mybatisplus.service.IService;
import com.baozi.entity.quartz.QuartzSchedulejobGroup;
import com.github.pagehelper.PageInfo;

import java.util.Map;

/**
 * 具体执行任务的逻辑类
 * @author wenjunzhangp
 * @since 2018-08-01
 */
public interface IQuartzSchedulejobGroupService extends IService<QuartzSchedulejobGroup> {

    /**
     * 停止某个分组下面所有的定时器
     * @param scheduleJobGroupId 任务组id
     */
     void congeal(Integer scheduleJobGroupId);

    /**
     * 启动定时器分组
     * @param scheduleJobGroupId 任务组id
     */
    void start(Integer scheduleJobGroupId);

    /**
     * 删除一个任务组，如果该组下面有任务执行，则不能删除
     * @param scheduleJobGroupId 任务组id
     * @return
     */
    boolean deleteById(Integer scheduleJobGroupId);

    /**
     * 分页查询定时器组
     * @param paramMap
     * @return
     */
    PageInfo<QuartzSchedulejobGroup> selectQuartzSchedulejobGroupList(Map<String,Object> paramMap);
}
