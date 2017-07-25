package com.xiangxun.sampling.ui.presenter;

import android.text.TextUtils;

import com.xiangxun.sampling.R;
import com.xiangxun.sampling.base.BaseActivity;
import com.xiangxun.sampling.base.FrameListener;
import com.xiangxun.sampling.bean.PlannningData;
import com.xiangxun.sampling.bean.PlannningData.ResultPointData;
import com.xiangxun.sampling.common.SharePreferHelp;
import com.xiangxun.sampling.common.ToastApp;
import com.xiangxun.sampling.common.retrofit.PontCacheHelper;
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
                biz.onStop(loading);
                if (result.resCode == 2000) {
                    //使用缓存
                    view.onLoginFailed();
                } else if (result.resCode == 1000) {
                    //由于要更新点位系统所以需要将缓存进行更新。
                    PontCacheHelper.cachePoint(id, result);
                    view.onLoginSuccess(result.result);
                }
            }

            @Override
            public void onFaild(int code, String info) {
                biz.onStop(loading);
                ToastApp.showToast(info);
                view.onLoginFailed();
            }
        });

    }

    /**
     * 上传现场采集点位功能。简单参数上传。
     */
    public void sampling(SenceSamplingSugar point) {
        if (point == null ) {
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
        if (TextUtils.isEmpty(point.getSoil_type())) {
            ToastApp.showToast("土壤类型不能为空");
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
        biz.upSampling(point, new FrameListener<PlannningData.ResultPointData>() {
            @Override
            public void onSucces(PlannningData.ResultPointData data) {
                biz.onStop(loading);
                //保存进入数据库，进行下次WIIF上传。
                // paramer.save();
            }

            @Override
            public void onFaild(int code, String info) {
                biz.onStop(loading);
            }
        });
    }

}