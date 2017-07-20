package com.xiangxun.sampling.ui.presenter;

import android.app.ActivityManager;
import android.content.Context;
import android.content.Intent;
import android.view.View;

import com.xiangxun.sampling.BuildConfig;
import com.xiangxun.sampling.R;
import com.xiangxun.sampling.base.BaseActivity;
import com.xiangxun.sampling.base.FrameListener;
import com.xiangxun.sampling.base.SystemCfg;
import com.xiangxun.sampling.base.XiangXunApplication;
import com.xiangxun.sampling.bean.LoginInfo;
import com.xiangxun.sampling.bean.PlannningData;
import com.xiangxun.sampling.bean.ResultData;
import com.xiangxun.sampling.bean.ResultData.LoginData;
import com.xiangxun.sampling.common.NetUtils;
import com.xiangxun.sampling.common.retrofit.paramer.SenceParamer;
import com.xiangxun.sampling.ui.MainFragmentActivity;
import com.xiangxun.sampling.ui.biz.LoginListener;
import com.xiangxun.sampling.ui.biz.SenceListener;
import com.xiangxun.sampling.ui.setting.SetGuide;
import com.xiangxun.sampling.ui.setting.SystemSettingActivity;
import com.xiangxun.sampling.widget.dialog.LoadDialog;


/**
 * @author zhangyh2 s 上午10:57:39 TODO 控制器 页面动作操作
 */
public class SencePresenter {

    private String TAG = getClass().getSimpleName();
    private SenceListener userBiz;
    private SenceListener.SenceInterface main;
    private LoadDialog loading;

    public SencePresenter(SenceListener.SenceInterface main) {
        this.main = main;
        this.userBiz = new SenceListener();
        loading = new LoadDialog((BaseActivity) main);
        loading.setTitle(R.string.st_loading);
    }


    /**
     * 上传现场采集点位功能。简单参数上传。
     */
    public void sampling(PlannningData.Point point) {
        userBiz.onStart(loading);
        main.setDisableClick();
        SenceParamer paramer = new SenceParamer(point.id, point.schemeId, main.getaddress(), main.getlongitude(), main.getlatitude(), main.gettype(), main.getname(), main.getparams(), main.getproject(), main.getother());

        userBiz.upSampling(paramer, new FrameListener<String>() {
            @Override
            public void onSucces(String data) {
                userBiz.onStop(loading);
                main.setEnableClick();
            }

            @Override
            public void onFaild(int code, String info) {
                userBiz.onStop(loading);
                main.setEnableClick();
                main.onLoginFailed(info);
            }
        });
    }
}