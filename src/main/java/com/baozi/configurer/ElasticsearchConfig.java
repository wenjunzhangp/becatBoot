package com.baozi.configurer;

import com.alibaba.fastjson.JSON;
import com.baozi.entity.coupon.LpzTaobaoCoupon;
import com.baozi.service.coupon.ILpzTaobaoCouponService;
import com.baozi.util.LogUtils;
import com.baozi.util.spring.SpringContextUtil;
import org.apache.http.HttpHost;
import org.elasticsearch.action.admin.indices.create.CreateIndexResponse;
import org.elasticsearch.action.admin.indices.delete.DeleteIndexRequest;
import org.elasticsearch.action.bulk.BulkRequest;
import org.elasticsearch.action.bulk.BulkResponse;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.client.IndicesClient;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestClient;
import org.elasticsearch.action.admin.indices.create.CreateIndexRequest;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.common.xcontent.XContentBuilder;
import org.elasticsearch.common.xcontent.XContentFactory;
import org.elasticsearch.common.xcontent.XContentType;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Copyright:   张文君
 *
 * @author: zhangwenjun
 * @version: V1.0
 * @Date: 2019-05-13 14:45
 */
@Configuration
public class ElasticsearchConfig {

    private static RestHighLevelClient client = client();

    /*初始化api客户端*/
    @Bean
    private static RestHighLevelClient client() {
        RestHighLevelClient client = new RestHighLevelClient(
                RestClient.builder(
                        new HttpHost("123.56.219.123", 9200, "http")));
        LogUtils.logInfo("********************************************");
        LogUtils.logInfo(client.toString());
        LogUtils.logInfo("********************************************");
        return client;
    }

    /*生成mapping*/
    private static XContentBuilder genMapping() throws Exception{
        XContentBuilder mapping = XContentFactory.jsonBuilder()
                .startObject()
                .startObject("properties")
                .startObject("id").field("type","text").endObject()
                .startObject("goodsName").field("type","text").field("analyzer", "ik_smart").field("search_analyzer", "ik_smart").endObject()
                .startObject("goodsRemark").field("type","text").field("analyzer", "ik_smart").field("search_analyzer", "ik_smart").endObject()
                .startObject("onlinePrice").field("type","double").endObject()
                .startObject("couponPrice").field("type","double").endObject()
                .startObject("sellCount").field("type","integer").endObject()
                .startObject("update").field("type","text").field("analyzer", "ik_smart").field("search_analyzer", "ik_smart").endObject()
                .startObject("createDate").field("type","date").endObject()
                .startObject("linkUrl").field("type","text").endObject()
                .endObject()
                .endObject();
        return mapping;
    }

    /*创建索引*/
    public static boolean createIndex(String indexName,String type) {
        try {
            Settings settings = Settings.builder().put("number_of_shards", 3).put("number_of_replicas", 3).build();
            CreateIndexRequest request = new CreateIndexRequest(indexName).settings(settings).mapping(type,genMapping());
            IndicesClient indices = client.indices();
            CreateIndexResponse createIndexResponse = indices.create(request,RequestOptions.DEFAULT);
            boolean acknowledged = createIndexResponse.isAcknowledged();
            return acknowledged;
        }catch (Exception e){
            LogUtils.logError("创建索引发生异常",e);
            return false;
        }
    }

    /*删除索引*/
    public static boolean deleteIndex(String indexName){
        try {
            DeleteIndexRequest request = new DeleteIndexRequest(indexName);
            client.indices().delete(request,RequestOptions.DEFAULT);
            return true;
        }catch (Exception e){
            LogUtils.logError("删除索引发生异常",e);
            return false;
        }
    }

    /*插入数据*/
    public static boolean batchImportData(String indexName){
        try {
            LogUtils.logInfo("开始向Elasticsearch灌数据");
            Long sendTime = new Date().getTime();
            Long endTime = new Long("0");
            ILpzTaobaoCouponService lLpzTaobaoCouponService = SpringContextUtil.getBean(ILpzTaobaoCouponService.class);
            Map<String,Object> paramMap = new HashMap<>();
            int count = 0;
            int page = 1;
            paramMap.put("page",page);
            paramMap.put("limit",1000);
            BulkRequest request = new BulkRequest();
            while (page<88){
                paramMap.put("page",page);
                List<LpzTaobaoCoupon> allList = lLpzTaobaoCouponService.selectLpzTaobaoCouponAll(paramMap).getList();
                if (allList.isEmpty())
                    break;
                for (int i=0;i<allList.size();i++) {
                    LpzTaobaoCoupon lpzTaobaoCoupon = allList.get(i);
                    Map<String,Object> current = new HashMap<>();
                    current.put("id",lpzTaobaoCoupon.getId());
                    current.put("goodsName",lpzTaobaoCoupon.getGoodsname());
                    current.put("goodsRemark",lpzTaobaoCoupon.getGoodsremark());
                    current.put("onlinePrice",lpzTaobaoCoupon.getOnlineprice());
                    current.put("couponPrice",lpzTaobaoCoupon.getCouponprice());
                    current.put("sellCount",lpzTaobaoCoupon.getSellcount());
                    current.put("update",lpzTaobaoCoupon.getUpdate());
                    current.put("createDate",lpzTaobaoCoupon.getCreatedate());
                    current.put("linkUrl",lpzTaobaoCoupon.getLinkurl());
                    request.add(new IndexRequest(indexName).source(current,XContentType.JSON));
                    BulkResponse bulkResponse = client.bulk(request,RequestOptions.DEFAULT);
                    System.out.println(bulkResponse);
                    if (count % 1000 == 0){
                        page++;
                    }
                    count++;
                }
            }
            endTime = new Date().getTime();
            LogUtils.logInfo("数据灌入完毕，耗时["+(endTime-sendTime)+"]毫秒");
            return true;
        } catch ( Exception e ){
            LogUtils.logError("灌数据发生异常",e);
            return false;
        }
    }
}
