package wy.experiment.xposed.hook.util;

import android.util.Log;

import de.robv.android.xposed.XposedBridge;

public class LogUtils {
    public static void show(String tips) {
        try {
            XposedBridge.log(tips);
        } catch (NoClassDefFoundError ignore) {

        }
        Log.e("LogUtils", getFunctionName() + tips);
    }

    private static String getFunctionName() {
        StackTraceElement[] stackTrace = Thread.currentThread().getStackTrace();
        if (stackTrace != null) {
            for (StackTraceElement stackTraceElement : stackTrace) {
                if (!stackTraceElement.isNativeMethod() && !stackTraceElement.getClassName().equals(Thread.class.getName())
                        && !stackTraceElement.getFileName().contentEquals("LogUtils.java")) {
                    return "[ " + Thread.currentThread().getName() + ": " + stackTraceElement.getFileName()
                            + ":" + stackTraceElement.getLineNumber() + " " + stackTraceElement.getMethodName() + " ]";
                }
            }
        }
        return null;
    }
}
