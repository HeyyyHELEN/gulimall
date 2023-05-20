package cn.edu.hjnu.gulimall.ware.service;

import com.baomidou.mybatisplus.extension.service.IService;
import cn.edu.hjnu.common.utils.PageUtils;
import cn.edu.hjnu.gulimall.ware.entity.WareInfoEntity;

import java.util.Map;

/**
 * 仓库信息
 *
 * @author HELEN
 * @email 784460900@qq.com
 * @date 2023-05-10 13:07:46
 */
public interface WareInfoService extends IService<WareInfoEntity> {

    PageUtils queryPage(Map<String, Object> params);

    PageUtils queryPageByCondition(Map<String, Object> params);
}

