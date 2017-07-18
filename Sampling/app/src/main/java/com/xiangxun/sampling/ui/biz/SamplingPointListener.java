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
import com.xiangxun.sampling.bean.PlannningData.ResultData;
import com.xiangxun.sampling.bean.PlannningData.Scheme;
import com.xiangxun.sampling.common.NetUtils;
import com.xiangxun.sampling.common.ToastApp;
import com.xiangxun.sampling.common.dlog.DLog;
import com.xiangxun.sampling.common.retrofit.RxjavaRetrofitRequestUtil;
import com.xiangxun.sampling.common.retrofit.paramer.LoginParamer;
import com.xiangxun.sampling.common.retrofit.paramer.SamPointParamer;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

import okhttp3.RequestBody;
import rx.Observer;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Func1;
import rx.schedulers.Schedulers;


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

        if (TextUtils.isEmpty(id) || TextUtils.isEmpty(id)) {
            listener.onFaild(0, "方案id不能为空");
            return;
        }
        if (!NetUtils.isNetworkAvailable(XiangXunApplication.getInstance())) {
            listener.onFaild(0, "网络异常,请检查网络");
            return;
        }
        RequestBody body = RequestBody.create(okhttp3.MediaType.parse("application/x-www-form-urlencoded"), RxjavaRetrofitRequestUtil.getParamers(new SamPointParamer(id, strTime), "UTF-8"));
        RxjavaRetrofitRequestUtil.getInstance().post()
                .point(body)
                .subscribeOn(Schedulers.io()).unsubscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .map(new Func1<JsonObject, ResultData>() {
                    @Override
                    public ResultData call(JsonObject s) {
                        DLog.json("Func1", s.toString());
                        ResultData root = new Gson().fromJson(s, new TypeToken<ResultData>() {
                        }.getType());
                        return root;
                    }
                })
                .subscribe(new Observer<ResultData>() {
                               @Override
                               public void onCompleted() {

                               }

                               @Override
                               public void onError(Throwable e) {
                                   ToastApp.showToast(e.getMessage());
                                   listener.onFaild(1, e.getMessage());
                               }

                               @Override
                               public void onNext(ResultData data) {
                                   if (data != null) {
                                       if (data.result != null && data.resCode == 1000) {
                                           listener.onSucces(data);
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

    public interface SamplingPointInterface extends FrameView {

        void onLoginSuccess(List<Scheme> info);

        void onLoginFailed(String info);

        void end();
    }


}
