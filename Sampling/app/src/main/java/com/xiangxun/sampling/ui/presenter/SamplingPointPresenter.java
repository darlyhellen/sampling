package com.xiangxun.sampling.ui.presenter;

import com.xiangxun.sampling.base.FrameListener;
import com.xiangxun.sampling.bean.PlannningData.ResultPointData;
import com.xiangxun.sampling.common.SharePreferHelp;
import com.xiangxun.sampling.common.ToastApp;
import com.xiangxun.sampling.ui.biz.SamplingPointListener;
import com.xiangxun.sampling.ui.biz.SamplingPointListener.SamplingPointInterface;


/**
 * @author zhangyh2 s 上午10:57:39 TODO 控制器 页点位列表操作
 */
public class SamplingPointPresenter {

    private String TAG = getClass().getSimpleName();
    private SamplingPointListener biz;
    private SamplingPointInterface view;

    public SamplingPointPresenter(SamplingPointInterface view) {
        this.view = view;
        this.biz = new SamplingPointListener();
    }


    public void point(final String id, String time) {
        biz.postPoint(id, time, new FrameListener<ResultPointData>() {
            @Override
            public void onSucces(ResultPointData result) {
                if (result.resCode == 2000) {
                    //使用缓存
                    view.onLoginFailed();
                } else if (result.resCode == 1000) {
                    SharePreferHelp.putValue(id, result);
                    view.onLoginSuccess(result.result);
                }
            }

            @Override
            public void onFaild(int code, String info) {
                ToastApp.showToast(info);
                view.onLoginFailed();
            }
        });

    }

}