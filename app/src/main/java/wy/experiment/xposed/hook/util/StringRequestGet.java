package wy.experiment.xposed.hook.util;

import android.support.annotation.Nullable;

import com.alibaba.fastjson.JSONException;
import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.NetworkResponse;
import com.android.volley.ParseError;
import com.android.volley.Response;
import com.android.volley.toolbox.HttpHeaderParser;
import com.android.volley.toolbox.JsonRequest;

import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;

public class StringRequestGet extends JsonRequest<String> {
    private Map<String, String> headers = new HashMap<>();

    public StringRequestGet(String url, Response.Listener<String> listener, @Nullable Response.ErrorListener errorListener) {
        super(Method.GET, url, null, listener, errorListener);
        setRetryPolicy(new DefaultRetryPolicy(5000, 0, 0));
    }

    public StringRequestGet addHeaders(String key, String value) {
        headers.put(key, value);
        return this;
    }

    /**
     * 这个方法可以不用继承修改的哈
     *
     * @return
     * @throws AuthFailureError
     */
    @Override
    public Map<String, String> getHeaders() throws AuthFailureError {
        addHeaders("token", StrEncode
                .encoderByDES(System.currentTimeMillis() + "|" + getUrl()
                        , Configer.getInstance().getToken()));
        headers.putAll(super.getHeaders());
        return headers;
    }

    @Override
    protected Response<String> parseNetworkResponse(NetworkResponse response) {
        try {
            String jsonString = new String(response.data,
                    HttpHeaderParser.parseCharset(response.headers, PROTOCOL_CHARSET));
            return Response.success(jsonString, HttpHeaderParser.parseCacheHeaders(response));
        } catch (UnsupportedEncodingException e) {
            return Response.error(new ParseError(e));
        } catch (JSONException je) {
            return Response.error(new ParseError(je));
        }
    }
}
