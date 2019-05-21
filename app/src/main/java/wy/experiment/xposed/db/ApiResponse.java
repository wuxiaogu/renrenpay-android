package wy.experiment.xposed.db;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONException;

/**
 * Created by chenxinyou on 2019/3/7.
 */

public class ApiResponse {
    private Object data;
    private String message;
    private int code;
    private long serverTime;
    private boolean valid;

    public ApiResponse() {}

    public static ApiResponse getApiResponse(String jsonString) {

        try {
            return JSON.parseObject(jsonString, ApiResponse.class);
        } catch(JSONException e) {
            return null;
        }
    }

    public boolean isValid() {
        return valid;
    }

    public void setValid(boolean valid) {
        this.valid = valid;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public long getServerTime() {
        return serverTime;
    }

    public void setServerTime(long serverTime) {
        this.serverTime = serverTime;
    }

    public Object getData() {
        return data;
    }

    public void setData(Object data) {
        this.data = data;
    }

    @Override
    public String toString() {
        return "data:" + data + " message:" + message + " code:"
                + code;
    }

    public static String getErrorMessage(int errorCode) {
        switch (errorCode) {
            case ERROR_PARAMETER:
                return "参数不合法";
            default:
                return errorCode + "";

        }
    }


    // 参数不合法
    public static final int ERROR_PARAMETER = 61000;
}
