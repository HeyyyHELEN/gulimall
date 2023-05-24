package cn.edu.hjnu.gulimall.product.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class Catelog2Vo {

    private String catelog1Id;  // 1级父分类ID
    private List<Catelog3Vo> catelog3List;// 3级子分类集合
    private String id;  // 2级分类ID
    private String name;  // 2级分类name

    /**
     * 三级分类Vo
     */
    @NoArgsConstructor
    @AllArgsConstructor
    @Data
    public static class Catelog3Vo {
        private String catelog2Id;  // 2级父分类ID
        private String id;  // 3级分类ID
        private String name;  // 3级分类name

    }
}

