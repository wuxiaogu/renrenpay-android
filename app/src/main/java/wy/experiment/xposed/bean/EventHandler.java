package wy.experiment.xposed.bean;

/**
 * Created by chenxinyou on 2019/2/26.
 */

public interface EventHandler {
    /**
     * 当接收到消息时回调此方法
     *
     * @param event 发生的事件
     */
    void onEvent(LocalEvent event);
}
