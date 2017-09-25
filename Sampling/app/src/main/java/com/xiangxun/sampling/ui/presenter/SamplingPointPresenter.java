package com.xiangxun.sampling.ui.presenter;

import android.text.TextUtils;

import com.orm.SugarRecord;
import com.xiangxun.sampling.R;
import com.xiangxun.sampling.base.BaseActivity;
import com.xiangxun.sampling.base.FrameListener;
import com.xiangxun.sampling.bean.PlannningData;
import com.xiangxun.sampling.bean.PlannningData.ResultPointData;
import com.xiangxun.sampling.bean.SenceInfo;
import com.xiangxun.sampling.common.SharePreferHelp;
import com.xiangxun.sampling.common.ToastApp;
import com.xiangxun.sampling.common.retrofit.PontCacheHelper;
import com.xiangxun.sampling.db.MediaSugar;
import com.xiangxun.sampling.db.SenceSamplingSugar;
import com.xiangxun.sampling.ui.biz.SamplingPointListener;
import com.xiangxun.sampling.ui.biz.SamplingPointListener.SamplingPointInterface;
import com.xiangxun.sampling.ui.main.AddNewPointPlanningActivity;
import com.xiangxun.sampling.ui.main.ChaoTuActivity;
import com.xiangxun.sampling.ui.main.SamplingPointActivity;
import com.xiangxun.sampling.widget.dialog.LoadDialog;

import java.util.ArrayList;
import java.util.List;


/**
 * @author zhangyh2 s 上午10:57:39 TODO 控制器 页点位列表操作
 */
public class SamplingPointPresenter {

    private String TAG = getClass().getSimpleName();
    private SamplingPointListener biz;
    private SamplingPointInterface view;
    private LoadDialog loading;

    public SamplingPointPresenter(SamplingPointInterface view) {
        this.view = view;
        this.biz = new SamplingPointListener();
        loading = new LoadDialog((BaseActivity) view);
        loading.setTitle(R.string.st_loading);
    }


    public void point(final String id, String time) {
        biz.onStart(loading);
        biz.postPoint(id, time, new FrameListener<ResultPointData>() {
            @Override
            public void onSucces(ResultPointData result) {

                if (result.resCode == 2000) {
                    //使用缓存
                    view.onLoginFailed();
                } else if (result.resCode == 1000) {
                    //由于要更新点位系统所以需要将缓存进行更新。
                    PontCacheHelper.cachePoint(id, result);
                    view.onLoginSuccess(result.result);
                }
                biz.onStop(loading);
            }

            @Override
            public void onFaild(int code, String info) {

                ToastApp.showToast(info);
                view.onLoginFailed();
                biz.onStop(loading);
            }
        });

    }

    /**
     * 上传现场采集点位功能。简单参数上传。
     */
    public void sampling(final PlannningData.Scheme planning, final SenceSamplingSugar point, final PlannningData.Point dats) {
        if (point == null) {
            ToastApp.showToast("点位信息传递错误");
            return;
        }
        if (TextUtils.isEmpty(point.getPointId())) {
            ToastApp.showToast("点位ID不能为空");
            return;
        }

        if (TextUtils.isEmpty(point.getSchemeId())) {
            ToastApp.showToast("方案ID不能为空");
            return;
        }
        if (TextUtils.isEmpty(point.getRegion_id())) {
            ToastApp.showToast("地址信息不能为空");
            return;
        }
        if (TextUtils.isEmpty(point.getName())) {
            ToastApp.showToast("采样名称不能为空");
            return;
        }
        if (TextUtils.isEmpty(point.getDepth())) {
            ToastApp.showToast("采样深度不能为空");
            return;
        }
        if (TextUtils.isEmpty(point.getMissionId())) {
            ToastApp.showToast("任务ID不能为空");
            return;
        }
        if (TextUtils.isEmpty(point.getSoil_type())) {
            ToastApp.showToast("土壤类型不能为空");
            return;
        }
        if (TextUtils.isEmpty(point.getLongitude())) {
            ToastApp.showToast("经度不能为空");
            return;
        }
        if (TextUtils.isEmpty(point.getLatitude())) {
            ToastApp.showToast("纬度不能为空");
            return;
        }
        biz.onStart(loading);
        biz.upSampling(point, new FrameListener<List<SenceInfo.SenceObj>>() {
            @Override
            public void onSucces(List<SenceInfo.SenceObj> data) {
                biz.onStop(loading);
                //将返回的数据和传递的数据进行合并并保存进数据库。
                if (data != null) {
                    if (data.size() == 1) {
                        point.setSamplingId(data.get(0).id);
                    }
                }
                //图片信息建表
                if (point.getImages() != null && point.getImages().size() > 1) {
                    point.getImages().remove(point.getImages().size() - 1);
                    for (String image : point.getImages()) {
                        MediaSugar sugar = new MediaSugar();
                        sugar.setSamplingId(point.getSamplingId());
                        sugar.setUrl(image);
                        sugar.setType("image");
                        sugar.save();
                    }
                }
                //视频信息建表
                if (point.getVideos() != null && point.getVideos().size() > 1) {
                    point.getVideos().remove(point.getVideos().size() - 1);
                    for (String image : point.getVideos()) {
                        MediaSugar sugar = new MediaSugar();
                        sugar.setSamplingId(point.getSamplingId());
                        sugar.setUrl(image);
                        sugar.setType("video");
                        sugar.save();
                    }
                }
                point.save();
                //将对应的缓存点位进行剔除操作。
                //当时大气采样时，不需要整理缓存
                if (!planning.sampleCode.equals("DQ")){
                    PontCacheHelper.update(point.getSchemeId(), dats);
                }

                view.onUpSuccess();
            }

            @Override
            public void onFaild(int code, String info) {
                biz.onStop(loading);
                view.onUpFailed();
                ToastApp.showToast(info);
            }
        });
    }

}