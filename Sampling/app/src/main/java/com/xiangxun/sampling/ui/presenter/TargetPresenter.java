package com.xiangxun.sampling.ui.presenter;

import com.xiangxun.sampling.R;
import com.xiangxun.sampling.base.FrameListener;
import com.xiangxun.sampling.bean.SimplingTargetResult;
import com.xiangxun.sampling.common.ToastApp;
import com.xiangxun.sampling.common.retrofit.paramer.AnaylistParamer;
import com.xiangxun.sampling.ui.biz.TargetListener;
import com.xiangxun.sampling.ui.main.SamplingTargetActivity;
import com.xiangxun.sampling.widget.dialog.LoadDialog;


/**
 * @author zhangyh2 s 上午10:57:39 TODO 控制器 指标查询页面操作
 */
public class TargetPresenter {

    private String TAG = getClass().getSimpleName();
    private TargetListener userBiz;
    private TargetListener.TargetInterface main;
    private LoadDialog loading;

    public TargetPresenter(TargetListener.TargetInterface main) {
        this.main = main;
        this.userBiz = new TargetListener();
        loading = new LoadDialog((SamplingTargetActivity) main);
        loading.setTitle(R.string.st_loading);
    }


    /**
     * 上传现场采集点位功能。简单参数上传。
     */
    public void analysis(int page, String address, String regionId, String sampleOver, String sampleName, String sampleTarget) {
        userBiz.onStart(loading);
        main.setDisableClick();
        AnaylistParamer paramers = new AnaylistParamer(page, address, regionId, sampleOver, sampleName, sampleTarget);
        userBiz.analysis(paramers, new FrameListener<SimplingTargetResult>() {
            @Override
            public void onSucces(SimplingTargetResult data) {
                userBiz.onStop(loading);
                main.setEnableClick();
                main.onDateSuccess(data);
            }

            @Override
            public void onFaild(int code, String info) {
                userBiz.onStop(loading);
                main.setEnableClick();
                main.onDateFailed(info);
                ToastApp.showToast(info);
            }
        });
    }
}