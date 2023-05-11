package cn.edu.hjnu.gulimall.order.dao;

import cn.edu.hjnu.gulimall.order.entity.OrderSettingEntity;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

/**
 * 订单配置信息
 * 
 * @author HELEN
 * @email 784460900@qq.com
 * @date 2023-05-10 13:01:49
 */
@Mapper
public interface OrderSettingDao extends BaseMapper<OrderSettingEntity> {
	
}
