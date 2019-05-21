package wy.experiment.xposed.tool;

import android.widget.Toast;

import wy.experiment.xposed.view.AppContext;


/**
 * Created by chenxinyou on 2019/2/26.
 */

public class ToastTool {
    private static Toast mToast;

    private ToastTool() {
    }

    public static void showToastLong(String text) {
        if (mToast == null)
            mToast = Toast.makeText(AppContext.getInstance(), "", Toast.LENGTH_SHORT);

        mToast.setText(text);
        mToast.show();
    }

    public static void showToast(String text) {
        if (mToast == null)
            mToast = Toast.makeText(AppContext.getInstance(), "", Toast.LENGTH_SHORT);

        mToast.setText(text);
        mToast.show();
    }

    public static void showToast(int text) {
        if (mToast == null)
            mToast = Toast.makeText(AppContext.getInstance(), "", Toast.LENGTH_SHORT);

        mToast.setText(text);
        mToast.show();
    }

    public static void showToastSystem(String msg) {

        Toast.makeText(AppContext.getInstance(), msg, Toast.LENGTH_SHORT).show();
    }

    public void cancelToast() {
        if (mToast != null) {
            mToast.cancel();
            mToast = null;
        }
    }
}
