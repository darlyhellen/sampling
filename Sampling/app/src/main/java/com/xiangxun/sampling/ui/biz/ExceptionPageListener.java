package com.xiangxun.sampling.ui.biz;

import android.app.Dialog;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;
import com.xiangxun.sampling.base.FrameListener;
import com.xiangxun.sampling.base.FramePresenter;
import com.xiangxun.sampling.base.FrameView;
import com.xiangxun.sampling.base.XiangXunApplication;
import com.xiangxun.sampling.bean.HisExceptionInfo;
import com.xiangxun.sampling.common.NetUtils;
import com.xiangxun.sampling.common.ToastApp;
import com.xiangxun.sampling.common.dlog.DLog;
import com.xiangxun.sampling.common.retrofit.RxjavaRetrofitRequestUtil;

import java.util.List;

import okhttp3.RequestBody;
import rx.Observer;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Func1;
import rx.schedulers.Schedulers;


/**
 * @author zhangyh2 LoginUser 下午3:42:16 TODO 地块异常上传功能
 */
public class ExceptionPageListener implements FramePresenter {
    @Override
    public void onStart(Dialog loading) {
        if (loading != null) loading.show();
    }

    @Override
    public void onStop(Dialog loading) {
        if (loading != null) loading.dismiss();
    }

    public void addException(RequestBody requestBody, final FrameListener<HisExceptionInfo> listener) {
        if (!NetUtils.isNetworkAvailable(XiangXunApplication.getInstance())) {
            listener.onFaild(0, "网络异常,请检查网络");
            return;
        }
        RxjavaRetrofitRequestUtil.getInstance().post()
                .addexc(requestBody)
                .subscribeOn(Schedulers.io()).unsubscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .map(new Func1<JsonObject, HisExceptionInfo>() {
                    @Override
                    public HisExceptionInfo call(JsonObject s) {
                        DLog.json("Func1", s.toString());
                        HisExceptionInfo root = new Gson().fromJson(s, new TypeToken<HisExceptionInfo>() {
                        }.getType());
                        return root;
                    }
                })
                .subscribe(new Observer<HisExceptionInfo>() {
                               @Override
                               public void onCompleted() {

                               }

                               @Override
                               public void onError(Throwable e) {
                                   listener.onFaild(1, "网络连接异常，请检查网络");
                               }

                               @Override
                               public void onNext(HisExceptionInfo data) {
                                   if (data.resCode == 1000) {
                                       listener.onSucces(data);
                                   } else {
                                       listener.onFaild(0, "解析异常");
                                   }
                               }
                           }

                );
    }

    public interface ExceptionPageInterface extends FrameView {

        void onDateSuccess(List<HisExceptionInfo.HisException> result);

        void onDateFailed(String info);

        String getLatitude();

        String getLongitude();

        String getLandid();

        String getDeclare();

        List<String> getImages();
    }
}
