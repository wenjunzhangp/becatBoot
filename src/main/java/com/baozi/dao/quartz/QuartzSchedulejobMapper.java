package com.baozi.dao.quartz;

import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.baozi.entity.quartz.QuartzSchedulejob;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * mapper接口
 * @author wenjunzhangp
 * @since 2018-08-01
 */
public interface QuartzSchedulejobMapper extends BaseMapper<QuartzSchedulejob> {

    /**
     * 根据任务id查找具体任务
     * @param scheduleJobGroupId 任务id
     * @return
     */
    List<QuartzSchedulejob> selectQuartzSchedulejobGroupId(@Param("scheduleJobGroupId") Integer scheduleJobGroupId);

    /**
     * 分页查询任务列表
     * @return
     */
    List<QuartzSchedulejob> selectQuartzSchedulejobList();

    /**
     * 查询全部任务
     * @return
     */
    List<QuartzSchedulejob> selectQuartzSchedulejobAll();
}