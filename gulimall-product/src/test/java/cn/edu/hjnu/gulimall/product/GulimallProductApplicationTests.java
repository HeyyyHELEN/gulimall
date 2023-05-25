package cn.edu.hjnu.gulimall.product;

import cn.edu.hjnu.gulimall.product.entity.BrandEntity;
import cn.edu.hjnu.gulimall.product.entity.CategoryBrandRelationEntity;
import cn.edu.hjnu.gulimall.product.service.BrandService;
import cn.edu.hjnu.gulimall.product.service.CategoryBrandRelationService;
import org.junit.jupiter.api.Test;
import org.redisson.api.RedissonClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.ValueOperations;


@SpringBootTest
class GulimallProductApplicationTests {

    @Autowired
    BrandService brandService;

    @Autowired
    StringRedisTemplate redisTemplate;

    @Autowired
    RedissonClient redissonClient;

    @Test
    void testredissonClient(){
        System.out.println(redissonClient);
    }

    @Test
    void contextLoads() {
        BrandEntity brandEntity = new BrandEntity();
        brandEntity.setName("华为");
        brandService.save(brandEntity);
        System.out.println("保存成功...");
    }

    @Test
    void testredisTemplate(){
        ValueOperations<String, String> operations = redisTemplate.opsForValue();
        operations.set("a","aa");
        String a = operations.get("a");
        System.out.println(a);
    }



}
