package cn.edu.hjnu.gulimall.product.dao;

import cn.edu.hjnu.gulimall.product.entity.CategoryEntity;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

/**
 * 商品三级分类
 * 
 * @author HELEN
 * @email 784460900@qq.com
 * @date 2023-05-10 12:22:23
 */
@Mapper
public interface CategoryDao extends BaseMapper<CategoryEntity> {
	
}
