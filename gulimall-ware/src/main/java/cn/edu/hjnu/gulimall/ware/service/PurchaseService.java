package cn.edu.hjnu.gulimall.ware.service;

import cn.edu.hjnu.gulimall.ware.vo.MergeVo;
import cn.edu.hjnu.gulimall.ware.vo.PurchaseDoneVo;
import com.baomidou.mybatisplus.extension.service.IService;
import cn.edu.hjnu.common.utils.PageUtils;
import cn.edu.hjnu.gulimall.ware.entity.PurchaseEntity;

import java.util.List;
import java.util.Map;

/**
 * 采购信息
 *
 * @author HELEN
 * @email 784460900@qq.com
 * @date 2023-05-10 13:07:46
 */
public interface PurchaseService extends IService<PurchaseEntity> {

    PageUtils queryPage(Map<String, Object> params);

    PageUtils queryPageUnreceive(Map<String, Object> params);

    void mergePurchase(MergeVo mergeVo);

    void received(List<Long> ids);

    void done(PurchaseDoneVo vo);
}

