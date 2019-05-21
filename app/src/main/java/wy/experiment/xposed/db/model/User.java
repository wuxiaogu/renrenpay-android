package wy.experiment.xposed.db.model;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

/**
 * Created by chenxinyou on 2019/3/7.
 */

@DatabaseTable(tableName = "user")
public class User {
    @DatabaseField(generatedId = true)
    private int id;

    @DatabaseField(columnName = "mobile_phone")
    private String mobile_phone;

    @DatabaseField(columnName = "email")
    private String email;

    @DatabaseField(columnName = "userid")
    private int userid;

    @DatabaseField(columnName = "token")
    private String token;

    @DatabaseField(columnName = "lv")
    private int lv;

    @DatabaseField(columnName = "status")
    private int status;

    @DatabaseField(columnName = "membership_expired")
    private long membership_expired;

    @DatabaseField(columnName = "balance")
    private int balance;

    @DatabaseField(columnName = "nickname")
    private String nickname;

    @DatabaseField(columnName = "weixin")
    private String weixin;

    @DatabaseField(columnName = "qq")
    private String qq;

    @DatabaseField(columnName = "address")
    private String address;


    public int getId() {
        return id;
    }

    public String getMobile_phone() {
        return mobile_phone;
    }

    public void setMobile_phone(String mobile_phone) {
        this.mobile_phone = mobile_phone;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public int getUserid() {
        return userid;
    }

    public void setUserid(int userid) {
        this.userid = userid;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public int getLv() {
        return lv;
    }

    public void setLv(int lv) {
        this.lv = lv;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public long getMembership_expired() {
        return membership_expired;
    }

    public void setMembership_expired(long membership_expired) {
        this.membership_expired = membership_expired;
    }

    public int getBalance() {
        return balance;
    }

    public void setBalance(int balance) {
        this.balance = balance;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public String getWeixin() {
        return weixin;
    }

    public void setWeixin(String weixin) {
        this.weixin = weixin;
    }

    public String getQq() {
        return qq;
    }

    public void setQq(String qq) {
        this.qq = qq;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    @Override
    public String toString() {
        return "id -> " + id + ";mobile_phone -> " + mobile_phone + "; userid -> " + userid + "; \ntoken -> " + token;
    }
}
