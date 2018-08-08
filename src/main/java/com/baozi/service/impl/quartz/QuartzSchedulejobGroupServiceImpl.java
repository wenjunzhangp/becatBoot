package com.baozi.service.impl.quartz;

import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.baozi.annotation.Operator;
import com.baozi.dao.quartz.QuartzSchedulejobGroupMapper;
import com.baozi.dao.quartz.QuartzSchedulejobMapper;
import com.baozi.entity.quartz.QuartzSchedulejob;
import com.baozi.entity.quartz.QuartzSchedulejobGroup;
import com.baozi.enums.StatusType;
import com.baozi.exception.BecatBootException;
import com.baozi.factory.quartz.SchedulerFactory;
import com.baozi.service.quartz.IQuartzSchedulejobGroupService;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.quartz.Scheduler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;

/**
 * 系统常量定义类
 * @author wenjunzhangp
 * @since 2018-08-01
 */
@Service
public class QuartzSchedulejobGroupServiceImpl extends ServiceImpl<QuartzSchedulejobGroupMapper, QuartzSchedulejobGroup> implements IQuartzSchedulejobGroupService {

    @Autowired(required = false)
    private QuartzSchedulejobMapper quartzSchedulejobMapper;

    @Autowired(required = false)
    private QuartzSchedulejobGroupMapper quartzSchedulejobGroupMapper;

    @Autowired
    private Scheduler scheduler;

    @Override
    @Transactional(readOnly = false, propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
    @Operator(story="停止定时器分组")
    public void congeal(Integer scheduleJobGroupId) {
        QuartzSchedulejobGroup quartzSchedulejobGroup = baseMapper.selectById(scheduleJobGroupId);
        QuartzSchedulejob scheduleJob = new QuartzSchedulejob();
        scheduleJob.setQuartzSchedulejobGroup(quartzSchedulejobGroup);
        scheduleJobGroupId = quartzSchedulejobGroup.getScheduleJobGroupId();
        List<QuartzSchedulejob> list = quartzSchedulejobMapper.selectQuartzSchedulejobGroupId(scheduleJobGroupId);
        //把定时器分组下的所有定时器都停止掉
        for(QuartzSchedulejob scheduleJob2 : list){
            scheduleJob2.setQuartzSchedulejobGroup(quartzSchedulejobGroup);
            SchedulerFactory.stop(scheduleJob2, scheduler);
            //1冻结 0 启动 2 删除
            scheduleJob2.setStatus(StatusType.CONGEAL.getValue());
            quartzSchedulejobMapper.updateById(scheduleJob2);
        }
        quartzSchedulejobGroup.setStatus(StatusType.CONGEAL.getValue());
        baseMapper.updateById(quartzSchedulejobGroup);
    }

    @Override
    @Transactional(readOnly = false, propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
    @Operator(story="启动定时器分组")
    public void start(Integer scheduleJobGroupId) {
        QuartzSchedulejobGroup quartzSchedulejobGroup = baseMapper.selectById(scheduleJobGroupId);
        QuartzSchedulejob scheduleJob = new QuartzSchedulejob();
        scheduleJob.setQuartzSchedulejobGroup(quartzSchedulejobGroup);
        List<QuartzSchedulejob> list = (List<QuartzSchedulejob>) quartzSchedulejobMapper.selectQuartzSchedulejobGroupId(quartzSchedulejobGroup.getScheduleJobGroupId());
        //把定时器分组下的所有定时器都启动
        for(QuartzSchedulejob scheduleJob2 : list){
            //添加到执行队列中
            SchedulerFactory.add(scheduleJob2,true, scheduler);
            //1冻结 0 启动 2 删除
            scheduleJob2.setStatus(StatusType.NORMAL.getValue());
            //启动
            quartzSchedulejobMapper.updateById(scheduleJob2);
        }
        //启动
        quartzSchedulejobGroup.setStatus(StatusType.NORMAL.getValue());
        baseMapper.updateById(quartzSchedulejobGroup);
    }

    @Override
    public boolean deleteById(Integer scheduleJobGroupId) {
        List<QuartzSchedulejob> list = (List<QuartzSchedulejob>) quartzSchedulejobMapper.selectQuartzSchedulejobGroupId(scheduleJobGroupId);
        if(list != null && list.size() > 0){
            throw new BecatBootException("请先删除该分组下面的任务.");
        }
        quartzSchedulejobGroupMapper.deleteById(scheduleJobGroupId);
        return true;
    }

    @Override
    public PageInfo<QuartzSchedulejobGroup> selectQuartzSchedulejobGroupList(Map<String, Object> paramMap) {
        PageHelper.startPage(Integer.valueOf(paramMap.get("page").toString()),Integer.valueOf(paramMap.get("limit").toString()),true);
        List<QuartzSchedulejobGroup> dataList = quartzSchedulejobGroupMapper.selectQuartzSchedulejobGroupList();
        return new PageInfo<QuartzSchedulejobGroup>(dataList);
    }
}
