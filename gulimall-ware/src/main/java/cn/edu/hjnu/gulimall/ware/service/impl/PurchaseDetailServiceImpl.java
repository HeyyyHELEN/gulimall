package cn.edu.hjnu.gulimall.ware.service.impl;

import org.springframework.stereotype.Service;
import java.util.Map;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import cn.edu.hjnu.common.utils.PageUtils;
import cn.edu.hjnu.common.utils.Query;

import cn.edu.hjnu.gulimall.ware.dao.PurchaseDetailDao;
import cn.edu.hjnu.gulimall.ware.entity.PurchaseDetailEntity;
import cn.edu.hjnu.gulimall.ware.service.PurchaseDetailService;
import org.springframework.util.StringUtils;


@Service("purchaseDetailService")
public class PurchaseDetailServiceImpl extends ServiceImpl<PurchaseDetailDao, PurchaseDetailEntity> implements PurchaseDetailService {

    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        QueryWrapper<PurchaseDetailEntity> wrapper = new QueryWrapper<>();
        String key = (String) params.get("key");
        if (!StringUtils.isEmpty(key)){
            wrapper.and(w -> {
                w.eq("sku_id",key).or().eq("purchase",key);
            });
        }

        String status = (String) params.get("status");
        if (!StringUtils.isEmpty(status)){
            wrapper.eq("status",status);
        }

        String wareId = (String) params.get("wareId");
        if (!StringUtils.isEmpty(wareId)){
            wrapper.eq("ware_id",wareId);
        }

        IPage<PurchaseDetailEntity> page = this.page(
                new Query<PurchaseDetailEntity>().getPage(params),
                wrapper
        );

        return new PageUtils(page);
    }

}