package cn.edu.hjnu.gulimall.ware.controller;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

import cn.edu.hjnu.gulimall.ware.vo.SkuHasStockVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import cn.edu.hjnu.gulimall.ware.entity.WareSkuEntity;
import cn.edu.hjnu.gulimall.ware.service.WareSkuService;
import cn.edu.hjnu.common.utils.PageUtils;
import cn.edu.hjnu.common.utils.R;



/**
 * 商品库存
 *
 * @author HELEN
 * @email 784460900@qq.com
 * @date 2023-05-10 13:07:46
 */
@RestController
@RequestMapping("ware/waresku")
public class WareSkuController {
    @Autowired
    private WareSkuService wareSkuService;

    //查询sku是否有库存
    @PostMapping("/hasstock")
    public R getSkuHasStock(@RequestBody List<Long> skuIds){
        //
        List<SkuHasStockVo> vos = wareSkuService.getSkuHasStock(skuIds);

        return R.ok().setData(vos);
    }

    /**
     * 列表
     */
    @RequestMapping("/list")
    public R list(@RequestParam Map<String, Object> params){
        PageUtils page = wareSkuService.queryPage(params);

        return R.ok().put("page", page);
    }


    /**
     * 信息
     */
    @RequestMapping("/info/{id}")
    public R info(@PathVariable("id") Long id){
		WareSkuEntity wareSku = wareSkuService.getById(id);

        return R.ok().put("wareSku", wareSku);
    }

    /**
     * 保存
     */
    @RequestMapping("/save")
    public R save(@RequestBody WareSkuEntity wareSku){
		wareSkuService.save(wareSku);

        return R.ok();
    }

    /**
     * 修改
     */
    @RequestMapping("/update")
    public R update(@RequestBody WareSkuEntity wareSku){
		wareSkuService.updateById(wareSku);

        return R.ok();
    }

    /**
     * 删除
     */
    @RequestMapping("/delete")
    public R delete(@RequestBody Long[] ids){
		wareSkuService.removeByIds(Arrays.asList(ids));

        return R.ok();
    }

}
