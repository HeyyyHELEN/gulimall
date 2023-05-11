package cn.edu.hjnu.gulimall.product;

import cn.edu.hjnu.gulimall.product.entity.BrandEntity;
import cn.edu.hjnu.gulimall.product.entity.CategoryBrandRelationEntity;
import cn.edu.hjnu.gulimall.product.service.BrandService;
import cn.edu.hjnu.gulimall.product.service.CategoryBrandRelationService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;


@SpringBootTest
class GulimallProductApplicationTests {

    @Autowired
    BrandService brandService;




    @Test
    void contextLoads() {
        BrandEntity brandEntity = new BrandEntity();
        brandEntity.setName("华为");
        brandService.save(brandEntity);
        System.out.println("保存成功...");
    }

}
