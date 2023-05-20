package cn.edu.hjnu.gulimall.product.controller;

import java.security.PublicKey;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import cn.edu.hjnu.gulimall.product.entity.ProductAttrValueEntity;
import cn.edu.hjnu.gulimall.product.service.ProductAttrValueService;
import cn.edu.hjnu.gulimall.product.vo.AttrRespVo;
import cn.edu.hjnu.gulimall.product.vo.AttrVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import cn.edu.hjnu.gulimall.product.service.AttrService;
import cn.edu.hjnu.common.utils.PageUtils;
import cn.edu.hjnu.common.utils.R;



/**
 * 商品属性
 *
 * @author HELEN
 * @email 784460900@qq.com
 * @date 2023-05-10 12:22:23
 */
@RestController
@RequestMapping("product/attr")
public class AttrController {

    @Autowired
    private AttrService attrService;

    @Autowired
    private ProductAttrValueService productAttrValueService;

    @PostMapping("/update/{spuId}")
    public R updateSpuAttr(@PathVariable("spuId") Long spuId, @RequestBody List<ProductAttrValueEntity> entities){
        productAttrValueService.updateSpuAttr(spuId,entities);
        return R.ok();
    }

    @GetMapping("/base/listforspu/{spuId}")
    public R baseListforspu(@PathVariable("spuId") Long spuId){
        List<ProductAttrValueEntity> entityList = productAttrValueService.baseAttrlistForSpu(spuId);
        return R.ok().put("data",entityList);
    }


    @RequestMapping("/{attrType}/list/{catelogId}")
    public R baseList(@RequestParam Map<String, Object> params, @PathVariable("catelogId") Long catelogId,
                      @PathVariable("attrType") String attrType){
        PageUtils page = attrService.queryBaseAttrPage(params, catelogId, attrType);
        return R.ok().put("page",page);
    }


//    @GetMapping("/base/list/{catelogId}")
//    public R baseAttrList(@RequestParam Map<String, Object> params,
//                          @PathVariable("catelogId") Long catelogId){
//        PageUtils page = attrService.queryBaseAttrPage(params,catelogId, attrType);
//        return R.ok().put("page", page);
//    }

    /**
     * 列表
     */
    @RequestMapping("/list")
    public R list(@RequestParam Map<String, Object> params){
        PageUtils page = attrService.queryPage(params);

        return R.ok().put("page", page);
    }


    /**
     * 信息
     */
    @RequestMapping("/info/{attrId}")
    public R info(@PathVariable("attrId") Long attrId){
        AttrRespVo attrRespVo = attrService.getAttrInfo(attrId);

        return R.ok().put("attr", attrRespVo);
    }

    /**
     * 保存
     */
    @RequestMapping("/save")
    public R save(@RequestBody AttrVo attr){
		attrService.saveAttr(attr);

        return R.ok();
    }

    /**
     * 修改
     */
    @RequestMapping("/update")
    public R update(@RequestBody AttrVo attr){
		attrService.updateAttr(attr);

        return R.ok();
    }

    /**
     * 删除
     */
    @RequestMapping("/delete")
    public R delete(@RequestBody Long[] attrIds){
		attrService.removeByIds(Arrays.asList(attrIds));

        return R.ok();
    }

}
