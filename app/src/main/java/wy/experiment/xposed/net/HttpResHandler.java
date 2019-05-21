package wy.experiment.xposed.net;

import com.zhy.http.okhttp.callback.StringCallback;

import okhttp3.Call;
import wy.experiment.xposed.db.ApiResponse;

/**
 * Created by chenxinyou on 2019/3/7.
 */

public abstract class HttpResHandler  extends StringCallback {
    @Override
    public void onError(Call call, Exception e, int id) {
        onError(e.toString());
    }

    @Override
    public void onResponse(String response, int id) {
        if (response == null) {
            onFailure(999, "Server not response!");
        } else {
            ApiResponse res = ApiResponse.getApiResponse(response);
            if (res == null) {
                onFailure(999, "Server response can not be resolved!");
                return;
            } else if (res.getCode() == 200) {
                onSuccess(res);
            }  else {
                onFailure(res.getCode(), res.getMessage());
            }
        }
    }

    @Override
    public void onAfter(int id) {
        super.onAfter(id);
        onFinish();
    }

    public abstract void onFailure(int errorCode, String data);

    public abstract void onSuccess(ApiResponse res);

    public void onError(String msg) {
    }

    public void onFinish() {
    }
}
