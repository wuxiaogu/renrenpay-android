package wy.experiment.xposed.db.model;

/**
 * Created by chenxinyou on 2019/3/13.
 */

public class Order {
    private String orderid;
    private int amount;
    private int payway;
    private String appid;
    private String pay_code;
    private String order_userid;
    private String createdAt;

    public String getOrderid() {
        return orderid;
    }

    public void setOrderid(String orderid) {
        this.orderid = orderid;
    }

    public int getAmount() {
        return amount;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }

    public int getPayway() {
        return payway;
    }

    public void setPayway(int payway) {
        this.payway = payway;
    }

    public String getAppid() {
        return appid;
    }

    public void setAppid(String appid) {
        this.appid = appid;
    }

    public String getOrder_userid() {
        return order_userid;
    }

    public void setOrder_userid(String order_userid) {
        this.order_userid = order_userid;
    }

    public String getPay_code() {
        return pay_code;
    }

    public void setPay_code(String pay_code) {
        this.pay_code = pay_code;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }

    @Override
    public String toString() {
        return "orderId -> " + orderid;
    }
}
