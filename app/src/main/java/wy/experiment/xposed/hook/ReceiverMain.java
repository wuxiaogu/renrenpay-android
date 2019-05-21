package wy.experiment.xposed.hook;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.alibaba.fastjson.JSONObject;

import java.util.HashMap;

import wy.experiment.xposed.bean.EventBus;
import wy.experiment.xposed.bean.LocalEvent;
import wy.experiment.xposed.db.model.Logs;
import wy.experiment.xposed.db.util.LogDao;
import wy.experiment.xposed.utils.XPConstant;
import static wy.experiment.xposed.hook.HookBase.RECV_ACTION_DATE;
import static wy.experiment.xposed.hook.HookBase.RECV_RESULT_TYPE;

public class ReceiverMain extends BroadcastReceiver {
    private static String lastMsg = ""; //防止重启接收广播，一定要用static
    private static final String TAG = "cxyRec";
//    private User user = AppContext.getInstance().getUser();

    //本地广播的任务列表
    public static HashMap<String, CallBackDo> mLocalTaskMap = new HashMap<>();


    @Override
    public void onReceive(Context context, Intent intent) {
        String datas = intent.getStringExtra(RECV_ACTION_DATE);
        int resultType = intent.getIntExtra(RECV_RESULT_TYPE, 0);
        Log.d(TAG, "onReceive -> " + datas);
        if(resultType == 1) {
            try {
                QrBean qrBean = JSONObject.parseObject(datas, QrBean.class);
                int types = intent.getIntExtra("type", 0);

                if (types == 1) {
                    EventBus.getDefault().postInOtherThread(LocalEvent.getEvent(LocalEvent.RECEIVE_ALIPAY_MSG, 0, "", qrBean));
                } else if (types == 2) {
                    EventBus.getDefault().postInOtherThread(LocalEvent.getEvent(LocalEvent.RECEIVE_WECHAT_MSG, 0, "", qrBean));
                } else if (types == 3) {
                    EventBus.getDefault().postInOtherThread(LocalEvent.getEvent(LocalEvent.SEND_WECHAT_MSG, 0, "", qrBean));
                }
            } catch (Error | Exception e) {
                Log.d(TAG, "ReceiverMain错误：" + e.getMessage());
                Log.d(TAG, "ReceiverMain错误：" + e);
                Logs rMain = new Logs("ReceiverMain错误: " + e.getMessage(), System.currentTimeMillis(), XPConstant.ERROR);
                LogDao.add(rMain);
            }
        } else {
            EventBus.getDefault().postInOtherThread(LocalEvent.getEvent(LocalEvent.HOOK_MESSAGE, datas));
        }
    }
}
