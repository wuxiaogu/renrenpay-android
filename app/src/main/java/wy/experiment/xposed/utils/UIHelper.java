package wy.experiment.xposed.utils;

import android.content.Context;
import android.content.Intent;

import wy.experiment.xposed.activity.LoginActivity;
import wy.experiment.xposed.activity.MainActivity;
import wy.experiment.xposed.activity.SelectAppActivity;


/**
 * Created by chenxinyou on 2019/2/26.
 */

public class UIHelper {
    public static void showMainView(Context context) {
        Intent intent = new Intent(context, MainActivity.class);
        context.startActivity(intent);
    }
    public static void showLoginView(Context context) {
        Intent intent = new Intent(context, LoginActivity.class);
        context.startActivity(intent);
    }
    public static void showSelectActivity(Context context) {
        Intent intent = new Intent(context, SelectAppActivity.class);
        context.startActivity(intent);
    }
}
