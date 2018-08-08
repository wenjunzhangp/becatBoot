package com.baozi.service.impl.quartz;

import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.baozi.annotation.Operator;
import com.baozi.dao.quartz.QuartzSchedulejobMapper;
import com.baozi.entity.quartz.QuartzSchedulejob;
import com.baozi.enums.StatusType;
import com.baozi.exception.BecatBootException;
import com.baozi.factory.quartz.SchedulerFactory;
import com.baozi.service.quartz.IQuartzSchedulejobService;
import com.baozi.util.BeanUtil;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.quartz.Scheduler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.lang.reflect.Method;
import java.util.List;
import java.util.Map;

/**
 * 服务具体实现类
 * @author wenjunzhangp
 * @since 2018-08-01
 */
@Service
public class QuartzSchedulejobServiceImpl extends ServiceImpl<QuartzSchedulejobMapper, QuartzSchedulejob> implements IQuartzSchedulejobService {

    @Autowired
    private Scheduler scheduler;

    @Autowired(required = false)
    private QuartzSchedulejobMapper quartzSchedulejobMapper;

    @Transactional(readOnly = false, propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
    @Operator(name="添加定时器", story = "添加定时器")
    @Override
    public boolean insert(QuartzSchedulejob scheduleJob) {
        //验证是否通过验证
        verifySchedule(scheduleJob);
        //验证执行方法是否正确
        SchedulerFactory.verifyTrigger(scheduleJob);
        scheduleJob.setStatus(StatusType.CONGEAL.getValue());
        baseMapper.insert(scheduleJob);
        return true;
    }

    @Override
    @Transactional(readOnly = false, propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
    @Operator(story = "修改定时器信息")
    public void update(QuartzSchedulejob scheduleJob) {
        //执行方法验证是否通过验证
        verifySchedule(scheduleJob);
        //验证表达式是否正确
        SchedulerFactory.verifyTrigger(scheduleJob);
        QuartzSchedulejob quartzSchedulejob = baseMapper.selectById(scheduleJob.getScheduleJobId());
        if(quartzSchedulejob == null){
            throw new BecatBootException("修改错误,没有这条定时器");
        }
       baseMapper.updateById(scheduleJob);
    }

    @Override
    public int delete(Integer scheduleJobId) {
        QuartzSchedulejob quartzSchedulejob = baseMapper.selectById(scheduleJobId);
        if(quartzSchedulejob == null){
            throw new BecatBootException("删除错误,没有这条定时器");
        }
        //删除定时器
        SchedulerFactory.del(quartzSchedulejob, scheduler);
        return  baseMapper.deleteById(scheduleJobId);
    }

    @Override
    @Operator(story = "停止定时器")
    public void congeal(Integer scheduleJobId) {
        QuartzSchedulejob quartzSchedulejob = baseMapper.selectById(scheduleJobId);
        if(quartzSchedulejob == null){
            throw new BecatBootException("停止错误,没有这条定时器");
        }
        if(quartzSchedulejob.getQuartzSchedulejobGroup().getStatus()==StatusType.CONGEAL.getValue()){
            throw new BecatBootException("停止失败,请对所在定时器类型进行启动操作.");
        }
        //执行方法验证是否通过验证
        verifySchedule(quartzSchedulejob);
        //在队列中停止
        SchedulerFactory.stop(quartzSchedulejob, scheduler);
        quartzSchedulejob.setStatus(StatusType.CONGEAL.getValue());
        baseMapper.updateById(quartzSchedulejob);
    }

    @Override
    @Transactional(readOnly = false, propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
    @Operator(story = "启动定时器")
    public void thaw(Integer scheduleJobId) {
        QuartzSchedulejob quartzSchedulejob = baseMapper.selectById(scheduleJobId);
        if(quartzSchedulejob == null){
            throw new BecatBootException("启动错误,没有这条定时器");
        }
        //执行方法验证是否通过验证
        verifySchedule(quartzSchedulejob);
        //验证表达式是否正确
        SchedulerFactory.verifyTrigger(quartzSchedulejob);
        if(quartzSchedulejob.getQuartzSchedulejobGroup().getStatus()==StatusType.CONGEAL.getValue()){
            throw new BecatBootException("启动失败,请对所在定时器类型进行启动操作.");
        }
        //添加到执行队列中
        SchedulerFactory.add(quartzSchedulejob,true, scheduler);
        quartzSchedulejob.setStatus(StatusType.NORMAL.getValue());
        baseMapper.updateById(quartzSchedulejob);
    }

    @Override
    @Transactional(readOnly = false, propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
    @Operator(story = "重启定时器")
    public void restart(Integer scheduleJobId) {
        QuartzSchedulejob quartzSchedulejob = baseMapper.selectById(scheduleJobId);
        if(quartzSchedulejob == null){
            throw new BecatBootException("重启错误,没有这条定时器");
        }
        if(quartzSchedulejob.getQuartzSchedulejobGroup().getStatus()==StatusType.CONGEAL.getValue()){
            throw new BecatBootException("重启失败,请对所在定时器类型进行启动操作.");
        }
        //执行方法验证是否通过验证
        verifySchedule(quartzSchedulejob);
        //验证表达式是否正确
        SchedulerFactory.verifyTrigger(quartzSchedulejob);
        //重启定时器
        SchedulerFactory.reStart(quartzSchedulejob, scheduler);
        //修改为运行中
        quartzSchedulejob.setStatus(StatusType.NORMAL.getValue());
        baseMapper.updateById(quartzSchedulejob);
    }

    @Override
    @Transactional(readOnly = false, propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
    @Operator(story = "同步定时器")
    public void async() {
        List<QuartzSchedulejob> remoteList =  baseMapper.selectQuartzSchedulejobAll();

        for(QuartzSchedulejob scheduleJob : remoteList){
            //执行方法验证是否通过验证
            verifySchedule(scheduleJob);
            //验证表达式是否正确
            SchedulerFactory.verifyTrigger(scheduleJob);
            if(scheduleJob.getStatus()==StatusType.CONGEAL.getValue()){
                //添加到执行队列中
                SchedulerFactory.add(scheduleJob,false, scheduler);
            }else if(scheduleJob.getStatus()==StatusType.NORMAL.getValue()){
                //添加到执行队列中
                SchedulerFactory.add(scheduleJob,true, scheduler);
            }
        }
    }

    public void verifySchedule(QuartzSchedulejob scheduleJob){
        Class<?> class1 = BeanUtil.classExists(scheduleJob.getScheduleJobClass());
        if(class1!=null){
            Method method = null;
            if(null!=(method=BeanUtil.methodExists(class1,scheduleJob.getScheduleJobMethod()))){
                if(!BeanUtil.parameterTypesExists(method)){
                    throw new BecatBootException("执行方法中不能存有任何参数.");
                }
            }else{
                throw new BecatBootException("执行方法不存在此调用类中.");
            }
        }else{
            throw new BecatBootException("调用类不存在此系统中.");
        }
    }

    @Override
    public PageInfo<QuartzSchedulejob> selectQuartzSchedulejobList(Map<String,Object> paramMap) {
        PageHelper.startPage(Integer.valueOf(paramMap.get("page").toString()),Integer.valueOf(paramMap.get("limit").toString()),true);
        List<QuartzSchedulejob> dataList = quartzSchedulejobMapper.selectQuartzSchedulejobList();
        return new PageInfo<QuartzSchedulejob>(dataList);
    }

}
