package com.xiangxun.sampling.ui.biz;

import android.app.Dialog;
import android.content.Context;
import android.text.TextUtils;

import com.google.gson.JsonSyntaxException;
import com.xiangxun.sampling.base.FrameListener;
import com.xiangxun.sampling.base.FramePresenter;
import com.xiangxun.sampling.base.FrameView;
import com.xiangxun.sampling.base.SystemCfg;
import com.xiangxun.sampling.base.XiangXunApplication;
import com.xiangxun.sampling.bean.PlannningData.Point;
import com.xiangxun.sampling.bean.PlannningData.ResultPointData;
import com.xiangxun.sampling.bean.PlannningData.Scheme;
import com.xiangxun.sampling.common.Utils;
import com.xiangxun.sampling.common.dlog.DLog;
import com.xiangxun.sampling.common.http.ApiUrl;
import com.xiangxun.sampling.common.http.DcHttpClient;
import com.xiangxun.vollynet.Response;
import com.xiangxun.vollynet.VolleyError;

import org.json.JSONObject;

import java.util.LinkedHashMap;
import java.util.Map;


/**
 * @author zhangyh2 LoginUser 下午3:42:16 TODO  新增点位对应解决方案，进行接口对接。
 */
public class AddPointListener implements FramePresenter {
    @Override
    public void onStart(Dialog loading) {
        if (loading != null) loading.show();
    }

    @Override
    public void onStop(Dialog loading) {
        if (loading != null) loading.dismiss();
    }

    public void addPostPoint(Context context,Scheme planning, String longitude, String latitude, final FrameListener<Point> listener) {

        String url = ApiUrl.addPoint(XiangXunApplication.getInstance());
        if (TextUtils.isEmpty(longitude) || TextUtils.isEmpty(longitude)) {
            listener.onFaild(0, "经度不能为空");
            return;
        }
        if (TextUtils.isEmpty(latitude) || TextUtils.isEmpty(latitude)) {
            listener.onFaild(0, "纬度不能为空");
            return;
        }
        if (planning == null) {
            listener.onFaild(0, "方案不能为空");
            return;
        }
        Map<String, String> params = new LinkedHashMap<String, String>();
        params.put("schemeId", planning.id);
        params.put("blockId", planning.blockId);
        params.put("regionId", planning.regionId);
        params.put("longitude", longitude);
        params.put("latitude", latitude);
        params.put("imei", XiangXunApplication.getInstance().getDevId());
        params.put("account", SystemCfg.getAccount(context));
        DcHttpClient.getInstance().postWithURL(XiangXunApplication.getInstance(), url, params, new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        if (null != response) {
                            DLog.json(response.toString());
                            try {
                                ResultPointData resultData = Utils.getGson().fromJson(response.toString(), ResultPointData.class);
                                listener.onSucces(resultData.result);
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

    public interface AddPointInterface extends FrameView {

        void onLoginSuccess(Point data);

        void onLoginFailed(String info);

        //经度
        String longitude();

        //纬度
        String latitude();

        void end();
    }


}
