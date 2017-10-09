package com.xiangxun.sampling.ui.biz;

import android.app.Dialog;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;
import com.xiangxun.sampling.base.FrameListener;
import com.xiangxun.sampling.base.FramePresenter;
import com.xiangxun.sampling.base.FrameView;
import com.xiangxun.sampling.base.XiangXunApplication;
import com.xiangxun.sampling.bean.PlannningData;
import com.xiangxun.sampling.bean.SenceInfo;
import com.xiangxun.sampling.bean.SenceLandRegion;
import com.xiangxun.sampling.common.NetUtils;
import com.xiangxun.sampling.common.ToastApp;
import com.xiangxun.sampling.common.dlog.DLog;
import com.xiangxun.sampling.common.retrofit.RxjavaRetrofitRequestUtil;
import com.xiangxun.sampling.db.SenceSamplingSugar;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

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

    public void upSampling(SenceSamplingSugar paramer, final FrameListener<List<SenceInfo.SenceObj>> listener) {
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
                .map(new Func1<JsonObject, SenceInfo>() {
                    @Override
                    public SenceInfo call(JsonObject s) {
                        DLog.json("Func1", s.toString());
                        SenceInfo root = new Gson().fromJson(s, new TypeToken<SenceInfo>() {
                        }.getType());
                        return root;
                    }
                })
                .subscribe(new Observer<SenceInfo>() {
                               @Override
                               public void onCompleted() {

                               }

                               @Override
                               public void onError(Throwable e) {
                                   listener.onFaild(1, "网络连接异常，请检查网络");
                               }

                               @Override
                               public void onNext(SenceInfo data) {
                                   if (data != null && data.resCode == 1000) {
                                       listener.onSucces(data.result);
                                   } else {
                                       listener.onFaild(0, "解析错误");
                                   }
                               }
                           }

                );

    }

    public void senceSampy(SenceSamplingSugar paramer, final FrameListener<List<SenceInfo.SenceObj>> listener) {
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
                .senceSamplyV(body)
                .subscribeOn(Schedulers.io()).unsubscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .map(new Func1<JsonObject, SenceInfo>() {
                    @Override
                    public SenceInfo call(JsonObject s) {
                        DLog.json("Func1", s.toString());
                        SenceInfo root = new Gson().fromJson(s, new TypeToken<SenceInfo>() {
                        }.getType());
                        return root;
                    }
                })
                .subscribe(new Observer<SenceInfo>() {
                               @Override
                               public void onCompleted() {

                               }
                               @Override
                               public void onError(Throwable e) {
                                   listener.onFaild(1, "网络连接异常，请检查网络");
                               }
                               @Override
                               public void onNext(SenceInfo data) {
                                   if (data.resCode == 1000){
                                       if (data != null) {
                                           listener.onSucces(data.result);
                                       } else {
                                           listener.onFaild(0, "解析失败");
                                       }
                                   }else {
                                       listener.onFaild(0, data.resDesc);
                                   }
                               }
                           }

                );

    }

    //地块选择列表请求接口
    public void landType(final FrameListener<SenceLandRegion> listener) {
        RxjavaRetrofitRequestUtil.getInstance().get().landType()
                .subscribeOn(Schedulers.io()).unsubscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .map(new Func1<JsonObject, SenceLandRegion>() {
                    @Override
                    public SenceLandRegion call(JsonObject s) {
                        DLog.json("Func1", s.toString());
                        SenceLandRegion root = new Gson().fromJson(s, new TypeToken<SenceLandRegion>() {
                        }.getType());
                        return root;
                    }
                })
                .subscribe(new Observer<SenceLandRegion>() {
                               @Override
                               public void onCompleted() {

                               }

                               @Override
                               public void onError(Throwable e) {
                                   listener.onFaild(1, "网络连接异常，请检查网络");
                               }

                               @Override
                               public void onNext(SenceLandRegion data) {
                                   listener.onSucces(data);
                               }
                           }

                );
    }

    //根據不同選擇進行不同請求
    public void getTypese(String code,int mold, String missionId,final FrameListener<SenceLandRegion> listener) {
        if (code == null) {
            listener.onFaild(0, "采样类型不能为空");
            return;
        }
        if (!NetUtils.isNetworkAvailable(XiangXunApplication.getInstance())) {
            listener.onFaild(0, "网络异常,请检查网络");
            return;
        }
        Map<String,String> paramer = new HashMap<String ,String >();
        paramer.put("sampleCode",code);
        paramer.put("mold",String.valueOf(mold));
        paramer.put("missionId",String.valueOf(missionId));
        RequestBody body = RequestBody.create(okhttp3.MediaType.parse("application/x-www-form-urlencoded"), RxjavaRetrofitRequestUtil.getParamers(paramer, "UTF-8"));
        RxjavaRetrofitRequestUtil.getInstance().post().getSample(body)
                .subscribeOn(Schedulers.io()).unsubscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .map(new Func1<JsonObject, SenceLandRegion>() {
                    @Override
                    public SenceLandRegion call(JsonObject s) {
                        DLog.json("Func1", s.toString());
                        SenceLandRegion root = new Gson().fromJson(s, new TypeToken<SenceLandRegion>() {
                        }.getType());
                        return root;
                    }
                })
                .subscribe(new Observer<SenceLandRegion>() {
                               @Override
                               public void onCompleted() {

                               }

                               @Override
                               public void onError(Throwable e) {
                                   listener.onFaild(1, "网络连接异常，请检查网络");
                               }

                               @Override
                               public void onNext(SenceLandRegion data) {
                                   listener.onSucces(data);
                               }
                           }

                );
    }

    public void region(final FrameListener<SenceLandRegion> listener) {
        RxjavaRetrofitRequestUtil.getInstance().get().region()
                .subscribeOn(Schedulers.io()).unsubscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .map(new Func1<JsonObject, SenceLandRegion>() {
                    @Override
                    public SenceLandRegion call(JsonObject s) {
                        DLog.json("Func1", s.toString());
                        SenceLandRegion root = new Gson().fromJson(s, new TypeToken<SenceLandRegion>() {
                        }.getType());
                        return root;
                    }
                })
                .subscribe(new Observer<SenceLandRegion>() {
                               @Override
                               public void onCompleted() {

                               }

                               @Override
                               public void onError(Throwable e) {
                                   listener.onFaild(1, "网络连接异常，请检查网络");
                               }

                               @Override
                               public void onNext(SenceLandRegion data) {
                                   listener.onSucces(data);
                               }
                           }

                );
    }

    public interface SenceInterface extends FrameView {
        //背景土壤采样内容和展示内容
        void BackSoilSamply();
        //农作物采样内容和展示内容
        void FarmSamply();
        //大气采样内容和展示内容
        void OxsSamply();
        //水采样内容和展示内容
        void WaterSamply();
        //肥料内容和展示内容
        void ManureSamply();
        //土壤采样内容和展示内容
       void SoilSamply();

        //采样类型
        SenceLandRegion.LandRegion sence_typeed();

        String gettype();
        //样品名称
        String getname();
        //样品深度
        String getparams();
        //待测项目
        SenceLandRegion.LandRegion sence_project();
        //其他說明
        String getother();

        //空采样内容和展示内容
        void NullSamply();
        //位置名称
        String getaddress();
        //经度
        String getlatitude();
        //纬度
        String getlongitude();
        //图片
        List<String> getImages();
        //视频
        List<String> getVideos();
        void onLoginSuccess(List<SenceInfo.SenceObj> data);
        void onLoginFailed(String info);
        void onTypeRegionSuccess(String title, SenceLandRegion result);
        void end();
    }
}
