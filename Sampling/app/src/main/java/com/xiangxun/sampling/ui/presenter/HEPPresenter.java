package com.xiangxun.sampling.ui.presenter;

import com.xiangxun.sampling.R;
import com.xiangxun.sampling.base.FrameListener;
import com.xiangxun.sampling.bean.HisExceptionPageInfo;
import com.xiangxun.sampling.bean.SimplingTargetResult;
import com.xiangxun.sampling.common.ToastApp;
import com.xiangxun.sampling.ui.biz.HEPListener;
import com.xiangxun.sampling.ui.main.SamplingHisExceptionPageActivity;
import com.xiangxun.sampling.widget.dialog.LoadDialog;


/**
 * @author zhangyh2 s 上午10:57:39 TODO 异常地块查看页面
 */
public class HEPPresenter {

    private String TAG = getClass().getSimpleName();
    private HEPListener userBiz;
    private HEPListener.HEPInterface main;
    private LoadDialog loading;

    public HEPPresenter(HEPListener.HEPInterface main) {
        this.main = main;
        this.userBiz = new HEPListener();
        loading = new LoadDialog((SamplingHisExceptionPageActivity) main);
        loading.setTitle(R.string.st_loading);
    }


    /**
     * 上传现场采集点位功能。简单参数上传。
     */
    public void getExc(String regionId) {
        userBiz.onStart(loading);
        main.setDisableClick();
        userBiz.getHEP(regionId, new FrameListener<HisExceptionPageInfo>() {
            @Override
            public void onSucces(HisExceptionPageInfo data) {
                userBiz.onStop(loading);
                main.setEnableClick();
                main.onDateSuccess(data.result);
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