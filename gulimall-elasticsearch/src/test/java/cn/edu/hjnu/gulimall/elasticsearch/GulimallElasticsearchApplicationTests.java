package cn.edu.hjnu.gulimall.elasticsearch;

import cn.edu.hjnu.gulimall.elasticsearch.config.ElasticSearchConfig;
import com.alibaba.fastjson.JSON;
import lombok.Data;
import lombok.ToString;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.action.index.IndexResponse;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.common.xcontent.XContentType;
import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.SearchHits;
import org.elasticsearch.search.aggregations.Aggregation;
import org.elasticsearch.search.aggregations.AggregationBuilders;
import org.elasticsearch.search.aggregations.Aggregations;
import org.elasticsearch.search.aggregations.bucket.terms.Terms;
import org.elasticsearch.search.aggregations.bucket.terms.TermsAggregationBuilder;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.IOException;

import static cn.edu.hjnu.gulimall.elasticsearch.config.ElasticSearchConfig.COMMON_OPTIONS;

@SpringBootTest
class GulimallElasticsearchApplicationTests {

    @Autowired
    private RestHighLevelClient restHighLevelClient;

    @Test
    void contextLoads() throws IOException {
        //创建索引的请求指定索引名称
        IndexRequest indexRequest = new IndexRequest("users");
        //指定id
        indexRequest.id("1");
        //
        User user = new User();
        user.setUsername("张三");
        user.setGender("MALE");
        user.setAge(18);
        //将对象解析为JSON字符串
        String s = JSON.toJSONString(user);
        indexRequest.source(s, XContentType.JSON);
        //保存后拿到响应结果
        IndexResponse index = restHighLevelClient.index(indexRequest, COMMON_OPTIONS);
        System.out.println(index);

    }

    @Data
    class User{
        private String username;
        private String gender;
        private Integer age;
    }

    @Test
    public void searchData() throws IOException {
        //1、创建索引请求
        SearchRequest searchRequest = new SearchRequest();
        //2、指定索引
        searchRequest.indices("bank");
        //3、指定DSL检索条件
        //SearchSourceBuilder
        SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
        //3.1构建索引条件
//        searchSourceBuilder.query();
//        searchSourceBuilder.from();
//        searchSourceBuilder.size();
//        searchSourceBuilder.aggregation();
        searchSourceBuilder.query(QueryBuilders.matchQuery("address","mill"));
        //创建聚合条件
        //1.查看值分布聚合
        TermsAggregationBuilder agg1 = AggregationBuilders.terms("ageAgg").field("age").size(10);
        //将聚合条件加入到查询条件中
        searchSourceBuilder.aggregation(agg1);
        searchRequest.source(searchSourceBuilder);
        //4、执行检索，拿到数据
        SearchResponse searchResponse = restHighLevelClient.search(searchRequest, COMMON_OPTIONS);
        //5、分析结果
        //获取所有查到的数据
        SearchHits hits = searchResponse.getHits();
        SearchHit[] searchHits = hits.getHits();
        for (SearchHit searchHit : searchHits) {
            String string = searchHit.getSourceAsString();
            Accout accout = JSON.parseObject(string, Accout.class);
            System.out.println(accout);
        }

        //获取检索到的分析信息
        Aggregations aggregations = searchResponse.getAggregations();
        Terms aggName = aggregations.get("ageAgg");
        for (Terms.Bucket bucket : aggName.getBuckets()) {
            String keyAsString = bucket.getKeyAsString();
            System.out.println("年龄"+keyAsString+bucket.getDocCount());
        }
    }

    /**
     * Auto-generated: 2021-10-25 16:57:49
     *
     * @author bejson.com (i@bejson.com)
     * @website http://www.bejson.com/java2pojo/
     */
    @Data
    @ToString
    public static class Accout {

        private int account_number;
        private int balance;
        private String firstname;
        private String lastname;
        private int age;
        private String gender;
        private String address;
        private String employer;
        private String email;
        private String city;
        private String state;
    }


}
