package com.xiangxun.sampling.ui.biz;

import android.app.Dialog;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;
import com.xiangxun.sampling.base.FrameListener;
import com.xiangxun.sampling.base.FramePresenter;
import com.xiangxun.sampling.base.FrameView;
import com.xiangxun.sampling.base.XiangXunApplication;
import com.xiangxun.sampling.bean.HisExceptionPageInfo;
import com.xiangxun.sampling.bean.SimplingTarget;
import com.xiangxun.sampling.bean.SimplingTargetResult;
import com.xiangxun.sampling.common.NetUtils;
import com.xiangxun.sampling.common.ToastApp;
import com.xiangxun.sampling.common.dlog.DLog;
import com.xiangxun.sampling.common.retrofit.RxjavaRetrofitRequestUtil;
import com.xiangxun.sampling.common.retrofit.paramer.AnaylistParamer;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import okhttp3.RequestBody;
import rx.Observer;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Func1;
import rx.schedulers.Schedulers;


/**
 * @author zhangyh2 LoginUser 下午3:42:16 TODO 指标查询接口
 */
public class HEPListener implements FramePresenter {
    @Override
    public void onStart(Dialog loading) {
        if (loading != null) loading.show();
    }

    @Override
    public void onStop(Dialog loading) {
        if (loading != null) loading.dismiss();
    }

    public void getHEP(String id, final FrameListener<HisExceptionPageInfo> listener) {
        if (!NetUtils.isNetworkAvailable(XiangXunApplication.getInstance())) {
            listener.onFaild(0, "网络异常,请检查网络");
            return;
        }
        Map<String, String> params = new HashMap<String, String>();
        params.put("id", id);
        RequestBody body = RequestBody.create(okhttp3.MediaType.parse("application/x-www-form-urlencoded"), RxjavaRetrofitRequestUtil.getParamers(params, "UTF-8"));
        RxjavaRetrofitRequestUtil.getInstance().post()
                .hisshow(body)
                .subscribeOn(Schedulers.io()).unsubscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .map(new Func1<JsonObject, HisExceptionPageInfo>() {
                    @Override
                    public HisExceptionPageInfo call(JsonObject s) {
                        DLog.json("Func1", s.toString());
                        HisExceptionPageInfo root = new Gson().fromJson(s, new TypeToken<HisExceptionPageInfo>() {
                        }.getType());
                        return root;
                    }
                })
                .subscribe(new Observer<HisExceptionPageInfo>() {
                               @Override
                               public void onCompleted() {

                               }

                               @Override
                               public void onError(Throwable e) {
                                   listener.onFaild(1, "网络连接异常，请检查网络");
                               }

                               @Override
                               public void onNext(HisExceptionPageInfo data) {
                                   if (data.resCode == 1000 && data.result != null) {
                                       listener.onSucces(data);
                                   } else {
                                       listener.onFaild(0, "解析异常");
                                   }
                               }
                           }

                );
    }

    public interface HEPInterface extends FrameView {

        void onDateSuccess(HisExceptionPageInfo.HisExceptionPage result);

        void onDateFailed(String info);
    }
}
