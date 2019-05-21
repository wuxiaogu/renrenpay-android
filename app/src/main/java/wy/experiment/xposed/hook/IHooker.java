package wy.experiment.xposed.hook;

import android.content.Intent;

public interface IHooker {
    void hookCreatQr() throws Error, Exception;

    /**
     * 最先执行的HOOK，可以不用实现。
     */
    void hookFirst() throws Error, Exception;

    void hookBill() throws Error, Exception;

    /**
     * 添加远端被HOOK程序要执行的任务列表
     */
    void addRemoteTaskI();


    /**
     * 本程序收到广播后的操作任务列表
     */
    void addLocalTaskI();


    /**
     * 根据广播，在别HOOK程序领空要做的事情
     *
     * @param intent
     * @throws Error
     * @throws Exception
     */
    void doOnOtherApp(Intent intent) throws Error, Exception;


    /**
     * 这个是首选注册到被HOOK进程的广播的Action字符串
     *
     * @return
     */
    String getRemoteAction();

    /**
     * 远程app获取二维码操作
     *
     * @return
     */
    String getRemoteQrActionType();

    /**
     * 远程app去订单查询操作
     *
     * @return
     */
    String getRemoteBillActionType();

    /**
     * 本地APP收到广播的Action字符串
     *
     * @return
     */
    String getLocalQrActionType();


    /**
     * 本地收到订单消息的Action字符串
     *
     * @return
     */
    String getLocalBillActionType();




    /**
     * 返回包名
     *
     * @return
     */
    String getPackPageName();

    /**
     * 返回APP名，可以自己乱定义
     *
     * @return
     */
    String getAppName();
}
