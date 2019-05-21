package wy.experiment.xposed.bean;

public class MessageEvent {

    public static int RECEIVE_ALIPAY_MSG = 1005;
    public static int RECEIVE_WECHAT_MSG = 1006;
    public static int SEND_WECHAT_MSG = 1007;
    public static int RUN_TASK_LOG = 1008;

    public static int HOOK_MESSAGE = 2000;

    private int eventType;
    private Object eventData;
    private String eventMsg;
    private int eventValue;


    public MessageEvent(int type) {
        this.eventType = type;
    }

    public MessageEvent(int type, String message) {
        this.eventType = type;
        this.eventMsg = message;
    }

    public MessageEvent(int type, int value) {
        this.eventType = type;
        this.eventValue = value;
    }

    public MessageEvent(int type, int value, String message, Object data) {
        this.eventType = type;
        this.eventMsg = message;
        this.eventValue = value;
        this.eventData = data;
    }

    public int getEventType() {
        return eventType;
    }

    public Object getEventData() {
        return eventData;
    }

    public String getEventMsg() {
        return eventMsg;
    }

    public int getEventValue() {
        return eventValue;
    }
}
