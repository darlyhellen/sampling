package com.xiangxun.sampling.ui.biz;

import android.app.Dialog;
import android.text.TextUtils;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;
import com.xiangxun.sampling.base.FrameListener;
import com.xiangxun.sampling.base.FramePresenter;
import com.xiangxun.sampling.base.FrameView;
import com.xiangxun.sampling.base.SystemCfg;
import com.xiangxun.sampling.base.XiangXunApplication;
import com.xiangxun.sampling.bean.ResultData;
import com.xiangxun.sampling.bean.ResultData.LoginData;
import com.xiangxun.sampling.common.NetUtils;
import com.xiangxun.sampling.common.ToastApp;
import com.xiangxun.sampling.common.dlog.DLog;
import com.xiangxun.sampling.common.retrofit.RxjavaRetrofitRequestUtil;
import com.xiangxun.sampling.common.retrofit.paramer.LoginParamer;
import com.xiangxun.sampling.common.retrofit.paramer.SenceParamer;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import okhttp3.RequestBody;
import rx.Observer;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Func1;
import rx.schedulers.Schedulers;


/**
 * @author zhangyh2 LoginUser 下午3:42:16 TODO 用户登录获取数据传递给了接口
 */
public class SenceListener implements FramePresenter {
    @Override
    public void onStart(Dialog loading) {
        if (loading != null) loading.show();
    }

    @Override
    public void onStop(Dialog loading) {
        if (loading != null) loading.dismiss();
    }

    public void upSampling(SenceParamer paramer, final FrameListener<String> listener) {
        if (paramer == null) {
            listener.onFaild(0, "传递参数不能为空");
            return;
        }

        if (!NetUtils.isNetworkAvailable(XiangXunApplication.getInstance())) {
            listener.onFaild(0, "网络异常,请检查网络");
            return;
        }

        RequestBody body = RequestBody.create(okhttp3.MediaType.parse("application/x-www-form-urlencoded"), RxjavaRetrofitRequestUtil.getParamers(paramer, "UTF-8"));
        RxjavaRetrofitRequestUtil.getInstance().post()
                .senceSamply(body)
                .subscribeOn(Schedulers.io()).unsubscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .map(new Func1<JsonObject, String>() {
                    @Override
                    public String call(JsonObject s) {
                        DLog.json("Func1", s.toString());
                        return s.toString();
                    }
                })
                .subscribe(new Observer<String>() {
                               @Override
                               public void onCompleted() {

                               }

                               @Override
                               public void onError(Throwable e) {
                                   ToastApp.showToast(e.getMessage());
                                   listener.onFaild(1, e.getMessage());
                               }

                               @Override
                               public void onNext(String data) {
                               }
                           }

                );
    }

    public interface SenceInterface extends FrameView {

        void onLoginSuccess();

        void onLoginFailed(String info);

        //位置名称
        String getaddress();

        //经度
        String getlatitude();

        //纬度
        String getlongitude();

        //采样类型
        String gettype();

        //样品名称
        String getname();

        //样品深度
        String getparams();

        //待测项目
        String getproject();

        //其他說明
        String getother();

        void end();
    }
}
