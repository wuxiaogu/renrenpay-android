package wy.experiment.xposed.hook;

import android.text.TextUtils;

import java.text.DecimalFormat;

public class StrUtils {
    /**
     * 获取指定文本的两指定文本之间的文本
     *
     * @param text
     * @param begin
     * @param end
     * @return
     */
    public static String getMidText(String text, String begin, String end) {
        try {
            int b = text.indexOf(begin) + begin.length();
            int e = text.indexOf(end, b);
            return text.substring(b, e);
        } catch (Exception e) {
            e.printStackTrace();
            return "";
        }
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
        String rnt = new DecimalFormat("#.00").format(Float.valueOf(money.trim()) / 100f);
        if (TextUtils.isEmpty(rnt)) {
            rnt = "0.00";
        }
        if (rnt.startsWith(".")) {
            rnt = "0" + rnt;
        }
        return rnt;
    }
}
