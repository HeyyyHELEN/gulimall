package cn.edu.hjnu.gulimall.coupon.dao;

import cn.edu.hjnu.gulimall.coupon.entity.CouponEntity;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

/**
 * 优惠券信息
 * 
 * @author HELEN
 * @email 784460900@qq.com
 * @date 2023-05-10 12:44:40
 */
@Mapper
public interface CouponDao extends BaseMapper<CouponEntity> {
	
}
