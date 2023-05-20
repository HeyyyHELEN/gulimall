package cn.edu.hjnu.gulimall.product.service;

import com.baomidou.mybatisplus.extension.service.IService;
import cn.edu.hjnu.common.utils.PageUtils;
import cn.edu.hjnu.gulimall.product.entity.ProductAttrValueEntity;

import java.util.List;
import java.util.Map;

/**
 * spu属性值
 *
 * @author HELEN
 * @email 784460900@qq.com
 * @date 2023-05-10 12:22:23
 */
public interface ProductAttrValueService extends IService<ProductAttrValueEntity> {

    PageUtils queryPage(Map<String, Object> params);

    List<ProductAttrValueEntity> baseAttrlistForSpu(Long spuId);

    void updateSpuAttr(Long spuId, List<ProductAttrValueEntity> entities);
}

