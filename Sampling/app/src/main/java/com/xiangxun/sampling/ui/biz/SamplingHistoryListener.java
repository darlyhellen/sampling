package com.xiangxun.sampling.ui.biz;

import android.app.Dialog;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;
import com.xiangxun.sampling.base.FrameListener;
import com.xiangxun.sampling.base.FramePresenter;
import com.xiangxun.sampling.base.FrameView;
import com.xiangxun.sampling.bean.PlannningData.ResultData;
import com.xiangxun.sampling.bean.PlannningData.Scheme;
import com.xiangxun.sampling.common.ToastApp;
import com.xiangxun.sampling.common.dlog.DLog;
import com.xiangxun.sampling.common.retrofit.RxjavaRetrofitRequestUtil;
import com.xiangxun.sampling.common.retrofit.paramer.LoginParamer;
import com.xiangxun.sampling.common.retrofit.paramer.SamplingHistoryParamer;

import java.util.List;

import okhttp3.RequestBody;
import rx.Observer;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Func1;
import rx.schedulers.Schedulers;


/**
 * @author zhangyh2 LoginUser 下午3:42:16 TODO 历史采样信息列表
 */
public class SamplingHistoryListener implements FramePresenter {
    @Override
    public void onStart(Dialog loading) {

    }

    @Override
    public void onStop(Dialog loading) {

    }

    public void getHistory(final FrameListener<ResultData> listener) {
        RequestBody body = RequestBody.create(okhttp3.MediaType.parse("application/x-www-form-urlencoded"), RxjavaRetrofitRequestUtil.getParamers(new SamplingHistoryParamer("123"), "UTF-8"));

        //在这里进行数据请求
        RxjavaRetrofitRequestUtil.getInstance().post().hisence(body).
                subscribeOn(Schedulers.io()).unsubscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .map(new Func1<JsonObject, ResultData>() {
                    @Override
                    public ResultData call(JsonObject jsonObject) {
                        DLog.json("Func1", jsonObject.toString());
                        ResultData root = new Gson().fromJson(jsonObject, new TypeToken<ResultData>() {
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
                            if (data.result != null) {
                                listener.onSucces(data);
                            } else {
                                listener.onFaild(0, data.resDesc);
                            }
                        } else {
                            listener.onFaild(0, "解析错误");
                        }
                    }
                });

    }

    public interface SamplingHistoryInterface extends FrameView {

        void onLoginSuccess(List<Scheme> info);

        void onLoginFailed();

        void end();
    }


}
