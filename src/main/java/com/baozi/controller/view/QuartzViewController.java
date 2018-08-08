package com.baozi.controller.view;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import springfox.documentation.annotations.ApiIgnore;

/**
 * Copyright:   互融云
 * 视图跳转controller
 * @author: zhangwenjun
 * @version: V1.0
 * @Date: 2018-08-01 13:38
 */
@Controller
@ApiIgnore()
public class QuartzViewController {

    @RequestMapping("/group")
    public String quartzGroup(){
        return "/quartz/quartzGroup";
    }

    @RequestMapping("/quartz")
    public String quartzList(){
        return "/quartz/quartzList";
    }
}
