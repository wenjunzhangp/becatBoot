package com.baozi.dao.quartz;

import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.baozi.entity.quartz.QuartzSchedulejobGroup;

import java.util.List;

/**
 * mapper接口
 * @author wenjunzhangp
 * @since 2018-08-01
 */
public interface QuartzSchedulejobGroupMapper extends BaseMapper<QuartzSchedulejobGroup> {

    /**
     * 分页查询任务列表
     * @return
     */
    List<QuartzSchedulejobGroup> selectQuartzSchedulejobGroupList();
}