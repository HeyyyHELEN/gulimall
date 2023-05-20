package cn.edu.hjnu.gulimall.ware.service.impl;

import cn.edu.hjnu.common.utils.R;
import cn.edu.hjnu.common.utils.WareConstant;
import cn.edu.hjnu.gulimall.ware.feign.ProductFeignService;
import cn.edu.hjnu.gulimall.ware.service.WareSkuService;
import cn.edu.hjnu.gulimall.ware.vo.MergeVo;
import cn.edu.hjnu.gulimall.ware.entity.PurchaseDetailEntity;
import cn.edu.hjnu.gulimall.ware.service.PurchaseDetailService;
import cn.edu.hjnu.gulimall.ware.vo.PurchaseDoneVo;
import cn.edu.hjnu.gulimall.ware.vo.PurchaseItemDoneVo;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import cn.edu.hjnu.common.utils.PageUtils;
import cn.edu.hjnu.common.utils.Query;

import cn.edu.hjnu.gulimall.ware.dao.PurchaseDao;
import cn.edu.hjnu.gulimall.ware.entity.PurchaseEntity;
import cn.edu.hjnu.gulimall.ware.service.PurchaseService;
import org.springframework.transaction.annotation.Transactional;


@Service("purchaseService")
public class PurchaseServiceImpl extends ServiceImpl<PurchaseDao, PurchaseEntity> implements PurchaseService {

    @Autowired
    private PurchaseDetailService detailService;

    @Autowired
    private ProductFeignService productFeignService;

    @Autowired
    private WareSkuService wareSkuService;

    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        IPage<PurchaseEntity> page = this.page(
                new Query<PurchaseEntity>().getPage(params),
                new QueryWrapper<PurchaseEntity>()
        );

        return new PageUtils(page);
    }

    @Override
    public PageUtils queryPageUnreceive(Map<String, Object> params) {
        QueryWrapper<PurchaseEntity> wrapper = new QueryWrapper<>();
        wrapper.eq("status", WareConstant.PurchaseStatusEnmu.CREATED.getCode()).or().eq("status",WareConstant.PurchaseStatusEnmu.ASSIGNED.getCode());
        IPage<PurchaseEntity> page = this.page(new Query<PurchaseEntity>().getPage(params), wrapper);
        return new PageUtils(page);
    }

    @Transactional
    @Override
    public void mergePurchase(MergeVo mergeVo) {
        Long purchaseId = mergeVo.getPurchaseId();
        if (purchaseId == null){
            PurchaseEntity purchaseEntity = new PurchaseEntity();
            purchaseEntity.setStatus(WareConstant.PurchaseStatusEnmu.CREATED.getCode());
            this.save(purchaseEntity);
            purchaseId = purchaseEntity.getId();
        }
        List<Long> items = mergeVo.getItems();
        Long finalPurchaseId = purchaseId;
        List<PurchaseDetailEntity> list = detailService.getBaseMapper().selectBatchIds(items).stream().filter(entity -> {
            return entity.getStatus() < WareConstant.PurchaseDetailStatusEnmu.BUYING.getCode()
                    || entity.getStatus() == WareConstant.PurchaseDetailStatusEnmu.HASERROR.getCode();
        }).map(entity -> {
            entity.setStatus(WareConstant.PurchaseDetailStatusEnmu.ASSIGNED.getCode());
            entity.setPurchaseId(finalPurchaseId);
            return entity;
        }).collect(Collectors.toList());
        detailService.updateBatchById(list);
    }

    @Override
    public void received(List<Long> ids) {
        //没有采购需求直接返回，否则会破坏采购单
        if (ids == null || ids.size() == 0){
            return;
        }

        List<PurchaseEntity> list = this.getBaseMapper().selectBatchIds(ids).stream().filter(entity -> {
            return entity.getStatus() <= WareConstant.PurchaseStatusEnmu.ASSIGNED.getCode();
        }).map(entity -> {
            //修改采购单的状态为已领取
            entity.setStatus(WareConstant.PurchaseStatusEnmu.RECEIVE.getCode());
            return entity;
        }).collect(Collectors.toList());
        this.updateBatchById(list);

        //修改改采购单下的所有采购需求的状态为正在采购
        UpdateWrapper<PurchaseDetailEntity> updateWrapper = new UpdateWrapper<>();
        updateWrapper.in("purchase_id",ids);
        PurchaseDetailEntity purchaseDetailEntity = new PurchaseDetailEntity();
        purchaseDetailEntity.setStatus(WareConstant.PurchaseDetailStatusEnmu.BUYING.getCode());
        detailService.update(purchaseDetailEntity,updateWrapper);
    }

    @Override
    public void done(PurchaseDoneVo vo) {
        //1、根据前端发过来的信息，更新采购需求的状态
        List<PurchaseItemDoneVo> items = vo.getItems();
        ArrayList<PurchaseDetailEntity> updateList = new ArrayList<>();
        boolean flag = true;
        for (PurchaseItemDoneVo item : items) {
            Long detailId = item.getItemId();
            PurchaseDetailEntity detailEntity = detailService.getById(detailId);
            detailEntity.setStatus(item.getStatus());
            //采购需求失败
            if (item.getStatus() == WareConstant.PurchaseDetailStatusEnmu.HASERROR.getCode()){
                flag = false;
                String skuName = "";
                try {
                    R info = productFeignService.info(detailEntity.getSkuId());
                    if (info.getCode() == 0){
                        Map<String,Object> data = (Map<String, Object>) info.get("skuInfo");
                        skuName = (String) data.get("skuName");
                    }
                }catch (Exception e){

                }
                //更新库存
                wareSkuService.addStock(detailEntity.getSkuId(), detailEntity.getWareId(), skuName, detailEntity.getSkuNum());
            }
            updateList.add(detailEntity);
        }
        //保存采购需求
        detailService.updateBatchById(updateList);
        //2、根据采购需求的状态，更新采购单的状态
        PurchaseEntity purchaseEntity = new PurchaseEntity();
        purchaseEntity.setId(vo.getId());
        purchaseEntity.setStatus(false ? WareConstant.PurchaseStatusEnmu.FINISH.getCode() : WareConstant.PurchaseStatusEnmu.HASERROR.getCode());
        this.updateById(purchaseEntity);
    }

}