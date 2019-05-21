package wy.experiment.xposed.bean;

import android.os.Handler;
import android.util.Log;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import wy.experiment.xposed.tool.ToastTool;


/**
 * Created by chenxinyou on 2019/2/26.
 */

public class EventBus {
    private static EventBus instance = new EventBus();
    private List<EventHandler> handlers = new ArrayList<EventHandler>();
    private Handler uiThreadHandler;

    /**
     * 获取消息处理器
     *
     * @return
     */
    public static EventBus getDefault() {

        return instance;
    }

    private EventBus() {

        uiThreadHandler = new Handler();
    }

    /**
     * 添加关注本地广播
     *
     * @param handler
     */
    public synchronized void add(EventHandler handler) {

        Log.d("mmsg", " EventBus add handler : " + handler);
        handlers.add(handler);
    }

    /**
     * 移除关注本地广播
     *
     * @param handler
     */
    public synchronized void remove(EventHandler handler) {

        Log.d("mmsg", " EventBus remove handler : " + handler);

        Iterator<EventHandler> it = handlers.iterator();

        while (it.hasNext()) {

            EventHandler get = it.next();
            if (get.equals(handler))
                it.remove();
        }
    }

    /**
     * 同步发送一个消息广播
     *
     * @param event
     */
    public synchronized void post(LocalEvent event) {

        Iterator<EventHandler> it = handlers.iterator();

        while (it.hasNext())
            it.next().onEvent(event);
    }

    /**
     * 异步发送一个消息广播
     *
     * @param event
     */
    public void postInOtherThread(final LocalEvent event) {

        uiThreadHandler.post(new Runnable() {

            @Override
            public void run() {
                post(event);
            }
        });
    }

    /**
     * 异步显示一个toast消息
     *
     * @param msg
     */
    public void noticeMsg(final String msg) {

        uiThreadHandler.post(new Runnable() {

            @Override
            public void run() {
                ToastTool.showToast(msg);
            }
        });
    }

    /**
     * 异步执行一个任务
     *
     * @param runnable
     */
    public void runningOnUiThread(Runnable runnable) {

        uiThreadHandler.post(runnable);
    }

    /**
     * 异步执行一个任务，带有延迟
     *
     * @param runnable
     */
    public void runningOnUiThread(Runnable runnable, int delay) {

        uiThreadHandler.postDelayed(runnable, delay);
    }
}
