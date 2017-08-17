package com.xiangxun.sampling.ui.presenter;

import com.xiangxun.sampling.R;
import com.xiangxun.sampling.base.FrameListener;
import com.xiangxun.sampling.bean.HisExceptionInfo;
import com.xiangxun.sampling.bean.SimplingTargetResult;
import com.xiangxun.sampling.common.ToastApp;
import com.xiangxun.sampling.ui.biz.HisExceptionListener;
import com.xiangxun.sampling.ui.main.SamplingHisExceptionActivity;
import com.xiangxun.sampling.widget.dialog.LoadDialog;


/**
 * @author zhangyh2 s 上午10:57:39 TODO 控制器 指标查询页面操作
 */
public class HisExceptionPresenter {

    private String TAG = getClass().getSimpleName();
    private HisExceptionListener userBiz;
    private HisExceptionListener.HisExceptionInterface main;
    private LoadDialog loading;

    public HisExceptionPresenter(HisExceptionListener.HisExceptionInterface main) {
        this.main = main;
        this.userBiz = new HisExceptionListener();
        loading = new LoadDialog((SamplingHisExceptionActivity) main);
        loading.setTitle(R.string.st_loading);
    }


    /**
     * 列表展示。
     */
    public void hisList(int page,String location,String regionId) {
        userBiz.onStart(loading);
        main.setDisableClick();
        userBiz.hisExcList(page,location,regionId,new FrameListener<HisExceptionInfo>() {
            @Override
            public void onSucces(HisExceptionInfo data) {
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