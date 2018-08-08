package com.baozi.service.impl.dubbo;

import com.alibaba.dubbo.config.annotation.Service;
import com.baozi.service.dubbo.IDubboService;
import com.baozi.util.LoggerUtils;

/**
 * Copyright:   互融云
 *
 * @author: zhangwenjun
 * @version: V1.0
 * @Date: 2018-08-08 14:46
 */
@Service(version = "1.0.0")
public class DubboServiceImpl implements IDubboService{

    @Override
    public void registerServer() {
        LoggerUtils.logInfo("hello,this is bebcatboot dubbo!!");
    }
}
