package com.xiangxun.sampling.ui.presenter;

import com.xiangxun.sampling.R;
import com.xiangxun.sampling.base.FrameListener;
import com.xiangxun.sampling.bean.PlannningData.ResultData;
import com.xiangxun.sampling.common.SharePreferHelp;
import com.xiangxun.sampling.common.ToastApp;
import com.xiangxun.sampling.ui.biz.SamplingHistoryListener;
import com.xiangxun.sampling.ui.biz.SamplingHistoryListener.SamplingHistoryInterface;
import com.xiangxun.sampling.ui.main.SamplingHistoryActivity;
import com.xiangxun.sampling.ui.main.SamplingTargetActivity;
import com.xiangxun.sampling.widget.dialog.LoadDialog;


/**
 * @author zhangyh2 s 上午10:57:39 TODO 控制器 历史采样列表展示
 */
public class SamplingHistoryPresenter {

    private String TAG = getClass().getSimpleName();
    private SamplingHistoryListener biz;
    private SamplingHistoryInterface view;
    private LoadDialog loading;

    public SamplingHistoryPresenter(SamplingHistoryInterface view) {
        this.view = view;
        this.biz = new SamplingHistoryListener();
        loading = new LoadDialog((SamplingHistoryActivity) view);
        loading.setTitle(R.string.st_loading);
    }


    public void getHistory(int currentPage, String hisName, String samplyName,String regionId, String loaction) {
        biz.onStart(loading);
        biz.getHistory(currentPage, hisName,samplyName,regionId,loaction, new FrameListener<ResultData>() {
            @Override
            public void onSucces(ResultData result) {
                biz.onStop(loading);
                view.onLoginSuccess(result);
            }

            @Override
            public void onFaild(int code, String info) {
                biz.onStop(loading);
                ToastApp.showToast(info);
                view.onLoginFailed();
            }
        });

    }

}