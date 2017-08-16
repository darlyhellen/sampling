package com.xiangxun.sampling.ui.presenter;

import com.xiangxun.sampling.R;
import com.xiangxun.sampling.base.FrameListener;
import com.xiangxun.sampling.common.ToastApp;
import com.xiangxun.sampling.common.Utils;
import com.xiangxun.sampling.ui.biz.ChangePassListener;
import com.xiangxun.sampling.ui.setting.ChangePasswordActivity;
import com.xiangxun.sampling.widget.dialog.LoadDialog;


/**
 * @author zhangyh2 s 上午10:57:39 TODO 控制器 修改密码
 */
public class ChangePassPresenter {

    private String TAG = getClass().getSimpleName();
    private ChangePassListener userBiz;
    private ChangePassListener.ChangePassInterface main;
    private LoadDialog loading;

    public ChangePassPresenter(ChangePassListener.ChangePassInterface main) {
        this.main = main;
        this.userBiz = new ChangePassListener();
        loading = new LoadDialog((ChangePasswordActivity) main);
        loading.setTitle(R.string.st_loading);
    }


    public void ChangePass(String oldpasswords, String newpasswords) {
        userBiz.onStart(loading);
        main.setDisableClick();
        userBiz.ChangePass(Utils.getCipherText(oldpasswords), Utils.getCipherText(newpasswords), new FrameListener<String>() {
            @Override
            public void onSucces(String data) {
                main.setEnableClick();
                userBiz.onStop(loading);
                main.onLoginSuccess();
            }

            @Override
            public void onFaild(int code, String info) {
                main.setEnableClick();
                userBiz.onStop(loading);
                ToastApp.showToast(info);
            }
        });
    }
}