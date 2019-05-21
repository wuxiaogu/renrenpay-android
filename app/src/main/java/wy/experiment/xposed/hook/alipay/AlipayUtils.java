package wy.experiment.xposed.hook.alipay;

import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.android.volley.Response;
import com.android.volley.VolleyError;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import de.robv.android.xposed.XposedHelpers;
import wy.experiment.xposed.hook.ApiBll;
import wy.experiment.xposed.hook.QrBean;
import wy.experiment.xposed.hook.StrUtils;
import wy.experiment.xposed.hook.util.AliBillList;
import wy.experiment.xposed.hook.util.LogUtils;
import wy.experiment.xposed.hook.util.SaveUtils;
import wy.experiment.xposed.hook.util.StringRequestGet;
import wy.experiment.xposed.view.AppContext;

public class AlipayUtils {
    //软件首次启动后，只处理支付最近xxx秒的订单，默认为只处理最近2小时的订单
    private final static int ALIPAY_BILL_TIME = 7200 * 1000;

    public static String mStrCookies = "";

    //上次查订单失败的时间,0表示没有失败
    public static long mLastFail = 0;


    public static String getAlipayCookieStr2(ClassLoader appClassLoader) {
        String cookieStr = "";
        XposedHelpers.callStaticMethod(XposedHelpers.findClass(
                "com.alipay.mobile.common.transportext.biz.appevent.AmnetUserInfo", appClassLoader), "getSessionid");
        Context context = (Context) XposedHelpers.callStaticMethod(XposedHelpers.findClass(
                "com.alipay.mobile.common.transportext.biz.shared.ExtTransportEnv", appClassLoader), "getAppContext");
        if (context != null) {
            Object readSettingServerUrl = XposedHelpers.callStaticMethod(
                    XposedHelpers.findClass("com.alipay.mobile.common.helper.ReadSettingServerUrl", appClassLoader),
                    "getInstance");
            if (readSettingServerUrl != null) {
                String gWFURL = ".alipay.com";
                cookieStr = (String) XposedHelpers.callStaticMethod(XposedHelpers
                                .findClass("com.alipay.mobile.common.transport.http.GwCookieCacheHelper", appClassLoader),
                        "getCookie", gWFURL);
            } else {
                LogUtils.show("支付宝订单C获取异常");
            }
        } else {
            LogUtils.show("支付宝订单C获取异常2");
        }
        return cookieStr;
    }

    private static long mLastQuery = 0;


    /**
     * 询问是否可以去查订单
     * 每10秒查一次单，或者上次查完了也可以查单，低于10秒，就加入列表
     * 如果可以就清除以前所有cookies列表，然后去查单
     *
     * @param cookies
     * @return 返回是否可以去查单
     */
    private static synchronized boolean taskDeal(String cookies) {
        SaveUtils saveUtils = new SaveUtils();
        if (System.currentTimeMillis() - mLastQuery < 5000) {
            List<String> notifyList = saveUtils.getJsonArray(SaveUtils.NOTIFY_LIST, String.class);
            if (notifyList == null) {
                notifyList = new ArrayList<>();
            }
            notifyList.add(cookies);
            saveUtils.putJson(SaveUtils.NOTIFY_LIST, notifyList).commit();
            return false;
        }
        saveUtils.putJson(SaveUtils.NOTIFY_LIST, null).commit();
        return true;
    }

    /**
     * 通过网络请求获取最近的20个订单号
     * 把最近xx分钟内的订单传号传给getAlipayTradeDetail函数处理
     *
     * @param context
     */
    public static void dealAlipayWebTrade(final Context context) {
        if (TextUtils.isEmpty(mStrCookies)) {
            LogUtils.show("C空返回1");
            return;
        }
        if (!taskDeal(mStrCookies)) {
            return;
        }

        long l = System.currentTimeMillis() + 200000;//怕手机的时间比支付宝慢了点，刚产生的订单就无法获取到
        String getUrl = "https://mbillexprod.alipay.com/enterprise/simpleTradeOrderQuery.json?beginTime=" + (l - 864000000L)
                + "&limitTime=" + l + "&pageSize=20&pageNum=1&channelType=ALL";
        StringRequestGet request = new StringRequestGet(getUrl, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                boolean succ = false;
                try {
                    JSONObject jsonObject = JSON.parseObject(response);
                    List<AliBillList> aliBillLists = jsonObject.getJSONObject("result")
                            .getJSONArray("list").toJavaList(AliBillList.class);

                    SaveUtils saveUtils = new SaveUtils();
                    List<String> list = saveUtils.getJsonArray(SaveUtils.BILL_LIST_LAST, String.class);
                    if (list == null) {
                        list = new ArrayList<>();
                    }
                    List<String> errlist = errTaskDealMain("", false);
                    succ = true;
                    for (AliBillList aliBillList : aliBillLists) {
                        //20分钟前的订单就忽略
                        if (System.currentTimeMillis() - aliBillList.getGmtCreateStamp().getTime() > ALIPAY_BILL_TIME) {
                            break;
                        }
                        //首次，或者上次一样，就返回
                        if (list.contains(aliBillList.getTradeNo()) && !errlist.contains(aliBillList.getTradeNo())) {
                            continue;//最新的订单都已经处理过，且没有处理出错，那就直接返回
                        }

                        list.add(aliBillList.getTradeNo());
                        getAlipayTradeDetail(context,
                                aliBillList.getTradeNo(), StrUtils.formatMoneyToCent(aliBillList.getTotalAmount() + ""),
                                mStrCookies);
                    }
                    if (list.size() > 100) {
                        list.subList(0, 50).clear();
                    }
                    saveUtils.putJson(SaveUtils.BILL_LIST_LAST, list).commit();

                } catch (Exception e) {
                    if (!succ) {//如果方案一没有成果，调用方案二。
                        dealAlipayWebTradeNew(context);
                    }
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                LogUtils.show("支付宝：--->请不要设置代理" + error.getMessage());
            }
        });

        String dataNow = new SimpleDateFormat("yyyy-MM-dd").format(new Date(l));
        String dataLastDay = new SimpleDateFormat("yyyy-MM-dd").format(new Date(l - 864000000L));

        request.addHeaders("Cookie", mStrCookies)
                .addHeaders("Referer", "https://render.alipay.com/p/z/merchant-mgnt/simple-order.html?source=mdb_new_card"
                        + dataLastDay + "&endTime=" + dataNow + "&fromBill=true&channelType=ALL");
        ApiBll.getInstance().getmQueue().add(request);
    }

    private static void dealAlipayWebTradeNew(final Context context) {
        if (TextUtils.isEmpty(mStrCookies)) {
            LogUtils.show("C空返回2");
            return;
        }
        if (!taskDeal(mStrCookies)) {
            return;
        }
        mLastQuery = System.currentTimeMillis();
        String getUrl = "https://mbillexprod.alipay.com/enterprise/walletTradeList.json?lastTradeNo=&lastDate=&pageSize=20&shopId=&_inputcharset=gbk&ctoken&source=mdb_new_card&_callback=jsonp30&_input_charset=utf-8&_ksTS=" + System.currentTimeMillis() + "_29";
        StringRequestGet request = new StringRequestGet(getUrl, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jsonObject = JSON.parseObject("{\"result" + StrUtils.getMidText(response, "30({\"result", "\"})") + "\"}");
                    List<AliBillList> aliBillLists = jsonObject.getJSONObject("result")
                            .getJSONArray("list").toJavaList(AliBillList.class);

                    SaveUtils saveUtils = new SaveUtils();
                    List<String> list = saveUtils.getJsonArray(SaveUtils.BILL_LIST_LAST, String.class);
                    if (list == null) {
                        list = new ArrayList<>();
                    }
                    List<String> errlist = errTaskDealMain("", false);

                    for (AliBillList aliBillList : aliBillLists) {
                        //20分钟前的订单就忽略
                        if (System.currentTimeMillis() - aliBillList.getDateKey().getTime() > ALIPAY_BILL_TIME) {
                            break;
                        }
                        //首次，或者上次一样，就返回
                        if (list.contains(aliBillList.getTradeNo()) && !errlist.contains(aliBillList.getTradeNo())) {
                            continue;//最新的订单都已经处理过，且没有处理出错，那就直接返回
                        }

                        list.add(aliBillList.getTradeNo());
                        getAlipayTradeDetail(context,
                                aliBillList.getTradeNo(), StrUtils.formatMoneyToCent(aliBillList.getTradeTransAmount() + ""),
                                mStrCookies);
                    }
                    if (list.size() > 100) {
                        list.subList(0, 50).clear();
                    }
                    saveUtils.putJson(SaveUtils.BILL_LIST_LAST, list).commit();
                } catch (Exception e) {
                    LogUtils.show("支付宝列表错误" + mStrCookies + response);
                    Intent intent1 = AppContext.getInstance().getPackageManager().getLaunchIntentForPackage(AlipayHook.getInstance().getPackPageName());
                    AppContext.getInstance().startActivity(intent1);
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                LogUtils.show("支付宝：--->请不要设置代理" + error.getMessage());
            }
        });

        request.addHeaders("Cookie", mStrCookies).addHeaders("Referer", "https://render.alipay.com/p/z/merchant-mgnt/simple-order.html?source=mdb_new_card");
        ApiBll.getInstance().getmQueue().add(request);
    }


    /**
     * 获取指定订单号的订单信息，如果是已收款状态，则发送给服务器，
     * 失败的会自动加数据库以后补发送。
     *
     * @param context
     * @param tradeNo
     * @param money   单位为分
     * @param cookies
     */
    private static void getAlipayTradeDetail(Context context, final String tradeNo, final int money, String cookies) {
        String getUrl = "https://tradeeportlet.alipay.com/wireless/tradeDetail.htm?tradeNo=" + tradeNo + "&source=channel&_from_url=https%3A%2F%2Frender.alipay.com%2Fp%2Fz%2Fmerchant-mgnt%2Fsimple-order._h_t_m_l_%3Fsource%3Dmdb_card";
        StringRequestGet request = new StringRequestGet(getUrl, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    String html = response.toLowerCase();
                    html = html.replace(" ", "")
                            .replace("\r", "")
                            .replace("\n", "")
                            .replace("\t", "");
                    html = StrUtils.getMidText(html, "\"id=\"j_logourl\"/>", "j_maskcode\"class=\"maskcodemain\"");

                    String tmp;
                    QrBean qrBean = new QrBean();
                    qrBean.setChannel(QrBean.ALIPAY);
                    qrBean.setOrder_id(tradeNo);

                    tmp = StrUtils.getMidText(html, "<divclass=\"am-flexbox\">当前状态</div>", "<divclass=\"am-list-itemtrade-info-item\">");
                    qrBean.setMark_buy(StrUtils.getMidText(tmp, "<divclass=\"trade-info-value\">", "</div>"));

                    tmp = StrUtils.getMidText(html, "<divclass=\"am-flexbox-item\">说</div><divclass=\"am-flexbox-item\">明", "<divclass=\"am-list-itemtrade-info-item\">");
                    qrBean.setMark_sell(StrUtils.getMidText(tmp, "<divclass=\"trade-info-value\">", "</div"));

                    //tmp = getMidText(html, "am-flexbox-item\">金</div><divclass=\"am-flexbox-item\">额", "<divclass=\"am-list-itemtrade-info-item\">");
                    //Float money = Float.valueOf(getMidText(tmp, "<divclass=\"trade-info-value\">", "</div")) * 100;
                    qrBean.setMoney(money);

                    if (TextUtils.isEmpty(qrBean.getMark_sell())
                            || !qrBean.getMark_buy().contentEquals("已收款")) {
                        return;
                    }

                    mLastFail = 0;
                    ApiBll.getInstance().payQR(qrBean);
                    LogUtils.show("支付宝支付成功任务：" + qrBean.getMark_sell() + "|" + qrBean.getMoney() + "|" + tradeNo);
                    errTaskDealMain(tradeNo, true);
                } catch (Exception ignore) {
                    errTaskDealMain(tradeNo, false);
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                errTaskDealMain(tradeNo, false);
                LogUtils.show("支付宝订单详情获取错误：" + tradeNo + "-->" + error.getMessage());
            }
        });

        request.addHeaders("Cookie", cookies);
        ApiBll.getInstance().getmQueue().add(request);
    }

    /**
     * 添加删除以前失败的订单
     *
     * @param tradeNo
     * @param isremove
     * @return 返回的不可能是null
     */
    private static synchronized List<String> errTaskDealMain(String tradeNo, Boolean isremove) {
        SaveUtils saveUtils = new SaveUtils();
        List<String> list = saveUtils.getJsonArray(SaveUtils.BILL_LIST_ERR, String.class);
        if (list == null) {
            list = new ArrayList<>();
        }
        if (!TextUtils.isEmpty(tradeNo)) {
            if (isremove) {
                list.remove(tradeNo);
            } else {
                if (list.size() > 50) {
                    list.subList(0, 20).clear();
                }
                if (!list.contains(tradeNo)) {
                    list.add(tradeNo);
                }
            }
            saveUtils.putJson(SaveUtils.BILL_LIST_ERR, list).commit();
        }
        return list;
    }


    //获取用户id
    public static String getUserId(ClassLoader classLoader) {
        try {
            String LogonId = (String) XposedHelpers.callMethod(XposedHelpers.callMethod(XposedHelpers.callMethod(XposedHelpers
                            .callMethod(XposedHelpers.callStaticMethod(classLoader.loadClass("com.alipay.mobile.framework.AlipayApplication"),
                                    "getInstance", new Object[0]), "getMicroApplicationContext", new Object[0]),
                    "getExtServiceByInterface", new Object[]{"com.alipay.mobile.framework.service.ext.security.AuthService"}),
                    "getUserInfo", new Object[0]), "getUserId", new Object[0]);
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("UserId=");
            stringBuilder.append(LogonId);
            return LogonId;
        } catch (Exception e) {
            return null;
        }
    }
}
