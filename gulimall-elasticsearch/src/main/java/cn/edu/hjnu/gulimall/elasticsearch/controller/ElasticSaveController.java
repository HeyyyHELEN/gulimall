package cn.edu.hjnu.gulimall.elasticsearch.controller;

import cn.edu.hjnu.common.to.es.SkuEsModel;
import cn.edu.hjnu.common.utils.BizCodeEnmu;
import cn.edu.hjnu.common.utils.R;
import cn.edu.hjnu.gulimall.elasticsearch.service.ProductSaveService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RequestMapping("/search/save")
@RestController
@Slf4j
public class ElasticSaveController {

    @Autowired
    ProductSaveService productSaveService;

    //上架商品
    @PostMapping("/product")
    public R productStatusUp(@RequestBody List<SkuEsModel> skuEsModels){
        Boolean b = false;
        try {
            b = productSaveService.productStatusUp(skuEsModels);
        }catch (Exception e){
            log.error("ElasticSaveController商品上架错误：{}",e);
            return R.error(BizCodeEnmu.PRODUCT_UP_EXCEPTION.getCode(),BizCodeEnmu.PRODUCT_UP_EXCEPTION.getMsg());
        }
        if (!b){return R.ok();}
        else {return R.error(BizCodeEnmu.PRODUCT_UP_EXCEPTION.getCode(),BizCodeEnmu.PRODUCT_UP_EXCEPTION.getMsg());}
    }

}
