package com.xiangxun.sampling.ui.presenter;

import android.text.TextUtils;

import com.xiangxun.sampling.R;
import com.xiangxun.sampling.base.BaseActivity;
import com.xiangxun.sampling.base.FrameListener;
import com.xiangxun.sampling.bean.PlannningData;
import com.xiangxun.sampling.bean.SenceInfo;
import com.xiangxun.sampling.bean.SenceLandRegion;
import com.xiangxun.sampling.common.SharePreferHelp;
import com.xiangxun.sampling.common.ToastApp;
import com.xiangxun.sampling.common.retrofit.PontCacheHelper;
import com.xiangxun.sampling.db.MediaSugar;
import com.xiangxun.sampling.db.SenceSamplingSugar;
import com.xiangxun.sampling.ui.biz.SenceListener;
import com.xiangxun.sampling.widget.dialog.LoadDialog;
import com.xiangxun.sampling.widget.timeselecter.option.TextUtil;

import java.util.List;


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

    public void checkWhichToSamply(PlannningData.Scheme planning){
        if (planning==null){
            ToastApp.showToast("参数传递错误");
            return;
        }
        if (planning.sampleCode.equals("BJTR")){//背景土壤
            main.BackSoilSamply();
        }else if (planning.sampleCode.equals("SD")){//农作物
            main.FarmSamply();
        }else if (planning.sampleCode.equals("NTTR")){//农田土壤
            main.SoilSamply();
        }else if (planning.sampleCode.equals("WATER")){//水采样
            main.WaterSamply();
        }else if (planning.sampleCode.equals("DQ")){//大气沉降物
            main.OxsSamply();
        }else if (planning.sampleCode.equals("FL")){//肥料
            main.ManureSamply();
        }else {
            //其他错误的采样信息
            main.NullSamply();
        }
    }


    /**
     * 上传现场采集点位功能。简单参数上传。
     */
    public void sampling(final PlannningData.Scheme planning, final SenceSamplingSugar point) {
        if (planning==null){
            ToastApp.showToast("参数传递错误");
            return;
        }
        if (point == null) {
            ToastApp.showToast("点位信息传递错误");
            return;
        }
        if (TextUtils.isEmpty(point.getMissionId())) {
            ToastApp.showToast("任务ID不能为空");
            return;
        }
        if (TextUtils.isEmpty(point.getSamplingCode())) {
            ToastApp.showToast("采样类型不能为空");
            return;
        }
        if (planning.sampleCode.equals("BJTR")){//背景土壤(判断参数)
            if (TextUtils.isEmpty(point.getSamplingType())) {
                ToastApp.showToast("采样类型不能为空");
                return;
            }
            if (TextUtil.isEmpty(point.getOtherType())){
                ToastApp.showToast("墙土来源不能为空");
                return;
            }
            if (TextUtil.isEmpty(point.getAmbient())){
                ToastApp.showToast("周围环境不能为空");
                return;
            }
            if (TextUtils.isEmpty(point.getYears())) {
                ToastApp.showToast("成墙年份不能为空");
                return;
            }
        }else if (planning.sampleCode.equals("SD")){//农作物(判断参数)
            if (TextUtils.isEmpty(point.getSamplingType())) {
                ToastApp.showToast("采样类型不能为空");
                return;
            }
            if (TextUtils.isEmpty(point.getPosition())) {
                ToastApp.showToast("采样部位不能为空");
                return;
            }
        }else if (planning.sampleCode.equals("NTTR")){//农田土壤(判断参数)
            if (TextUtils.isEmpty(point.getDepth())) {
                ToastApp.showToast("采样深度不能为空");
                return;
            }
            if (TextUtils.isEmpty(point.getSamplingType())) {
                ToastApp.showToast("采样类型不能为空");
                return;
            }
        }else if (planning.sampleCode.equals("WATER")){//水采样(判断参数)
            if (TextUtil.isEmpty(point.getOtherType())){
                ToastApp.showToast("样品来源不能为空");
                return;
            }
            if (TextUtils.isEmpty(point.getRiversName())) {
                ToastApp.showToast("河流名称不能为空");
                return;
            }
            if (TextUtils.isEmpty(point.getSamplingType())) {
                ToastApp.showToast("采样类型不能为空");
                return;
            }
        }else if (planning.sampleCode.equals("DQ")){//大气沉降物(判断参数)

            if (TextUtil.isEmpty(point.getOtherType())) {
                ToastApp.showToast("采样点位不能为空");
                return;
            }
            if (TextUtils.isEmpty(point.getContainerVolume())) {
                ToastApp.showToast("容器体积不能为空");
                return;
            }
            if (TextUtils.isEmpty(point.getCollectVolume())) {
                ToastApp.showToast("收集量不能为空");
                return;
            }
        }else if (planning.sampleCode.equals("FL")){//肥料(判断参数)
            if (TextUtils.isEmpty(point.getShopName())) {
                ToastApp.showToast("店面名称不能为空");
                return;
            }
            if (TextUtils.isEmpty(point.getDealManure())) {
                ToastApp.showToast("经营肥料不能为空");
                return;
            }
            if (TextUtils.isEmpty(point.getShopkeeper())) {
                ToastApp.showToast("店主姓名不能为空");
                return;
            }
            if (TextUtils.isEmpty(point.getTel())) {
                ToastApp.showToast("店主联系电话不能为空");
                return;
            }
        }else {
            //其他错误的采样信息
        }
        if (!planning.sampleCode.equals("DQ")) {
            if (TextUtils.isEmpty(point.getRegion_id())) {
                ToastApp.showToast("地址信息不能为空");
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
        }
        userBiz.onStart(loading);
        userBiz.senceSampy(point, new FrameListener<List<SenceInfo.SenceObj>>() {
            @Override
            public void onSucces(List<SenceInfo.SenceObj> data) {
                for (SenceInfo.SenceObj obj:data) {
                    //将返回的数据和传递的数据进行合并并保存进数据库。
                    point.setSamplingId(obj.id);
                    point.setCode(obj.code);
                    point.setRegion_id(obj.regionName);
                    //图片信息建表
                    if (point.getImages() != null && point.getImages().size() > 1) {
                        point.getImages().remove(point.getImages().size() - 1);
                        for (String image : point.getImages()) {
                            MediaSugar sugar = new MediaSugar();
                            sugar.setSamplingId(point.getSamplingId());
                            sugar.setUrl(image);
                            sugar.setType("image");
                            sugar.setSamplingCode(obj.samplingCode);
                            sugar.save();
                        }
                        //有圖片信息進行存儲
                        point.save();
                    }
                    //视频信息建表
                    if (point.getVideos() != null && point.getVideos().size() > 1) {
                        point.getVideos().remove(point.getVideos().size() - 1);
                        for (String image : point.getVideos()) {
                            MediaSugar sugar = new MediaSugar();
                            sugar.setSamplingId(point.getSamplingId());
                            sugar.setUrl(image);
                            sugar.setType("video");
                            sugar.setSamplingCode(obj.samplingCode);
                            sugar.save();
                        }
                        //有視頻信息進行存儲
                        point.save();
                    }
                }
                userBiz.onStop(loading);
                main.onLoginSuccess(data);
            }

            @Override
            public void onFaild(int code, String info) {
                userBiz.onStop(loading);
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
    //类型选项
    public void getTypes(final String title, String code, String missionId) {
        int mold =0;
        if (code.equals("BJTR")||code.equals("NTTR")||code.equals("SD")||code.equals("WATER")){//农田土壤 农作物 水采样
            mold= 2;
        }
        main.setDisableClick();
        userBiz.getTypese(code,mold,missionId,new FrameListener<SenceLandRegion>() {
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

    //其他选项
    public void getOtherType(final String title, String code, String missionId) {
        int mold =0;
        if (code.equals("BJTR")){//背景土壤
            mold= 3;
        }else if (code.equals("WATER")){
            mold= 1;
        }else if (code.equals("DQ")){
            mold= 4;
        }
        main.setDisableClick();
        userBiz.getTypese(code,mold,missionId,new FrameListener<SenceLandRegion>() {
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