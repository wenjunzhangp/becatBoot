package com.baozi.configurer.quartz;

import org.quartz.Scheduler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.quartz.SchedulerFactoryBean;

/**
 *
 * @author wenjunzhangp
 * @since 2018-08-01
 */
@Configuration
public class  SchedledConfiguration {

    private static Logger logger = LoggerFactory.getLogger(SchedledConfiguration.class);

    @Autowired
    private ApplicationContext applicationContext;
    @Autowired
    private SpringJobFactory springJobFactory;
    @Bean
    public SchedulerFactoryBean schedulerFactoryBean(){

        SchedulerFactoryBean schedulerFactoryBean = new SchedulerFactoryBean();

        return schedulerFactoryBean;
    }

    @Bean
    public Scheduler scheduler() {
        logger.info("scheduler1=" + schedulerFactoryBean().getScheduler());
        return schedulerFactoryBean().getScheduler();
    }


}
