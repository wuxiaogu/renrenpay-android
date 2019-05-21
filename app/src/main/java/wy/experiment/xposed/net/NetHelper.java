package wy.experiment.xposed.net;

import android.util.Log;

import com.zhy.http.okhttp.OkHttpUtils;

import wy.experiment.xposed.tool.Md5Utils;
import wy.experiment.xposed.utils.XPConstant;
import wy.experiment.xposed.view.AppContext;


/**
 * Created by chenxinyou on 2019/3/7.
 */

public class NetHelper {
    private static final String httpHeader = "http://" + XPConstant.URL_IP + ":3000";
    private static final AppContext appContext = AppContext.getInstance();

    /**
     * 用户注册
     * /api/register
     */
    public static void userRegister(String userName, String password, String name, String code, HttpResHandler handler) {
        StringBuffer stringBuffer = new StringBuffer();
        stringBuffer.append(httpHeader);
        stringBuffer.append("/register");
        String registerPassword = Md5Utils.encode(password);
        OkHttpUtils.post().url(stringBuffer.toString()).addParams("phone", userName)
                .addParams("password", registerPassword).build().execute(handler);
    }

    /**
     * 获取salt
     * /api/users/salt
     */
    public static void getSalt(String phone, HttpResHandler handler) {
        StringBuffer stringBuffer = new StringBuffer();
        stringBuffer.append(httpHeader);
        stringBuffer.append("/api/users/salt");
        OkHttpUtils.get().url(stringBuffer.toString()).addParams("mobile_phone", phone).build().execute(handler);
    }

    /**
     * 用户登陆
     * /api2/login
     */
    public static void userLogin(String phone, String password, HttpResHandler handler) {
        StringBuffer stringBuffer = new StringBuffer();
        stringBuffer.append(httpHeader);
        stringBuffer.append("/api2/phone/login");
        OkHttpUtils.post().url(stringBuffer.toString()).addParams("mobile_phone", phone)
                .addParams("password", password).build().execute(handler);
    }

    /**
     * 完善用户资料
     * /api/user/modify
     */
    public static void modifyUserInfo(String nickName, String address, String wechart, String website, String e_mail, HttpResHandler handler) {
        StringBuffer stringBuffer = new StringBuffer();
        stringBuffer.append(httpHeader);
        stringBuffer.append("/user/modify");
        OkHttpUtils.post().url(stringBuffer.toString()).addParams("nick_name", nickName)
                .addParams("address", address).addParams("wechart", wechart)
                .addParams("website", website).addParams("e_mail", e_mail)
                .addHeader("token", appContext.getUser().getToken()).build().execute(handler);
    }

    /**
     * 获取应用列表
     * /api2/phone/query_app
     */
    public static void getAppList(HttpResHandler handler) {
        StringBuffer stringBuffer = new StringBuffer();
        stringBuffer.append(httpHeader);
        stringBuffer.append("/api2/phone/query_app");
        OkHttpUtils.post().url(stringBuffer.toString())
                .addHeader("token", appContext.getUser().getToken()).build().execute(handler);
    }

    /**
     * 获取app详情
     * /api/app/info
     */
    public static void getAppInfo(String appId, HttpResHandler handler) {
        StringBuffer stringBuffer = new StringBuffer();
        stringBuffer.append(httpHeader);
        stringBuffer.append("/app/info");
        OkHttpUtils.get().url(stringBuffer.toString()).addParams("app_id", appId)
                .addHeader("token", appContext.getUser().getToken()).build().execute(handler);
    }

    /**
     * 发生二维码
     * /api2/phone/get_qrcode
     */
    public static void receivePayCode(String codeUrl, String orderId, String appId, int amount, int payWay, String payCode, String order_userid, HttpResHandler handler) {
        Log.d("cxy", "token -> " + appContext.getUser().getToken());
        StringBuffer stringBuffer = new StringBuffer();
        stringBuffer.append(httpHeader);
        stringBuffer.append("/api2/phone/get_qrcode");
        OkHttpUtils.post().url(stringBuffer.toString()).addParams("qrcode", codeUrl)
                .addParams("appid", appId).addParams("orderid", orderId)
                .addParams("amount", amount + "").addParams("payway", payWay + "")
                .addParams("pay_code", payCode).addParams("userid", order_userid)
                .addHeader("token", appContext.getUser().getToken()).build().execute(handler);
    }

    /**
     * 支付成功通知
     * /api2/phone/pay_success
     */
    public static void paySuccessNotify(int pay_way, int amount, String pay_code, String transid, HttpResHandler handler) {
        StringBuffer stringBuffer = new StringBuffer();
        stringBuffer.append(httpHeader);
        stringBuffer.append("/api2/phone/pay_success");
        OkHttpUtils.post().url(stringBuffer.toString())
                .addParams("amount", amount + "").addParams("payway", pay_way + "")
                .addParams("pay_code", pay_code).addParams("transid", transid)
                .addHeader("token", appContext.getUser().getToken()).build().execute(handler);
    }

    /**
     * 查询收款列表
     * /api2/phone/paychecks_list
     */
    public static void paycheckList(int page, int pageSize, HttpResHandler handler) {
        StringBuffer stringBuffer = new StringBuffer();
        stringBuffer.append(httpHeader);
        stringBuffer.append("/api2/phone/paychecks_list");
        OkHttpUtils.get().url(stringBuffer.toString())
                .addParams("page", page + "").addParams("page_size", pageSize + "").addParams("state", "1")
                .addHeader("token", appContext.getUser().getToken()).build().execute(handler);
    }

    /**
     * 上传日志
     * /api2/phone/set_logs
     */
    public static void paySetLogs(String logs, HttpResHandler handler) {
        StringBuffer stringBuffer = new StringBuffer();
        stringBuffer.append(httpHeader);
        stringBuffer.append("/api2/phone/set_logs");
        OkHttpUtils.post().url(stringBuffer.toString())
                .addParams("list", logs + "")
                .addHeader("token", appContext.getUser().getToken()).build().execute(handler);
    }

    /**
     * 清除微信付款码
     * /api2/phone/clear_wx
     * */
    public static void clearWxPayCode(HttpResHandler handler) {
        StringBuffer stringBuffer = new StringBuffer();
        stringBuffer.append(httpHeader);
        stringBuffer.append("/api2/phone/clear_wx");
        OkHttpUtils.post().url(stringBuffer.toString())
                .addHeader("token", appContext.getUser().getToken()).build().execute(handler);
    }
}
