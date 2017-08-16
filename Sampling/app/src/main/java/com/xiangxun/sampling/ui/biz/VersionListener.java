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
import com.xiangxun.sampling.bean.HisSencePageInfo;
import com.xiangxun.sampling.bean.UpdateData;
import com.xiangxun.sampling.bean.VerisonInfo;
import com.xiangxun.sampling.common.NetUtils;
import com.xiangxun.sampling.common.ToastApp;
import com.xiangxun.sampling.common.dlog.DLog;
import com.xiangxun.sampling.common.retrofit.RxjavaRetrofitRequestUtil;

import java.util.HashMap;
import java.util.Map;

import okhttp3.RequestBody;
import rx.Observer;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Func1;
import rx.schedulers.Schedulers;


/**
 * @author zhangyh2 LoginUser 下午3:42:16 TODO 版本更新接口
 */
public class VersionListener implements FramePresenter {
    @Override
    public void onStart(Dialog loading) {
    }

    @Override
    public void onStop(Dialog loading) {
    }

    public void findVersion(String version, final FrameListener<VerisonInfo> listener) {
        if (!NetUtils.isNetworkAvailable(XiangXunApplication.getInstance())) {
            listener.onFaild(0, "网络异常,请检查网络");
            return;
        }
        RxjavaRetrofitRequestUtil.getInstance().get()
                .getVersion(version)
                .subscribeOn(Schedulers.io()).unsubscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .map(new Func1<JsonObject, VerisonInfo>() {
                    @Override
                    public VerisonInfo call(JsonObject s) {
                        DLog.json("Func1", s.toString());
                        VerisonInfo root = new Gson().fromJson(s, new TypeToken<VerisonInfo>() {
                        }.getType());
                        return root;
                    }
                })
                .subscribe(new Observer<VerisonInfo>() {
                               @Override
                               public void onCompleted() {

                               }

                               @Override
                               public void onError(Throwable e) {
                                   listener.onFaild(1, "网络连接异常，请检查网络");
                               }

                               @Override
                               public void onNext(VerisonInfo data) {
                                   if (data.resCode == 1000) {
                                       listener.onSucces(data);
                                   } else {
                                       listener.onFaild(0, "没有新版本");
                                   }
                               }
                           }

                );
    }

    public interface VersionInterface extends FrameView {

        void onVersionSuccess(UpdateData result);

        void onVersionFailed(String info);
    }
}
