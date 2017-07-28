package com.xiangxun.sampling.ui.presenter;

import com.xiangxun.sampling.base.FrameListener;
import com.xiangxun.sampling.bean.VerisonInfo;
import com.xiangxun.sampling.ui.biz.VersionListener;
import com.xiangxun.sampling.widget.dialog.LoadDialog;


/**
 * @author zhangyh2 s 上午10:57:39 TODO 控制器 版本更新
 */
public class VersionPresenter {

    private String TAG = getClass().getSimpleName();
    private VersionListener userBiz;
    private VersionListener.VersionInterface main;
    private LoadDialog loading;

    public VersionPresenter(VersionListener.VersionInterface main) {
        this.main = main;
        this.userBiz = new VersionListener();
    }


    /**
     * 上传现场采集点位功能。简单参数上传。
     */
    public void findVersion(String version) {
        userBiz.findVersion(version, new FrameListener<VerisonInfo>() {
            @Override
            public void onSucces(VerisonInfo data) {
                main.onVersionSuccess(data.result);
            }

            @Override
            public void onFaild(int code, String info) {
                main.onVersionFailed(info);
            }
        });
    }
}