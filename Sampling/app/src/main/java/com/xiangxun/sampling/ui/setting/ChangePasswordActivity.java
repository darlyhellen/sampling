package com.xiangxun.sampling.ui.setting;

import android.content.Intent;
import android.os.Bundle;
import android.text.InputType;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

import com.xiangxun.sampling.R;
import com.xiangxun.sampling.base.BaseActivity;
import com.xiangxun.sampling.base.SystemCfg;
import com.xiangxun.sampling.binder.ContentBinder;
import com.xiangxun.sampling.binder.ViewsBinder;
import com.xiangxun.sampling.common.ToastApp;
import com.xiangxun.sampling.common.Utils;
import com.xiangxun.sampling.ui.LoginActivity;
import com.xiangxun.sampling.ui.biz.ChangePassListener;
import com.xiangxun.sampling.ui.biz.ChangePassListener.ChangePassInterface;
import com.xiangxun.sampling.ui.presenter.ChangePassPresenter;
import com.xiangxun.sampling.widget.clearedit.ViewEditText;
import com.xiangxun.sampling.widget.dialog.ProgressLoadingDialog;
import com.xiangxun.sampling.widget.groupview.DetailView;
import com.xiangxun.sampling.widget.header.TitleView;


/**
 * 修改密码
 *
 * @author ChenXiangshi
 * @className Layout_userlogin
 * @date 2015-6-10 上午9:52:07
 */
@ContentBinder(R.layout.activity_change_password)
public class ChangePasswordActivity extends BaseActivity implements ChangePassInterface {
    @ViewsBinder(R.id.tv_comm_title)
    private TitleView titleView;
    @ViewsBinder(R.id.buttonloginok)
    private Button btnOk;
    @ViewsBinder(R.id.old_password)
    private DetailView oldPassword;
    @ViewsBinder(R.id.new_password)
    private DetailView newPassword;
    @ViewsBinder(R.id.renew_password)
    private DetailView reNewPassword;

    private String userid;
    private String oldpwd;
    private String oldpasswords;
    private String newpasswords;
    private String renewpasswords;
    private ProgressLoadingDialog mDialog;

    private ChangePassPresenter presenter;

    @Override
    protected void initView(Bundle savedInstanceState) {
        titleView.setTitle(R.string.modifyPwd);
        presenter = new ChangePassPresenter(this);
    }

    @Override
    protected void loadData() {
        mDialog = new ProgressLoadingDialog(ChangePasswordActivity.this, "正在修改密码，请稍候...");
        oldpwd = SystemCfg.getWhitePwd(this);
        userid = SystemCfg.getUserId(this);
        oldPassword.isEdit(true);
        oldPassword.setInfo("旧密码", " ", "请输入旧密码");
        newPassword.isEdit(true);
        newPassword.setInfo("新密码", " ", "请输入新密码");
        reNewPassword.isEdit(true);
        reNewPassword.setInfo("确认新密码", " ", "请输入新密码");

        int passwordType = InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD;
        oldPassword.setEditTextInputMode(passwordType);
        newPassword.setEditTextInputMode(passwordType);
        reNewPassword.setEditTextInputMode(passwordType);
    }


    @Override
    public void initListener() {
        btnOk.setOnClickListener(new BtnOkOnClickListener());
        titleView.setLeftBackOneListener(R.mipmap.ic_back_title, new OnClickListener() {

            @Override
            public void onClick(View arg0) {
                onBackPressed();
            }
        });
    }

    @Override
    public void onLoginSuccess() {
        //进行退出登录操作，清理登录缓存。
        SystemCfg.loginOut(this);
        Intent i = new Intent(this, LoginActivity.class);
        i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_TASK_ON_HOME);
        startActivity(i);
        onBackPressed();

    }

    @Override
    public void onLoginFailed(String info) {

    }

    @Override
    public void end() {

    }

    @Override
    public void setDisableClick() {
        btnOk.setClickable(false);
    }

    @Override
    public void setEnableClick() {
        btnOk.setClickable(true);
    }

    private class BtnOkOnClickListener implements OnClickListener {

        @Override
        public void onClick(View v) {
            Utils.hideSoftInputFromWindow(ChangePasswordActivity.this);
            oldpasswords = oldPassword.getText();
            newpasswords = newPassword.getText();
            renewpasswords = reNewPassword.getText();
            if (oldpasswords == null || oldpasswords.length() <= 0) {
                ToastApp.showToast("旧密码不能为空");
                return;
            }
            if (newpasswords == null || newpasswords.length() <= 0) {
                ToastApp.showToast("新密码不能为空");
                return;
            }
            if (renewpasswords == null || renewpasswords.length() <= 0) {
                ToastApp.showToast("确认密码不能为空");
                return;
            }
            if (!(newpasswords.equals(renewpasswords))) {
                ToastApp.showToast(R.string.pwdNotEqual);
                newPassword.setEditText("");
                reNewPassword.setEditText("");
                return;
            }
            if (!oldpasswords.equals(oldpwd)) {
                ToastApp.showToast(R.string.checkPwd);
                oldPassword.setEditText("");
                return;
            }
            presenter.ChangePass(oldpasswords,
                    newpasswords);
        }
    }
}
