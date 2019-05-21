package wy.experiment.xposed.hook.util;

import com.alibaba.fastjson.JSON;

public class Configer {
    private static Configer mConfiger;
    private String url = "http://127.0.0.1:3000/";
    private String token = "";
    /**
     * 白天普通情况下每多少毫秒检测一次
     */
    private Integer delay_nor = 5000;

    /**
     * 夜间00:00-7:00,每多少秒检测一次
     */
    private Integer delay_slow = 15000;
    //支付宝用户名
    private String a;

    public synchronized static Configer getInstance() {
        if (mConfiger == null) {
            mConfiger = new SaveUtils().getJson(SaveUtils.BASE, Configer.class);
            if (mConfiger == null) {
                mConfiger = new Configer();
            }
        }
        return mConfiger;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public Integer getDelay_nor() {
        return delay_nor;
    }

    public void setDelay_nor(Integer delay_nor) {
        this.delay_nor = delay_nor;
    }

    public Integer getDelay_slow() {
        return delay_slow;
    }

    public void setDelay_slow(Integer delay_slow) {
        this.delay_slow = delay_slow;
    }

    public String getA() {
        return a;
    }

    public void setA(String a) {
        this.a = a;
    }

    public void save() {
        new SaveUtils().putJson(SaveUtils.BASE, this).commit();
    }

    @Override
    public String toString() {
        return JSON.toJSONString(this);
    }
}
