package com.xiangxun.sampling.ui.presenter;

import com.xiangxun.sampling.base.FrameListener;
import com.xiangxun.sampling.bean.PlannningData.ResultData;
import com.xiangxun.sampling.common.SharePreferHelp;
import com.xiangxun.sampling.common.ToastApp;
import com.xiangxun.sampling.ui.biz.SamplingPlanningListener;
import com.xiangxun.sampling.ui.biz.SamplingPlanningListener.SamplingPlanningInterface;


/**
 * @author zhangyh2 s 上午10:57:39 TODO 控制器 页面动作操作
 */
public class SamplingPlanningPresenter {

    private String TAG = getClass().getSimpleName();
    private SamplingPlanningListener biz;
    private SamplingPlanningInterface view;

    public SamplingPlanningPresenter(SamplingPlanningInterface view) {
        this.view = view;
        this.biz = new SamplingPlanningListener();
    }


    public void planning(String time) {

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
            }

            @Override
            public void onFaild(int code, String info) {
                ToastApp.showToast(info);
                //请求失败也加载缓存
                view.onLoginFailed();
            }
        });

    }

}