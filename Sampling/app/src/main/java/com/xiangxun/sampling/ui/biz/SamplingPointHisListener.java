package com.xiangxun.sampling.ui.biz;

import android.app.Dialog;
import android.text.TextUtils;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;
import com.xiangxun.sampling.base.FrameListener;
import com.xiangxun.sampling.base.FramePresenter;
import com.xiangxun.sampling.base.FrameView;
import com.xiangxun.sampling.base.XiangXunApplication;
import com.xiangxun.sampling.bean.HisPlanningData;
import com.xiangxun.sampling.common.NetUtils;
import com.xiangxun.sampling.common.dlog.DLog;
import com.xiangxun.sampling.common.retrofit.RxjavaRetrofitRequestUtil;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import okhttp3.RequestBody;
import rx.Observer;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Func1;
import rx.schedulers.Schedulers;


/**
 * @author zhangyh2 LoginUser 下午3:42:16 TODO  根据前面的方案列表点击获取方案id，根据方案id从服务端获取历史点位列表
 */
public class SamplingPointHisListener implements FramePresenter {
    @Override
    public void onStart(Dialog loading) {
        if (loading != null) loading.show();
    }

    @Override
    public void onStop(Dialog loading) {
        if (loading != null) loading.dismiss();
    }

    public void postHisPoint(String id, String code, final FrameListener<List<HisPlanningData.HisPoint>> listener) {

        if (TextUtils.isEmpty(id) || TextUtils.isEmpty(id)) {
            listener.onFaild(0, "方案id不能为空");
            return;
        }
        if (!NetUtils.isNetworkAvailable(XiangXunApplication.getInstance())) {
            listener.onFaild(0, "网络异常,请检查网络");
            return;
        }

        Map<String, String> map = new HashMap<String, String>();
        map.put("missionId", id);
        map.put("sampleCode",code);
        RequestBody body = RequestBody.create(okhttp3.MediaType.parse("application/x-www-form-urlencoded"), RxjavaRetrofitRequestUtil.getParamers(map, "UTF-8"));
        RxjavaRetrofitRequestUtil.getInstance().post()
                .hisencepointlist(body)
                .subscribeOn(Schedulers.io()).unsubscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .map(new Func1<JsonObject, HisPlanningData.HisPointData>() {
                    @Override
                    public HisPlanningData.HisPointData call(JsonObject s) {
                        DLog.json("Func1", s.toString());
                        HisPlanningData.HisPointData root = new Gson().fromJson(s, new TypeToken<HisPlanningData.HisPointData>() {
                        }.getType());
                        return root;
                    }
                })
                .subscribe(new Observer<HisPlanningData.HisPointData>() {
                               @Override
                               public void onCompleted() {

                               }

                               @Override
                               public void onError(Throwable e) {
                                   listener.onFaild(1, "网络连接异常，请检查网络");
                               }

                               @Override
                               public void onNext(HisPlanningData.HisPointData data) {
                                   if (data != null) {
                                       if (data.resCode == 1000&&data.result!=null) {
                                           listener.onSucces(data.result);
                                       } else {
                                           listener.onFaild(0, data.resDesc);
                                       }
                                   } else {
                                       listener.onFaild(0, "解析错误");
                                   }
                               }
                           }

                );


    }

    public interface SamplingPointHisInterface extends FrameView {

        void onLoginSuccess(List<HisPlanningData.HisPoint> info);

        void onLoginFailed();

    }


}
