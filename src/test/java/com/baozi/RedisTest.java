package com.baozi;

import com.baozi.util.RedisUtil;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.context.web.WebAppConfiguration;

/**
 * Copyright:   张文君
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
        RedisUtil.setex("baozi","zhangwnejun",60);
        System.out.println(RedisUtil.getStr("baozi"));
    }
}
