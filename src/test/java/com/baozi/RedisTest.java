package com.baozi;

import com.baozi.service.redis.RedisService;
import com.baozi.util.spring.SpringContextUtil;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.context.web.WebAppConfiguration;

/**
 * Copyright:   互融云
 *
 * @author: zhangwenjun
 * @version: V1.0
 * @Date: 2018-08-08 12:57
 */
@RunWith(SpringRunner.class)
@SpringBootTest
@WebAppConfiguration
public class RedisTest {

    @Test
    public void centralized(){
        RedisService redisService = (RedisService) SpringContextUtil.getBean(RedisService.class);
        redisService.hmSet("baozi","becat","{\"title\":\"BeCat蓝胖子-了解一下\",\"website\":\"https://www.doudoucat.com\",\"wechat\":{\"name\":\"豆豆的蓝胖子\",\"description\":\"微信搜索豆豆的蓝胖子关注走一波\"}}");
        System.out.println(redisService.hmGet("baozi","becat"));
    }
}
