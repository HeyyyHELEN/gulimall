package cn.edu.hjnu.gulimall.product.service;

import cn.edu.hjnu.gulimall.product.vo.Catelog2Vo;
import com.baomidou.mybatisplus.extension.service.IService;
import cn.edu.hjnu.common.utils.PageUtils;
import cn.edu.hjnu.gulimall.product.entity.CategoryEntity;

import java.util.List;
import java.util.Map;

/**
 * 商品三级分类
 *
 * @author HELEN
 * @email 784460900@qq.com
 * @date 2023-05-10 12:22:23
 */
public interface CategoryService extends IService<CategoryEntity> {

    PageUtils queryPage(Map<String, Object> params);


    List<CategoryEntity> listWithTree();

    void removeMenuByIds(List<Long> catIds);

    Long[] findCatelogPath(Long catelogId);

    void updateCascade(CategoryEntity category);

    List<CategoryEntity> getLevel1Categorys();

    Map<String, List<Catelog2Vo>> getCatalogJson();

    Map<String, List<Catelog2Vo>> getCatalogJsonAddCache();

    Map<String, List<Catelog2Vo>> getCatalogJsonBySpringCache();

}

