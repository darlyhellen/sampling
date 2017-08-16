package com.xiangxun.sampling.ui.biz;

import android.app.Dialog;
import android.content.Context;
import android.text.TextUtils;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;
import com.xiangxun.sampling.base.FrameListener;
import com.xiangxun.sampling.base.FramePresenter;
import com.xiangxun.sampling.base.FrameView;
import com.xiangxun.sampling.base.XiangXunApplication;
import com.xiangxun.sampling.bean.PlannningData;
import com.xiangxun.sampling.bean.PlannningData.Point;
import com.xiangxun.sampling.bean.PlannningData.ResultPointData;
import com.xiangxun.sampling.bean.PlannningData.Scheme;
import com.xiangxun.sampling.common.NetUtils;
import com.xiangxun.sampling.common.ToastApp;
import com.xiangxun.sampling.common.dlog.DLog;
import com.xiangxun.sampling.common.retrofit.RxjavaRetrofitRequestUtil;
import com.xiangxun.sampling.common.retrofit.paramer.AddPointParamer;
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

    public void addPostPoint(Context context, Scheme planning, String longitude, String latitude, final FrameListener<ResultPointData> listener) {

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

        if (!NetUtils.isNetworkAvailable(XiangXunApplication.getInstance())) {
            listener.onFaild(0, "网络异常,请检查网络");
            return;
        }
        RequestBody body = RequestBody.create(okhttp3.MediaType.parse("application/x-www-form-urlencoded"),
                RxjavaRetrofitRequestUtil.getParamers(new AddPointParamer(planning.id, planning.blockId, planning.regionId, longitude, latitude), "UTF-8"));
        RxjavaRetrofitRequestUtil.getInstance().post()
                .addPoint(body)
                .subscribeOn(Schedulers.io()).unsubscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .map(new Func1<JsonObject, ResultPointData>() {
                    @Override
                    public ResultPointData call(JsonObject s) {
                        DLog.json("Func1", s.toString());
                        ResultPointData root = new Gson().fromJson(s, new TypeToken<ResultPointData>() {
                        }.getType());
                        return root;
                    }
                })
                .subscribe(new Observer<ResultPointData>() {
                               @Override
                               public void onCompleted() {

                               }

                               @Override
                               public void onError(Throwable e) {
                                   listener.onFaild(1, "网络连接异常，请检查网络");
                               }

                               @Override
                               public void onNext(ResultPointData data) {
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
                           }

                );

    }

    public void updataPostPoint(Scheme planning, PlannningData.Point point, final FrameListener<ResultPointData> listener) {

        if (planning == null) {
            listener.onFaild(0, "方案不能为空");
            return;
        }
        if (point == null) {
            listener.onFaild(0, "点位信息不能为空");
            return;
        }

        if (!NetUtils.isNetworkAvailable(XiangXunApplication.getInstance())) {
            listener.onFaild(0, "网络异常,请检查网络");
            return;
        }
        RequestBody body = RequestBody.create(okhttp3.MediaType.parse("application/x-www-form-urlencoded"),
                RxjavaRetrofitRequestUtil.getParamers(point, "UTF-8"));
        RxjavaRetrofitRequestUtil.getInstance().post()
                .updataPoint(body)
                .subscribeOn(Schedulers.io()).unsubscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .map(new Func1<JsonObject, ResultPointData>() {
                    @Override
                    public ResultPointData call(JsonObject s) {
                        DLog.json("Func1", s.toString());
                        ResultPointData root = new Gson().fromJson(s, new TypeToken<ResultPointData>() {
                        }.getType());
                        return root;
                    }
                })
                .subscribe(new Observer<ResultPointData>() {
                               @Override
                               public void onCompleted() {

                               }

                               @Override
                               public void onError(Throwable e) {
                                   listener.onFaild(1, "网络连接异常，请检查网络");
                               }

                               @Override
                               public void onNext(ResultPointData data) {
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

    public interface AddPointInterface extends FrameView {

        void onLoginSuccess();

        void onLoginFailed(String info);

        //经度
        String longitude();

        //纬度
        String latitude();

        void end();
    }


}
