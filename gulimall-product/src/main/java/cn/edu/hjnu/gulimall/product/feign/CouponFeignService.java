package cn.edu.hjnu.gulimall.product.feign;

import cn.edu.hjnu.common.to.SkuReductionTo;
import cn.edu.hjnu.common.to.SpuBoundTo;
import cn.edu.hjnu.common.utils.R;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient("gulimall-coupon")
public interface CouponFeignService {

    @PostMapping("coupon/spubounds/save")
    R saveSpuBounds(@RequestBody SpuBoundTo spuBoundTo);

    @PostMapping("coupon/skufullreduction/saveInfo")
    R saveSkuReduction(@RequestBody SkuReductionTo skuReductionTo);

}
