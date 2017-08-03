package com.xiangxun.sampling.ui.presenter;

import com.xiangxun.sampling.base.FrameListener;
import com.xiangxun.sampling.bean.PlannningData.ResultData;
import com.xiangxun.sampling.common.SharePreferHelp;
import com.xiangxun.sampling.common.ToastApp;
import com.xiangxun.sampling.ui.biz.SamplingHistoryListener;
import com.xiangxun.sampling.ui.biz.SamplingHistoryListener.SamplingHistoryInterface;


/**
 * @author zhangyh2 s 上午10:57:39 TODO 控制器 历史采样列表展示
 */
public class SamplingHistoryPresenter {

    private String TAG = getClass().getSimpleName();
    private SamplingHistoryListener biz;
    private SamplingHistoryInterface view;

    public SamplingHistoryPresenter(SamplingHistoryInterface view) {
        this.view = view;
        this.biz = new SamplingHistoryListener();
    }


    public void getHistory(String hisName) {

        biz.getHistory(hisName, new FrameListener<ResultData>() {
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