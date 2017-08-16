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
import com.xiangxun.sampling.bean.SimplingTarget;
import com.xiangxun.sampling.bean.SimplingTargetResult;
import com.xiangxun.sampling.common.NetUtils;
import com.xiangxun.sampling.common.ToastApp;
import com.xiangxun.sampling.common.dlog.DLog;
import com.xiangxun.sampling.common.retrofit.RxjavaRetrofitRequestUtil;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import okhttp3.RequestBody;
import rx.Observer;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Func1;
import rx.schedulers.Schedulers;


/**
 * @author zhangyh2 LoginUser 下午3:42:16 TODO 历史现场展示页面
 */
public class HisSenceListener implements FramePresenter {
    @Override
    public void onStart(Dialog loading) {
        if (loading != null) loading.show();
    }

    @Override
    public void onStop(Dialog loading) {
        if (loading != null) loading.dismiss();
    }

    public void sencehispage(String id,String missionId, final FrameListener<HisSencePageInfo> listener) {
        if (!NetUtils.isNetworkAvailable(XiangXunApplication.getInstance())) {
            listener.onFaild(0, "网络异常,请检查网络");
            return;
        }
        if (TextUtils.isEmpty(id)) {
            listener.onFaild(0, "id不能为空");
            return;
        }
        Map<String, String> map = new HashMap<String, String>();
        map.put("id", id);
        map.put("schemeId", missionId);
        RequestBody body = RequestBody.create(okhttp3.MediaType.parse("application/x-www-form-urlencoded"), RxjavaRetrofitRequestUtil.getParamers(map, "UTF-8"));
        RxjavaRetrofitRequestUtil.getInstance().post()
                .sencehispage(body)
                .subscribeOn(Schedulers.io()).unsubscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .map(new Func1<JsonObject, HisSencePageInfo>() {
                    @Override
                    public HisSencePageInfo call(JsonObject s) {
                        DLog.json("Func1", s.toString());
                        HisSencePageInfo root = new Gson().fromJson(s, new TypeToken<HisSencePageInfo>() {
                        }.getType());
                        return root;
                    }
                })
                .subscribe(new Observer<HisSencePageInfo>() {
                               @Override
                               public void onCompleted() {

                               }

                               @Override
                               public void onError(Throwable e) {
                                   listener.onFaild(1, "网络连接异常，请检查网络");
                               }

                               @Override
                               public void onNext(HisSencePageInfo data) {
                                   if (data.resCode == 1000 && data.result != null) {
                                       listener.onSucces(data);
                                   } else {
                                       listener.onFaild(0, "解析异常");
                                   }
                               }
                           }

                );
    }

    public interface HisSenceInterface extends FrameView {

        void onDateSuccess(HisSencePageInfo.HisSencePage result);

        void onDateFailed(String info);
    }
}
