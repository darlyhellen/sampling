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
import com.xiangxun.sampling.bean.SamplingSenceGroup;
import com.xiangxun.sampling.common.ToastApp;
import com.xiangxun.sampling.common.dlog.DLog;
import com.xiangxun.sampling.common.retrofit.RxjavaRetrofitRequestUtil;

import java.util.List;

import rx.Observer;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Func1;
import rx.schedulers.Schedulers;


/**
 * @author zhangyh2 LoginUser 下午3:42:16 TODO 首页进入采样计划展示页面，进行数据请求。并对数据进行缓存处理。
 */
public class SamplingPlanningListener implements FramePresenter {
    @Override
    public void onStart(Dialog loading) {
        if (loading != null) loading.show();
    }

    @Override
    public void onStop(Dialog loading) {
        if (loading != null) loading.dismiss();
    }

    public void getPlanning(String time, final FrameListener<ResultData> listener) {


        //在这里进行数据请求
        RxjavaRetrofitRequestUtil.getInstance().get().planning(time).
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
                        listener.onFaild(1, "网络连接异常，请检查网络");
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

    public void findPlanning(final FrameListener<List<SamplingSenceGroup.SenceGroup>> listener) {


        //在这里进行数据请求
        RxjavaRetrofitRequestUtil.getInstance().get().planningscene().
                subscribeOn(Schedulers.io()).unsubscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .map(new Func1<JsonObject, SamplingSenceGroup.SamplingSence>() {
                    @Override
                    public SamplingSenceGroup.SamplingSence call(JsonObject jsonObject) {
                        DLog.json("Func1", jsonObject.toString());
                        SamplingSenceGroup.SamplingSence root = new Gson().fromJson(jsonObject, new TypeToken<SamplingSenceGroup.SamplingSence>() {
                        }.getType());
                        return root;
                    }
                })
                .subscribe(new Observer<SamplingSenceGroup.SamplingSence>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        listener.onFaild(1, "网络连接异常，请检查网络");
                    }

                    @Override
                    public void onNext(SamplingSenceGroup.SamplingSence data) {
                        if (data != null) {
                            if (data.resCode == 1000 && data.result != null) {
                                listener.onSucces(data.result);
                            } else {
                                listener.onFaild(0, data.resDesc);
                            }
                        } else {
                            listener.onFaild(0, "解析错误");
                        }
                    }
                });

    }

    public interface SamplingPlanningInterface extends FrameView {

        void onLoginSuccessV1(List<SamplingSenceGroup.SenceGroup> result);

        void onLoginSuccess(List<Scheme> info);

        void onLoginFailed();

        void end();
    }


}
