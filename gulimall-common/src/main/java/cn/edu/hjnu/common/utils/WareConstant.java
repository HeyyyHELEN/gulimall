package cn.edu.hjnu.common.utils;

public class WareConstant {

    public enum PurchaseStatusEnmu{
        CREATED(0,"新建"),ASSIGNED(1,"已分配"),
        RECEIVE(2,"已领取"),FINISH(3,"已完成"),
        HASERROR(4,"有异常");

        private int code;
        private String msg;

        PurchaseStatusEnmu(int code, String msg) {
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

    public enum PurchaseDetailStatusEnmu{
        CREATE(0,"新建"),ASSIGNED(1,"已分配"),
        BUYING(2,"正在采购"),FINISH(3,"已完成"),
        HASERROR(4,"采购失败");

        private int code;
        private String msg;

        PurchaseDetailStatusEnmu(int code, String msg) {
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
