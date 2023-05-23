package cn.edu.hjnu.gulimall.ware.service.impl;

import cn.edu.hjnu.gulimall.ware.vo.SkuHasStockVo;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import cn.edu.hjnu.common.utils.PageUtils;
import cn.edu.hjnu.common.utils.Query;

import cn.edu.hjnu.gulimall.ware.dao.WareSkuDao;
import cn.edu.hjnu.gulimall.ware.entity.WareSkuEntity;
import cn.edu.hjnu.gulimall.ware.service.WareSkuService;


@Service("wareSkuService")
public class WareSkuServiceImpl extends ServiceImpl<WareSkuDao, WareSkuEntity> implements WareSkuService {

    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        IPage<WareSkuEntity> page = this.page(
                new Query<WareSkuEntity>().getPage(params),
                new QueryWrapper<WareSkuEntity>()
        );

        return new PageUtils(page);
    }

    @Override
    public void addStock(Long skuId, Long wareId, String skuName, Integer skuNum) {
        WareSkuEntity wareSkuEntity = this.baseMapper.selectOne(new QueryWrapper<WareSkuEntity>().eq("sku_id", skuId).eq("ware_id", wareId));
        if (wareSkuEntity == null){
            //新增
             wareSkuEntity = new WareSkuEntity();
             wareSkuEntity.setStock(skuNum);
        }else {
            wareSkuEntity.setStock(wareSkuEntity.getStock() + skuNum);
        }
        wareSkuEntity.setSkuName(skuName);
        wareSkuEntity.setWareId(wareId);
        wareSkuEntity.setSkuId(skuId);
        this.saveOrUpdate(wareSkuEntity);
    }

    @Override
    public List<SkuHasStockVo> getSkuHasStock(List<Long> skuIds) {

        skuIds.stream().map(skuId->{
            SkuHasStockVo vo = new SkuHasStockVo();
            //查询当前sku的总库存量
            Long count = baseMapper.getSkuStock(skuId);
            vo.setSkuId(skuId);
            vo.setHasStock(count == null?false:count>0);
            return vo;
        }).collect(Collectors.toList());

        return null;
    }

}