package wy.experiment.xposed.service;

import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Handler;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.util.Log;

import wy.experiment.xposed.bean.EventBus;
import wy.experiment.xposed.bean.LocalEvent;

public class UploadLogService extends Service {
    private Handler handler;
    private Runnable runnable;
    private String TAG = "cxyUpload";

    private ConnectivityManager connectivityManager;
    private NetworkInfo info;

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    private BroadcastReceiver broadcastReceiver = new BroadcastReceiver() {

        @Override
        public void onReceive(Context context, Intent intent) {
            //当网络发生变化时
            String action = intent.getAction();
            if (action.equals(ConnectivityManager.CONNECTIVITY_ACTION)) {
                connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
                info = connectivityManager.getActiveNetworkInfo();
                if (info != null && info.isAvailable()) {
//                    sendNetworkStateBroadCast(ACTION_CONNECTIONED);
                    Log.d(TAG, "有网络链接");
                    EventBus.getDefault().postInOtherThread(LocalEvent.getEvent(LocalEvent.NET_WORK_CHANGE, 1));
                } else {
                    Log.d(TAG, "没有网络链接");
                    EventBus.getDefault().postInOtherThread(LocalEvent.getEvent(LocalEvent.NET_WORK_CHANGE, 0));
                }
            }
        }
    };

    @Override
    public void onCreate() {
        super.onCreate();
        IntentFilter intentFilter=new IntentFilter();
        intentFilter.addAction(ConnectivityManager.CONNECTIVITY_ACTION);
        registerReceiver(broadcastReceiver,intentFilter);
        handler = new Handler();
        runnable = new Runnable() {
            @Override
            public void run() {
                EventBus.getDefault().postInOtherThread(LocalEvent.getEvent(LocalEvent.RUN_TASK_LOG));
                handler.postDelayed(this, 1000 * 60 * 5);
            }
        };
        handler.postDelayed(runnable, 1000 * 30);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        unregisterReceiver(broadcastReceiver);
    }
}
