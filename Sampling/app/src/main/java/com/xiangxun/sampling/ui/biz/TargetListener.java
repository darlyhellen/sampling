package com.xiangxun.sampling.ui.biz;

import android.app.Dialog;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;
import com.xiangxun.sampling.base.FrameListener;
import com.xiangxun.sampling.base.FramePresenter;
import com.xiangxun.sampling.base.FrameView;
import com.xiangxun.sampling.base.XiangXunApplication;
import com.xiangxun.sampling.bean.SenceLandRegion;
import com.xiangxun.sampling.bean.SimplingTarget;
import com.xiangxun.sampling.bean.SimplingTargetResult;
import com.xiangxun.sampling.common.NetUtils;
import com.xiangxun.sampling.common.ToastApp;
import com.xiangxun.sampling.common.dlog.DLog;
import com.xiangxun.sampling.common.retrofit.RxjavaRetrofitRequestUtil;
import com.xiangxun.sampling.db.SenceSamplingSugar;

import java.util.List;

import okhttp3.RequestBody;
import rx.Observer;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Func1;
import rx.schedulers.Schedulers;


/**
 * @author zhangyh2 LoginUser 下午3:42:16 TODO 指标查询接口
 */
public class TargetListener implements FramePresenter {
    @Override
    public void onStart(Dialog loading) {
        if (loading != null) loading.show();
    }

    @Override
    public void onStop(Dialog loading) {
        if (loading != null) loading.dismiss();
    }

    public void analysis(SenceSamplingSugar paramer, final FrameListener<SimplingTargetResult> listener) {
        if (!NetUtils.isNetworkAvailable(XiangXunApplication.getInstance())) {
            listener.onFaild(0, "网络异常,请检查网络");
            return;
        }
        RxjavaRetrofitRequestUtil.getInstance().get()
                .analysis()
                .subscribeOn(Schedulers.io()).unsubscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .map(new Func1<JsonObject, SimplingTargetResult>() {
                    @Override
                    public SimplingTargetResult call(JsonObject s) {
                        DLog.json("Func1", s.toString());
                        SimplingTargetResult root = new Gson().fromJson(s, new TypeToken<SimplingTargetResult>() {
                        }.getType());
                        return root;
                    }
                })
                .subscribe(new Observer<SimplingTargetResult>() {
                               @Override
                               public void onCompleted() {

                               }

                               @Override
                               public void onError(Throwable e) {
                                   ToastApp.showToast(e.getMessage());
                                   listener.onFaild(1, e.getMessage());
                               }

                               @Override
                               public void onNext(SimplingTargetResult data) {
                                   if (data.resCode == 1000 && data.result != null) {
                                       listener.onSucces(data);
                                   } else {
                                       listener.onFaild(0, "解析异常");
                                   }
                               }
                           }

                );
    }

    public interface TargetInterface extends FrameView {

        void onDateSuccess(List<SimplingTarget> result);

        void onDateFailed(String info);
    }
}
