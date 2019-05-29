package com.baozi;

import com.baozi.configurer.ElasticsearchConfig;
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
 * @Date: 2019-05-14 13:20
 */
@RunWith(SpringRunner.class)
@SpringBootTest
@WebAppConfiguration
public class ElasticsearchTest {

    @Test
    public void centralized(){
        //System.out.println(ElasticsearchConfig.deleteIndex("goods_oupon"));
        //System.out.println(ElasticsearchConfig.createIndex("goods_oupon","goods_oupon"));
        //System.out.println(ElasticsearchConfig.batchImportData("goods_oupon"));
    }
}
