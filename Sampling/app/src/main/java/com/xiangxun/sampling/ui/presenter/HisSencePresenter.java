package com.xiangxun.sampling.ui.presenter;

import com.xiangxun.sampling.R;
import com.xiangxun.sampling.base.FrameListener;
import com.xiangxun.sampling.bean.HisSencePageInfo;
import com.xiangxun.sampling.bean.SimplingTargetResult;
import com.xiangxun.sampling.common.ToastApp;
import com.xiangxun.sampling.ui.biz.HisSenceListener;
import com.xiangxun.sampling.ui.main.HisSenceActivity;
import com.xiangxun.sampling.widget.dialog.LoadDialog;


/**
 * @author zhangyh2 s 上午10:57:39 TODO 控制器 历史现场页面展示
 */
public class HisSencePresenter {

    private String TAG = getClass().getSimpleName();
    private HisSenceListener userBiz;
    private HisSenceListener.HisSenceInterface main;
    private LoadDialog loading;

    public HisSencePresenter(HisSenceListener.HisSenceInterface main) {
        this.main = main;
        this.userBiz = new HisSenceListener();
        loading = new LoadDialog((HisSenceActivity) main);
        loading.setTitle(R.string.st_loading);
    }


    /**
     * 上传现场采集点位功能。简单参数上传。
     */
    public void sencehispage(String id,String missionId,String tableName) {
        userBiz.onStart(loading);
        main.setDisableClick();
        userBiz.sencehispage(id,missionId,tableName, new FrameListener<HisSencePageInfo>() {
            @Override
            public void onSucces(HisSencePageInfo data) {
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