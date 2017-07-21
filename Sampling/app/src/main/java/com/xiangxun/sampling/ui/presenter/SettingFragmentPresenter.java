package com.xiangxun.sampling.ui.presenter;

import com.xiangxun.sampling.R;
import com.xiangxun.sampling.base.BaseActivity;
import com.xiangxun.sampling.base.FrameListener;
import com.xiangxun.sampling.bean.ResultData.LoginData;
import com.xiangxun.sampling.common.ToastApp;
import com.xiangxun.sampling.ui.biz.SettingFragmentListener;
import com.xiangxun.sampling.ui.setting.SettingFragment;
import com.xiangxun.sampling.widget.dialog.LoadDialog;


/**
 * @author zhangyh2 s 上午10:57:39 TODO 控制器 页面动作操作
 */
public class SettingFragmentPresenter {

    private String TAG = getClass().getSimpleName();
    private SettingFragmentListener userBiz;
    private SettingFragmentListener.SettingFragmentInterface main;
    private LoadDialog loading;

    public SettingFragmentPresenter(SettingFragmentListener.SettingFragmentInterface main) {
        this.main = main;
        this.userBiz = new SettingFragmentListener();
        loading = new LoadDialog(((SettingFragment) main).getActivity());
        loading.setTitle(R.string.st_loading);
    }


    public void loginout() {
        userBiz.onStart(loading);
        userBiz.loginout(new FrameListener<LoginData>() {
            @Override
            public void onSucces(LoginData data) {
                userBiz.onStop(loading);
                main.onLoginSuccess();
            }

            @Override
            public void onFaild(int code, String info) {
                userBiz.onStop(loading);
                ToastApp.showToast(info);
                main.onLoginFailed(info);
            }
        });
    }
}