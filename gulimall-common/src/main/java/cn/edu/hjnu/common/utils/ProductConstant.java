package cn.edu.hjnu.common.utils;

import lombok.Data;

@Data
public class ProductConstant {

    public enum AttrEnmu{
        ATTR_TYPE_BASE(1,"基本属性"),
        ATTR_TYPE_SALE(0,"销售属性");

        private int code;
        private String msg;

        AttrEnmu(int code, String msg) {
            this.code = code;
            this.msg = msg;
        }

        public int getCode() {
            return code;
        }

        public String getMsg() {
            return msg;
        }
    }

    public enum StatusEnmu{
        NEW_SPU(0,"新建"),
        SPU_UP(1,"商品上架");

        private int code;
        private String msg;

        StatusEnmu(int code, String msg) {
            this.code = code;
            this.msg = msg;
        }

        public int getCode() {
            return code;
        }

        public String getMsg() {
            return msg;
        }
    }

}
