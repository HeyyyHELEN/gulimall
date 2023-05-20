package cn.edu.hjnu.gulimall.ware.vo;

import lombok.Data;
import org.aspectj.lang.annotation.DeclareAnnotation;

import java.util.List;

@Data
public class PurchaseDoneVo {

    private Long id;
    private List<PurchaseItemDoneVo> items;

}
