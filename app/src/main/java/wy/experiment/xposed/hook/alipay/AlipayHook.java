package wy.experiment.xposed.hook.alipay;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.WindowManager;
import android.widget.Button;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;

import de.robv.android.xposed.XC_MethodHook;
import de.robv.android.xposed.XC_MethodReplacement;
import de.robv.android.xposed.XposedBridge;
import de.robv.android.xposed.XposedHelpers;
import wy.experiment.xposed.hook.ApiBll;
import wy.experiment.xposed.hook.CallBackDo;
import wy.experiment.xposed.hook.HookBase;
import wy.experiment.xposed.hook.QrBean;
import wy.experiment.xposed.hook.StrUtils;
import wy.experiment.xposed.hook.util.LogUtils;
import wy.experiment.xposed.hook.util.ReflecUtils;
import wy.experiment.xposed.view.AppContext;

public class AlipayHook extends HookBase {
    private static AlipayHook mHookAlipay;
    public String COOK_RECV = getClass().getSimpleName() + ".COOK_RECV";
    public String mCookies = "";
    public static String TAG = "cxyAli";

    public static synchronized AlipayHook getInstance() {
        if (mHookAlipay == null) {
            mHookAlipay = new AlipayHook();
        }
        return mHookAlipay;
    }

    @Override
    public void hookFirst() throws Error, Exception {

        hookSafe();

        //HOOK帐号
        hookUserInfo();

        //hook金额
        hookMoney();

        //hook窗口
        hookQRWindows();

    }

    /**
     * hook掉安全校验
     */
    private void hookSafe() {
        try {
            Class<?> ScanAttack = XposedHelpers.findClass("com.alipay.apmobilesecuritysdk.scanattack.common.ScanAttack", mAppClassLoader);
            XposedHelpers.findAndHookMethod(ScanAttack, "getScanAttackInfo", new XC_MethodReplacement() {
                @Override
                protected Object replaceHookedMethod(MethodHookParam param) throws Throwable {
                    return null;
                }
            });
        } catch (Error | Exception e) {
            LogUtils.show(getAppName() + "first");
        }
    }

    @Override
    public void hookCreatQr() throws Error, Exception {
        XposedHelpers.findAndHookMethod("com.alipay.mobile.payee.ui.PayeeQRSetMoneyActivity", mAppClassLoader, "a",
                XposedHelpers.findClass("com.alipay.transferprod.rpc.result.ConsultSetAmountRes", mAppClassLoader), new XC_MethodHook() {
                    @Override
                    protected void afterHookedMethod(MethodHookParam param) throws Throwable {
                        try {
                            String money = (String) ReflecUtils.findField(param.thisObject.getClass(), String.class
                                    , 0, false).get(param.thisObject);
                            String payurl = (String) XposedHelpers.findField(param.args[0].getClass(),
                                    "qrCodeUrl").get(param.args[0]);
                            Class<?> auinputbox = XposedHelpers.findClass("com.alipay.mobile.antui.input.AUInputBox", mAppClassLoader);
                            Object markObject = ReflecUtils.findField(param.thisObject.getClass(), auinputbox
                                    , 1, false).get(param.thisObject);
                            String mark = (String) XposedHelpers.callMethod(markObject, "getUbbStr");
                            Log.d(TAG, "支付宝生成二维码：" + money + "|" + mark + "|" + payurl);
                        } catch (Error | Exception ignore) {
                            Log.d(TAG, "hookCreatQr error -> " + ignore.getMessage());
                        }
                    }
                });
    }

    @Override
    public void hookBill() throws Error, Exception {
        Class<?> insertTradeMessageInfo = XposedHelpers.findClass("com.alipay.android.phone.messageboxstatic.biz.dao.TradeDao", mAppClassLoader);
        XposedBridge.hookAllMethods(insertTradeMessageInfo, "insertMessageInfo", new XC_MethodHook() {
            @Override
            protected void beforeHookedMethod(MethodHookParam param) throws Throwable {
                super.beforeHookedMethod(param);
                try {
                    Object object = param.args[0];
                    String MessageInfo = (String) XposedHelpers.callMethod(object, "toString");
                    String content = StrUtils.getMidText(MessageInfo, "content='", "'");
                    Log.d(TAG, content);
                    if (content.contains("收到一笔转账")) {
                        JSONObject jsonObject = JSON.parseObject(content);
                        String mark = jsonObject.getString("assistMsg2");
                        String money = jsonObject.getString("content");
                        money = money.replace("￥", "").replace(" ", "");
                        String tradeNo = StrUtils.getMidText(MessageInfo, "tradeNO=", "&");
                        QrBean qrBean = new QrBean();
                        qrBean.setOrder_id(tradeNo);
                        qrBean.setMoney(StrUtils.formatMoneyToCent(money));
                        qrBean.setMark_sell(mark);
                        qrBean.setChannel(QrBean.ALIPAY);
                        Intent broadCastIntent = new Intent(RECV_ACTION);
                        broadCastIntent.putExtra(RECV_ACTION_DATE, qrBean.toString());
                        broadCastIntent.putExtra(RECV_ACTION_TYPE, getLocalBillActionType());
                        broadCastIntent.putExtra(RECV_RESULT_TYPE, 1);
                        broadCastIntent.putExtra("type", 1);
                        mContext.sendBroadcast(broadCastIntent);
                    }
                } catch (Exception e) {
                    Log.e(TAG, "支付宝订单获取错误");
                    Intent broadCastIntent1 = new Intent(RECV_ACTION);
                    broadCastIntent1.putExtra(RECV_ACTION_DATE, "支付宝订单获取错误");
                    broadCastIntent1.putExtra(RECV_ACTION_TYPE, getLocalBillActionType());
                    broadCastIntent1.putExtra(RECV_RESULT_TYPE, 2);
                    mContext.sendBroadcast(broadCastIntent1);
                }
            }
        });
    }

    @Override
    public void addLocalTaskI() {
        super.addLocalTaskI();
        addLocalTask(COOK_RECV, new CallBackDo() {
            @Override
            public void callBack(Intent intent) throws Error, Exception {
                mCookies = intent.getStringExtra(RECV_ACTION_DATE);
            }
        });

        addLocalTask(getLocalInfoActionType(), new CallBackDo() {
            @Override
            public void callBack(Intent intent) throws Error, Exception {
                String data = intent.getStringExtra(RECV_ACTION_DATE);
                LogUtils.show("支付宝帐号：" + (TextUtils.isEmpty(data) ? "未登录" : data));
            }
        });

        addLocalTask(getLocalBillActionType(), new CallBackDo() {
            @Override
            public void callBack(Intent intent) throws Error, Exception {
                String data = intent.getStringExtra(RECV_ACTION_DATE);
                //LogUtils.show("支付宝数据：" + data);
                if (data == null || !data.startsWith("{")) {
                    AlipayUtils.mLastFail = System.currentTimeMillis();
                    LogUtils.show("查询支付宝查订单");
                    AlipayUtils.mStrCookies = TextUtils.isEmpty(data) ? mCookies : data;
                    AlipayUtils.dealAlipayWebTrade(AppContext.getInstance());
                } else {
                    QrBean qrBean = JSON.parseObject(data, QrBean.class);
                    ApiBll.getInstance().payQR(qrBean);
                }
            }
        });
    }

    @Override
    public void addRemoteTaskI() {
        addRemoteTask(getRemoteQrActionType(), new CallBackDo() {
            @Override
            public void callBack(Intent intent) throws Error, Exception {
                LogUtils.show("获取支付宝二维码");
                QrBean qrBean = JSON.parseObject(intent.getStringExtra(RECV_ACTION_DATE), QrBean.class);

                Intent intent2 = new Intent(mContext, XposedHelpers.findClass("com.alipay.mobile.payee.ui.PayeeQRSetMoneyActivity", mAppClassLoader));
                intent2.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                intent2.putExtra("mark", qrBean.getMark_sell());
                intent2.putExtra("money", qrBean.getMoneyStr());
                mContext.startActivity(intent2);
            }
        });
    }

    @Override
    public String getPackPageName() {
        return "com.eg.android.AlipayGphone";
    }

    @Override
    public String getAppName() {
        return "支付宝";
    }

    /**
     * 开始Hook二维码创建窗口，目的是为了创建生成二维码
     *
     * @throws Exception
     */
    private void hookQRWindows() {
        XposedHelpers.findAndHookMethod("com.alipay.mobile.payee.ui.PayeeQRSetMoneyActivity"
                , mAppClassLoader, "onCreate", Bundle.class, new XC_MethodHook() {
                    @Override
                    protected void afterHookedMethod(MethodHookParam param) throws Throwable {
                        LogUtils.show("Hook到支付宝窗口");
                        try {
                            ((Activity) param.thisObject).getWindow().addFlags(WindowManager.LayoutParams.FLAG_SHOW_WHEN_LOCKED
                                    | WindowManager.LayoutParams.FLAG_DISMISS_KEYGUARD);
                            Intent intent = ((Activity) param.thisObject).getIntent();
                            String mark = intent.getStringExtra("mark");
                            String money = intent.getStringExtra("money");
                            if (TextUtils.isEmpty(mark) || mark.startsWith("test")) {
                                return;
                            }

                            //下面两行其实可以不要的
                            ReflecUtils.findField(param.thisObject.getClass(), String.class
                                    , 0, false).set(param.thisObject, money);

                            Class<?> auinputbox = XposedHelpers.findClass("com.alipay.mobile.antui.input.AUInputBox", mAppClassLoader);
                            Object jinErView = ReflecUtils.findField(param.thisObject.getClass(), auinputbox
                                    , 0, false).get(param.thisObject);
                            Object beiZhuView = ReflecUtils.findField(param.thisObject.getClass(), auinputbox
                                    , 1, false).get(param.thisObject);
                            //设置支付宝金额和备注
                            XposedHelpers.callMethod(jinErView, "setText", money);
                            XposedHelpers.callMethod(beiZhuView, "setText", mark);

                            Class<?> aUButton = XposedHelpers.findClass("com.alipay.mobile.antui.basic.AUButton", mAppClassLoader);
                            ((Button) ReflecUtils.findField(param.thisObject.getClass(), aUButton
                                    , 0, false).get(param.thisObject)).performClick();

                            //点击确认，这个模拟方案本来淘汰了，觉得直接用Call更稳定，但其实performClick也是相当于call，并不是传统模拟

                            //方式二是直接调用函数，直接可以替换上面模拟方案，各有各好吧。
                            //XposedHelpers.callMethod(param.thisObject, "a");
                        } catch (Error | Exception ignore) {
                            Log.e(TAG, "hookQRWindows is error" + ignore);
                        }
                    }
                });
    }

    /**
     * HOOK支付宝信息
     */
    private void hookUserInfo() {
        XposedHelpers.findAndHookMethod("com.alipay.mobile.common.transportext.biz.appevent.AmnetUserInfo"
                , mAppClassLoader
                , "updateUserInfo"
                , String.class, String.class, new XC_MethodHook() {
                    @Override
                    protected void afterHookedMethod(MethodHookParam param) throws Throwable {
                        super.afterHookedMethod(param);
                        try {
                            Intent broadCastIntent2 = new Intent(RECV_ACTION);
                            broadCastIntent2.putExtra(RECV_ACTION_DATE, "ALIPAYJSESSIONID=" + param.args[1]);
                            broadCastIntent2.putExtra(RECV_ACTION_TYPE, COOK_RECV);
                            mContext.sendBroadcast(broadCastIntent2);

//                            String loginid = mContext.getSharedPreferences("com.alipay.android.phone.socialcontact", Context.MODE_PRIVATE)
//                                    .getString("myaccountinfo_" + param.args[0], "");
//                            loginid = loginid.replace(" ", "").toLowerCase();
//                            loginid = StrUtils.getMidText(loginid, "\"loginid\":\"", "\"");

                            Intent broadCastIntent = new Intent(RECV_ACTION);
                            broadCastIntent.putExtra(RECV_ACTION_DATE, param.args[0] + "");
                            broadCastIntent.putExtra(RECV_ACTION_TYPE, getLocalInfoActionType());
                            mContext.sendBroadcast(broadCastIntent);
                        } catch (Error | Exception ignore) {
                            Log.e(TAG, "hookUserInfo is error" + ignore);
                        }
                    }
                });
    }

    private void hookMoney() {
        XposedHelpers.findAndHookMethod("com.alipay.mobile.framework.service.ext.dbhelper.SecurityDbHelper",
                mAppClassLoader, "addOrUpdateUserLogin",
                XposedHelpers.findClass("com.alipay.mobile.framework.service.ext.security.bean.UserInfo", mAppClassLoader),
                new XC_MethodHook() {
                    @Override
                    protected void beforeHookedMethod(MethodHookParam param) throws Throwable {
                        super.beforeHookedMethod(param);
                        if (param.args[0] == null) {
                            return;
                        }
                        try {
                            Intent broadCastIntent2 = new Intent(RECV_ACTION);
                            broadCastIntent2.putExtra(RECV_ACTION_TYPE, COOK_RECV);
                            broadCastIntent2.putExtra(RECV_ACTION_DATE, "ALIPAYJSESSIONID=" + XposedHelpers.getObjectField(param.args[0], "extern_token"));
                            mContext.sendBroadcast(broadCastIntent2);
                        } catch (Error | Exception e) {
                            Log.e(TAG, "hookMoney is error" + e);
                        }
                    }
                });

    }

    @Override
    public void creatQrTask(Integer money, String mark) {
        Intent broadCastIntent = new Intent(getRemoteAction());
        broadCastIntent.putExtra(RECV_ACTION_TYPE, getRemoteQrActionType());

        QrBean qrBean = new QrBean();
        qrBean.setMoney(money);
        qrBean.setMark_sell(mark);
        broadCastIntent.putExtra(RECV_ACTION_DATE, qrBean.toString());
        AppContext.getInstance().sendBroadcast(broadCastIntent);
    }
}
