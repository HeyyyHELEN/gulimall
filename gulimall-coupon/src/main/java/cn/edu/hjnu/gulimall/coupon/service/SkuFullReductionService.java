package cn.edu.hjnu.gulimall.coupon.service;

import cn.edu.hjnu.common.to.SkuReductionTo;
import com.baomidou.mybatisplus.extension.service.IService;
import cn.edu.hjnu.common.utils.PageUtils;
import cn.edu.hjnu.gulimall.coupon.entity.SkuFullReductionEntity;

import java.util.Map;

/**
 * 商品满减信息
 *
 * @author HELEN
 * @email 784460900@qq.com
 * @date 2023-05-10 12:44:40
 */
public interface SkuFullReductionService extends IService<SkuFullReductionEntity> {

    PageUtils queryPage(Map<String, Object> params);

    void saveSkuReduction(SkuReductionTo skuReductionTo);
}

