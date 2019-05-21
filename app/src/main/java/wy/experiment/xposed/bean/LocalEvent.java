package wy.experiment.xposed.bean;

/**
 * Created by chenxinyou on 2019/2/26.
 */

public class LocalEvent {

    public static int TEST_NOTIFY = 10001;

    public static int ALIPAY_CODE = 1001;
    public static int WECHART_CODE =  1002;
    public static int GET_ALIPAY_CODE = 1003;
    public static int GET_WECHART_CODE = 1004;
    public static int RECEIVE_ALIPAY_MSG = 1005;
    public static int RECEIVE_WECHAT_MSG = 1006;
    public static int SEND_WECHAT_MSG = 1007;
    public static int RUN_TASK_LOG = 1008;

    public static int HOOK_MESSAGE = 2000;
    public static int NET_WORK_CHANGE = 2001;

    private int eventType;
    private Object eventData;
    private String eventMsg;
    private int eventValue;

    public static LocalEvent getEvent(int eventType) {

        return getEvent(eventType, -1, "", null);
    }

    public static LocalEvent getEvent(int eventType, String eventMsg) {

        return getEvent(eventType, -1, eventMsg, null);
    }

    public static LocalEvent getEvent(int eventType, int eventValue) {

        return getEvent(eventType, eventValue, "", null);
    }

    public static LocalEvent getEvent(int eventType, int eventValue,
                                      String eventMsg) {

        return getEvent(eventType, eventValue, eventMsg, null);
    }

    public static LocalEvent getEvent(int eventType, int eventValue,
                                      String eventMsg, Object eventData) {

        LocalEvent event = new LocalEvent();

        event.eventMsg = eventMsg;
        event.eventType = eventType;
        event.eventData = eventData;
        event.eventValue = eventValue;

        return event;
    }

    public int getEventType() {
        return eventType;
    }

    public void setEventType(int eventType) {
        this.eventType = eventType;
    }

    public Object getEventData() {
        return eventData;
    }

    public void setEventData(Object eventData) {
        this.eventData = eventData;
    }

    public String getEventMsg() {
        return eventMsg;
    }

    public void setEventMsg(String eventMsg) {
        this.eventMsg = eventMsg;
    }

    public int getEventValue() {
        return eventValue;
    }

    public void setEventValue(int eventValue) {
        this.eventValue = eventValue;
    }
}
