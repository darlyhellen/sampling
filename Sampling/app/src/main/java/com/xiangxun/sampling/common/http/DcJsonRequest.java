package com.xiangxun.sampling.common.http;

import android.content.Context;


import com.xiangxun.sampling.common.dlog.DLog;
import com.xiangxun.vollynet.AuthFailureError;
import com.xiangxun.vollynet.DefaultRetryPolicy;
import com.xiangxun.vollynet.NetworkResponse;
import com.xiangxun.vollynet.ParseError;
import com.xiangxun.vollynet.Request;
import com.xiangxun.vollynet.Response;
import com.xiangxun.vollynet.VolleyError;
import com.xiangxun.vollynet.toolbox.HttpHeaderParser;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.Map;

/**
 * @package: com.huatek.api.common
 * @ClassName: DcJsonRequest
 * @Description: request请求类
 * @author: aaron_han
 * @data: 2015-1-15 下午4:55:46
 */
public class DcJsonRequest extends Request<JSONObject> {
    private final Response.Listener<JSONObject> mListener;
    private final Map<String, String> mParams;
    private Response.ErrorListener mErrorListener;
    private String mUrl;

    public DcJsonRequest(Context context, int method, String url, Map<String, String> params, Response.Listener<JSONObject> listener, Response.ErrorListener errorListener) {
        super(method, url, errorListener);
        DLog.e(url);
        mUrl = url;
        mErrorListener = errorListener;
        mListener = listener;
        mParams = params;
        setRetryPolicy(new DefaultRetryPolicy(10 * 1000, 1, 1.0f));// 设置重试超时10秒
        setTag(context);// 设置url为标记

    }

    @Override
    protected Map<String, String> getParams() throws AuthFailureError {
        return mParams;
    }

    @Override
    protected Response<JSONObject> parseNetworkResponse(NetworkResponse response) {
        try {
            String jsonString = new String(response.data, HttpHeaderParser.parseCharset(response.headers));
            return Response.success(new JSONObject(jsonString), shouldCache() ? HttpHeaderIgnoreParser.parseIgnoreCacheHeaders(response) : HttpHeaderParser.parseCacheHeaders(response));
        } catch (UnsupportedEncodingException e) {
            return Response.error(new ParseError(e));
        } catch (JSONException je) {
            je.printStackTrace();
            return Response.error(new ParseError(je));
        }
    }

    @Override
    protected void deliverResponse(JSONObject response) {
        if (shouldCache()) {
            if (response.optInt("result") == 0) {
                mListener.onResponse(response);
            }
        } else {
            mListener.onResponse(response);
        }
    }

    @Override
    public void deliverError(VolleyError error) {
        DLog.e(error.getMessage() + "\n" + "**url**" + mUrl);
        if (mErrorListener != null) {
            mErrorListener.onErrorResponse(error);
        }
    }

}
