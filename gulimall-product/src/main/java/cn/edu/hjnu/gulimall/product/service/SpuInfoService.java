package cn.edu.hjnu.gulimall.product.service;

import cn.edu.hjnu.gulimall.product.vo.SpuSaveVo;
import com.baomidou.mybatisplus.extension.service.IService;
import cn.edu.hjnu.common.utils.PageUtils;
import cn.edu.hjnu.gulimall.product.entity.SpuInfoEntity;

import java.util.Map;

/**
 * spu信息
 *
 * @author HELEN
 * @email 784460900@qq.com
 * @date 2023-05-10 12:22:23
 */
public interface SpuInfoService extends IService<SpuInfoEntity> {

    PageUtils queryPage(Map<String, Object> params);


    void saveSupInfo(SpuSaveVo vo);

    PageUtils queryPageByCondition(Map<String, Object> params);

    //商品上架
    void up(Long spuId);
}

