package wy.experiment.xposed.hook.util;

import java.text.DecimalFormat;

public class PayUtils {
    //软件首次启动后，只处理支付最近xxx秒的订单，默认为只处理最近1小时的订单
    private final static int ALIPAY_BILL_TIME = 3600 * 1000;

    private static PayUtils mPayUtils;

    public synchronized static PayUtils getInstance() {
        if (mPayUtils == null) {
            mPayUtils = new PayUtils();
        }
        return mPayUtils;
    }


    /**
     * 格式化金钱，把元变为分的单位
     *
     * @param money
     * @return
     */
    public static Integer formatMoneyToCent(String money) {
        return Integer.valueOf(new DecimalFormat("#").format(Float.valueOf(money.trim()) * 100));
    }

    /**
     * 格式化金钱，把分变为元的单位
     *
     * @param money
     * @return
     */
    public static String formatMoneyToYuan(String money) {
        String yuan = new DecimalFormat("#.00").format(Float.valueOf(money.trim()) / 100f);
        if (yuan.startsWith(".")) {
            yuan = "0" + yuan;
        }
        return yuan;
    }
}
