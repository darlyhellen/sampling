package com.xiangxun.sampling.ui.biz;

import android.app.Dialog;
import android.text.TextUtils;

import com.google.gson.JsonSyntaxException;
import com.xiangxun.sampling.base.FrameListener;
import com.xiangxun.sampling.base.FramePresenter;
import com.xiangxun.sampling.base.FrameView;
import com.xiangxun.sampling.base.XiangXunApplication;
import com.xiangxun.sampling.bean.PlannningData.ResultData;
import com.xiangxun.sampling.bean.PlannningData.Scheme;
import com.xiangxun.sampling.common.Utils;
import com.xiangxun.sampling.common.dlog.DLog;
import com.xiangxun.sampling.common.http.ApiUrl;
import com.xiangxun.sampling.common.http.DcHttpClient;
import com.xiangxun.vollynet.Response;
import com.xiangxun.vollynet.VolleyError;

import org.json.JSONObject;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;


/**
 * @author zhangyh2 LoginUser 下午3:42:16 TODO  根据前面的方案列表点击获取方案id，根据方案id从服务端获取点位列表
 */
public class SamplingPointListener implements FramePresenter {
    @Override
    public void onStart(Dialog loading) {

    }

    @Override
    public void onStop(Dialog loading) {

    }

    public void postPoint(String id, String strTime, final FrameListener<ResultData> listener) {

        String url = ApiUrl.point(XiangXunApplication.getInstance());
        if (TextUtils.isEmpty(id) || TextUtils.isEmpty(id)) {
            listener.onFaild(0, "方案id不能为空");
            return;
        }
        Map<String, String> params = new LinkedHashMap<String, String>();
        params.put("schemeId", id);
        params.put("strTime", strTime);
        DcHttpClient.getInstance().postWithURL(XiangXunApplication.getInstance(), url, params, new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        if (null != response) {
                            DLog.json(response.toString());
                            try {
                                ResultData resultData = Utils.getGson().fromJson(response.toString(), ResultData.class);
                                listener.onSucces(resultData);
                            } catch (JsonSyntaxException e) {
                                e.printStackTrace();
                                listener.onFaild(0, "解析异常！");
                            }
                        } else {
                            listener.onFaild(0, "登录失败，请检查网络！");
                        }
                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        listener.onFaild(0, "登录失败，请检查网络！");
                    }
                }

        );

    }

    public interface SamplingPointInterface extends FrameView {

        void onLoginSuccess(List<Scheme> info);

        void onLoginFailed(String info);

        void end();
    }


}
