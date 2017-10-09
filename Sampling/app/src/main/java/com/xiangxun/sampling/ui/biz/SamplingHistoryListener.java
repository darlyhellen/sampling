package com.xiangxun.sampling.ui.biz;

import android.app.Dialog;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;
import com.xiangxun.sampling.base.FrameListener;
import com.xiangxun.sampling.base.FramePresenter;
import com.xiangxun.sampling.base.FrameView;
import com.xiangxun.sampling.bean.PlannningData.ResultData;
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
 * @author zhangyh2 LoginUser 下午3:42:16 TODO 历史采样信息列表
 */
public class SamplingHistoryListener implements FramePresenter {
    @Override
    public void onStart(Dialog loading) {

    }

    @Override
    public void onStop(Dialog loading) {

    }

    public void getHistory(int currentPage, String hisName, String samplyName, String regionId, String loaction, final FrameListener<ResultData> listener) {


        Map<String, String> para = new HashMap<String, String>();
        para.put("missionName", hisName);
        para.put("pageNo", currentPage + "");
        para.put("regionName", loaction);
        para.put("regionId", regionId);
        if ("背景土壤".equals(samplyName)) {
            para.put("sampleCode", "BJTR");
        } else if ("农作物".equals(samplyName)) {
            para.put("sampleCode", "SD");
        } else if ("水样底泥".equals(samplyName)) {
            para.put("sampleCode", "WATER");
        } else if ("大气沉降".equals(samplyName)) {
            para.put("sampleCode", "DQ");
        } else if ("肥料".equals(samplyName)) {
            para.put("sampleCode", "FL");
        } else if ("农田土壤".equals(samplyName)) {
            para.put("sampleCode", "NTTR");
        } else {
            para.put("sampleCode", "");
        }
        RequestBody body = RequestBody.create(okhttp3.MediaType.parse("application/x-www-form-urlencoded"), RxjavaRetrofitRequestUtil.getParamers(para, "UTF-8"));

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

    public interface SamplingHistoryInterface extends FrameView {

        void onLoginSuccess(ResultData info);

        void onLoginFailed();

        void end();
    }


}
