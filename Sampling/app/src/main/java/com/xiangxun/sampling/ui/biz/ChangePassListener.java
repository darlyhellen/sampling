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
import com.xiangxun.sampling.common.retrofit.paramer.ChangePassParamer;
import com.xiangxun.sampling.common.retrofit.paramer.LoginParamer;

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
public class ChangePassListener implements FramePresenter {
    @Override
    public void onStart(Dialog loading) {
        if (loading != null) loading.show();
    }

    @Override
    public void onStop(Dialog loading) {
        if (loading != null) loading.dismiss();
    }

    public void ChangePass(String oldpasswords, String newpasswords, final FrameListener<String> listener) {
        if (!NetUtils.isNetworkAvailable(XiangXunApplication.getInstance())) {
            listener.onFaild(0, "网络异常,请检查网络");
            return;
        }
        RequestBody body = RequestBody.create(okhttp3.MediaType.parse("application/x-www-form-urlencoded"), RxjavaRetrofitRequestUtil.getParamers(new ChangePassParamer(newpasswords,oldpasswords), "UTF-8"));
        RxjavaRetrofitRequestUtil.getInstance().post()
                .postPass(body)
                .subscribeOn(Schedulers.io()).unsubscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .map(new Func1<JsonObject, LoginData>() {
                    @Override
                    public LoginData call(JsonObject s) {
                        DLog.json("Func1", s.toString());
                        LoginData root = new Gson().fromJson(s, new TypeToken<LoginData>() {
                        }.getType());
                        return root;
                    }
                })
                .subscribe(new Observer<LoginData>() {
                               @Override
                               public void onCompleted() {

                               }

                               @Override
                               public void onError(Throwable e) {
                                   listener.onFaild(1, "网络连接异常，请检查网络");
                               }

                               @Override
                               public void onNext(LoginData data) {
                                   if (data != null) {
                                       if (data.resCode == 1000) {
                                           listener.onSucces(data.resDesc);
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

    public interface ChangePassInterface extends FrameView {

        void onLoginSuccess();

        void onLoginFailed(String info);

        void end();
    }
}
