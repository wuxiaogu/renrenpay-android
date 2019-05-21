package wy.experiment.xposed.hook;

import android.content.Intent;

public interface CallBackDo {
    void callBack(Intent intent) throws Error, Exception;
}
