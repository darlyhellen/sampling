package com.xiangxun.sampling.ui.presenter;

import com.xiangxun.sampling.R;
import com.xiangxun.sampling.base.BaseActivity;
import com.xiangxun.sampling.base.FrameListener;
import com.xiangxun.sampling.bean.PlannningData.ResultData;
import com.xiangxun.sampling.bean.SamplingSenceGroup;
import com.xiangxun.sampling.common.SharePreferHelp;
import com.xiangxun.sampling.common.ToastApp;
import com.xiangxun.sampling.ui.biz.SamplingPlanningListener;
import com.xiangxun.sampling.ui.biz.SamplingPlanningListener.SamplingPlanningInterface;
import com.xiangxun.sampling.widget.dialog.LoadDialog;

import java.util.List;


/**
 * @author zhangyh2 s 上午10:57:39 TODO 控制器 页面动作操作
 */
public class SamplingPlanningPresenter {

    private String TAG = getClass().getSimpleName();
    private SamplingPlanningListener biz;
    private SamplingPlanningInterface view;

    private LoadDialog loading;


    public SamplingPlanningPresenter(SamplingPlanningInterface view) {
        this.view = view;
        this.biz = new SamplingPlanningListener();
        loading = new LoadDialog((BaseActivity) view);
        loading.setTitle(R.string.st_loading);
    }


    public void planning(String time) {
        biz.onStart(loading);
        biz.getPlanning(time, new FrameListener<ResultData>() {
            @Override
            public void onSucces(ResultData result) {
                if (result.resCode == 2000) {
                    //使用缓存
                    view.onLoginFailed();
                } else if (result.resCode == 1000) {
                    SharePreferHelp.putValue("ResultData", result);
                    view.onLoginSuccess(result.result);
                }
                biz.onStop(loading);
            }

            @Override
            public void onFaild(int code, String info) {
                ToastApp.showToast(info);
                //请求失败也加载缓存
                view.onLoginFailed();
                biz.onStop(loading);
            }
        });

    }

    public void findPlanning() {
        biz.onStart(loading);
        biz.findPlanning( new FrameListener<List<SamplingSenceGroup.SenceGroup>>() {
            @Override
            public void onSucces(List<SamplingSenceGroup.SenceGroup> result) {
                view.onLoginSuccessV1(result);
                biz.onStop(loading);
            }

            @Override
            public void onFaild(int code, String info) {
                ToastApp.showToast(info);
                //请求失败也加载缓存
                view.onLoginFailed();
                biz.onStop(loading);
            }
        });

    }
}