package com.xiangxun.sampling.ui.presenter;

import android.text.TextUtils;

import com.xiangxun.sampling.R;
import com.xiangxun.sampling.base.BaseActivity;
import com.xiangxun.sampling.base.FrameListener;
import com.xiangxun.sampling.bean.PlannningData;
import com.xiangxun.sampling.bean.SenceLandRegion;
import com.xiangxun.sampling.common.SharePreferHelp;
import com.xiangxun.sampling.common.ToastApp;
import com.xiangxun.sampling.common.retrofit.PontCacheHelper;
import com.xiangxun.sampling.db.SenceSamplingSugar;
import com.xiangxun.sampling.ui.biz.SenceListener;
import com.xiangxun.sampling.widget.dialog.LoadDialog;


/**
 * @author zhangyh2 s 上午10:57:39 TODO 控制器 页面动作操作
 */
public class SencePresenter {

    private String TAG = getClass().getSimpleName();
    private SenceListener userBiz;
    private SenceListener.SenceInterface main;
    private LoadDialog loading;

    private boolean isave;

    public SencePresenter(SenceListener.SenceInterface main) {
        this.main = main;
        this.userBiz = new SenceListener();
        loading = new LoadDialog((BaseActivity) main);
        loading.setTitle(R.string.st_loading);
        isave = false;
    }


    /**
     * 上传现场采集点位功能。简单参数上传。
     */
    public void sampling(final PlannningData.Scheme planning, PlannningData.Point point) {
        if (point == null || TextUtils.isEmpty(point.id) || TextUtils.isEmpty(point.schemeId)) {
            ToastApp.showToast("点位信息传递错误");
            return;
        }

        if (TextUtils.isEmpty(main.gettype().code)) {
            ToastApp.showToast("采样类型不能为空");
            return;
        }

        if (TextUtils.isEmpty(main.getname())) {
            ToastApp.showToast("样品名称不能为空");
            return;
        }
        if (TextUtils.isEmpty(main.getparams())) {
            ToastApp.showToast("样品深度不能为空");
            return;
        }
        if (TextUtils.isEmpty(main.getproject())) {
            ToastApp.showToast("待测项目不能为空");
            return;
        }
        if (TextUtils.isEmpty(main.getaddress())) {
            ToastApp.showToast("地址信息不能为空");
            return;
        }
        if (TextUtils.isEmpty(main.getlongitude())) {
            ToastApp.showToast("经度不能为空");
            return;
        }
        if (TextUtils.isEmpty(main.getlatitude())) {
            ToastApp.showToast("纬度不能为空");
            return;
        }
        final SenceSamplingSugar paramer = new SenceSamplingSugar();
        paramer.setPointId(point.id);
        paramer.setSchemeId(point.schemeId);
        paramer.setRegion_id(main.getaddress());
        paramer.setLongitude(main.getlongitude());
        paramer.setLatitude(main.getlatitude());
        paramer.setSoil_type(main.gettype().code);
        paramer.setName(main.getname());
        paramer.setDepth(main.getparams());
        paramer.setTest_item(main.getproject());
        paramer.setMissionId(planning.missionId);
        userBiz.onStart(loading);
        main.setDisableClick();
        userBiz.upSampling(paramer, new FrameListener<PlannningData.ResultPointData>() {
            @Override
            public void onSucces(PlannningData.ResultPointData data) {
                //当时大气采样时，不需要整理缓存
                if (!planning.sampleCode.equals("DQ")){
                    PontCacheHelper.cachePoint(planning.id, data);
                }
                userBiz.onStop(loading);
                main.setEnableClick();
                //保存进入数据库，进行下次WIIF上传。
                // paramer.save();
                main.onLoginSuccess();
            }

            @Override
            public void onFaild(int code, String info) {
                userBiz.onStop(loading);
                main.setEnableClick();
                main.onLoginFailed(info);
                ToastApp.showToast(info);
            }
        });
    }

    //土壤类型请求。
    public void landType(final String title) {
        main.setDisableClick();
        userBiz.landType(new FrameListener<SenceLandRegion>() {
            @Override
            public void onSucces(SenceLandRegion result) {
                main.setEnableClick();
                main.onTypeRegionSuccess(title, result);
            }

            @Override
            public void onFaild(int code, String info) {
                ToastApp.showToast(info);
                main.setEnableClick();
            }
        });
    }

    //地区请求
    public void region(final String title) {
        main.setDisableClick();
        userBiz.region(new FrameListener<SenceLandRegion>() {
            @Override
            public void onSucces(SenceLandRegion result) {
                main.setEnableClick();
                main.onTypeRegionSuccess(title, result);
            }

            @Override
            public void onFaild(int code, String info) {
                ToastApp.showToast(info);
                main.setEnableClick();
            }
        });
    }

    public boolean isave() {
        return isave;
    }
}