package com.xiangxun.sampling.ui.presenter;

import com.xiangxun.sampling.base.FrameListener;
import com.xiangxun.sampling.bean.PlannningData.ResultData;
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


    public void point(String id, String time) {
        biz.postPoint(id, time, new FrameListener<ResultData>() {
            @Override
            public void onSucces(ResultData result) {
                view.onLoginSuccess(result.result);
            }

            @Override
            public void onFaild(int code, String info) {
                view.onLoginFailed(info);
            }
        });

    }

}