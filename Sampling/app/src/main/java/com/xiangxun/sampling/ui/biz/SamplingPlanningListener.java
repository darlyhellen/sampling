package com.xiangxun.sampling.ui.biz;

import android.app.Dialog;

import com.google.gson.JsonSyntaxException;
import com.xiangxun.sampling.base.FrameListener;
import com.xiangxun.sampling.base.FramePresenter;
import com.xiangxun.sampling.base.FrameView;
import com.xiangxun.sampling.base.XiangXunApplication;
import com.xiangxun.sampling.bean.PlannningData.ResultData;
import com.xiangxun.sampling.bean.PlannningData.Scheme;
import com.xiangxun.sampling.common.SharePreferHelp;
import com.xiangxun.sampling.common.Utils;
import com.xiangxun.sampling.common.dlog.DLog;
import com.xiangxun.sampling.common.http.ApiUrl;
import com.xiangxun.sampling.common.http.DcHttpClient;
import com.xiangxun.vollynet.Response;
import com.xiangxun.vollynet.VolleyError;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * @author zhangyh2 LoginUser 下午3:42:16 TODO 首页进入采样计划展示页面，进行数据请求。并对数据进行缓存处理。
 */
public class SamplingPlanningListener implements FramePresenter {
    @Override
    public void onStart(Dialog loading) {

    }

    @Override
    public void onStop(Dialog loading) {

    }

    public void getPlanning(String time, final FrameListener<ResultData> listener) {

        String url = ApiUrl.planning(XiangXunApplication.getInstance());
        Map<String, String> params = new HashMap<String, String>();
        params.put("resTime", time);
        DcHttpClient.getInstance().getWithURL(XiangXunApplication.getInstance(), url, params, new Response.Listener<JSONObject>() {
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

    public interface SamplingPlanningInterface extends FrameView {

        void onLoginSuccess(List<Scheme> info);

        void onLoginFailed();

        void end();
    }


}
