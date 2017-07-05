package com.xiangxun.sampling.ui;

import android.app.ActivityManager;
import android.content.Intent;
import android.os.Bundle;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.URLSpan;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.xiangxun.sampling.R;
import com.xiangxun.sampling.base.BaseActivity;
import com.xiangxun.sampling.base.SystemCfg;
import com.xiangxun.sampling.base.XiangXunApplication;
import com.xiangxun.sampling.binder.ContentBinder;
import com.xiangxun.sampling.binder.ViewsBinder;
import com.xiangxun.sampling.common.NetUtils;
import com.xiangxun.sampling.common.ToastApp;
import com.xiangxun.sampling.common.dlog.DLog;
import com.xiangxun.sampling.service.MainService;
import com.xiangxun.sampling.ui.biz.LoginListener.LoginInterface;
import com.xiangxun.sampling.ui.presenter.LoginPresenter;
import com.xiangxun.sampling.widget.XSubButton;
import com.xiangxun.sampling.widget.clearedit.ClearEditText;

/**
 * @TODO:登录页面展示
 */
@ContentBinder(R.layout.activity_login)
public class LoginActivity extends BaseActivity implements LoginInterface, View.OnClickListener {

    @ViewsBinder(R.id.id_login_guide)
    TextView idLoginGuide;
    @ViewsBinder(R.id.id_login_set)
    TextView idLoginSet;
    @ViewsBinder(R.id.id_login_btn)
    XSubButton idLoginBtn;
    @ViewsBinder(R.id.id_login_name)
    ClearEditText idLoginName;
    @ViewsBinder(R.id.id_login_password)
    ClearEditText idLoginPassword;
    @ViewsBinder(R.id.id_login_bg)
    RelativeLayout titleBg;

    private LoginPresenter presenter;


    @Override
    protected void initView(Bundle savedInstanceState) {
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
        presenter = new LoginPresenter(this);
    }

    @Override
    public void loadData() {
        titleBg.setLayoutParams(new RelativeLayout.LayoutParams(SystemCfg.getWidth(this), (int) (SystemCfg.getWidth(this) / 1.47)));
        setUpLoginAnim();
        setUsernameAndPassword();
        if (!NetUtils.isNetworkAvailable(LoginActivity.this)) {
            if (SystemCfg.getUserId(this) != null && !SystemCfg.getUserId(this).equals("")) {
                ToastApp.showToast(R.string.loginoffline);
            } else {
                ToastApp.showToast(R.string.netnotavailable);
                idLoginBtn.setEnabled(false);
            }
        }
        idLoginBtn.setViewInit(R.string.mine_login_login, R.string.mine_login_loginning, idLoginName, idLoginPassword);    //, mSettings, mTakePic);

        String server_set_str = getResources().getString(R.string.loginset);
        SpannableString ss = new SpannableString(server_set_str);
        ss.setSpan(new URLSpan(server_set_str), 0, server_set_str.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        idLoginSet.setText(ss);
        String loginGuide_str = getResources().getString(R.string.loginguide);
        SpannableString guide = new SpannableString(loginGuide_str);
        guide.setSpan(new URLSpan(loginGuide_str), 0, loginGuide_str.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        idLoginGuide.setText(guide);

        if (ActivityManager.isUserAMonkey()) {
            idLoginName.setEnabled(false);
            idLoginPassword.setEnabled(false);
        }
        Intent mainService = new Intent(this, MainService.class);
        startService(mainService);
    }

    @Override
    public void initListener() {
        idLoginBtn.setOnClickListener(this);
        idLoginSet.setOnClickListener(this);
        idLoginGuide.setOnClickListener(this);
    }

    private void setUsernameAndPassword() {
        if (idLoginName == null || idLoginPassword == null)
            return;
        String username = SystemCfg.getAccount(this);
        String password = SystemCfg.getWhitePwd(this);
        if (username.length() > 1 && password.length() > 1) {
            idLoginName.setText(username);
            idLoginPassword.setText(password);
        }
    }

    private void setUpLoginAnim() {
        Animation animation = AnimationUtils.loadAnimation(LoginActivity.this, R.anim.login_up);
        animation.setAnimationListener(new Animation.AnimationListener() {

            @Override
            public void onAnimationStart(Animation arg0) {
            }

            @Override
            public void onAnimationRepeat(Animation arg0) {
            }

            @Override
            public void onAnimationEnd(Animation arg0) {
            }
        });
    }

    private boolean checkInputValidation() {
        if (idLoginName == null || idLoginName.getText().toString().trim().length() < 1) {
            idLoginBtn.setNormal();
            return false;
        }

        if (idLoginPassword == null || idLoginPassword.getText().toString().trim().length() < 1) {
            idLoginBtn.setNormal();
            return false;
        }
        return true;
    }


    @Override
    public void onClick(View v) {
        presenter.onClickDown(this, v);
    }

    @Override
    public void onLoginSuccess() {
        Intent intent = new Intent(LoginActivity.this, MainFragmentActivity.class);
        startActivity(intent);
        //XiangXunApplication.getInstance().startPushService();
        XiangXunApplication.getInstance().getMainService().clearGPSLimitData();
        LoginActivity.this.finish();
    }

    @Override
    public void onLoginFailed(String info) {
        ToastApp.showToast(info);
    }

    @Override
    public String getUserName() {
        return idLoginName.getText().toString().trim();
    }

    @Override
    public String getPassword() {
        return idLoginPassword.getText().toString().trim();
    }

    @Override
    public void end() {
        onBackPressed();
    }

    @Override
    public void setDisableClick() {
        idLoginBtn.setEnabled(false);
    }

    @Override
    public void setEnableClick() {
        idLoginBtn.setEnabled(true);
        idLoginBtn.setNormal();
    }

    @Override
    protected void onStart() {
        DLog.d(getClass().getSimpleName(), "onStart()");
        super.onStart();
    }

    @Override
    protected void onRestart() {
        DLog.d(getClass().getSimpleName(), "onRestart()");
        super.onRestart();
    }

    @Override
    protected void onResume() {
        DLog.d(getClass().getSimpleName(), "onResume()");
        super.onResume();
    }

    @Override
    protected void onPause() {
        DLog.d(getClass().getSimpleName(), "onPause()");
        super.onPause();
    }

    @Override
    protected void onStop() {
        DLog.d(getClass().getSimpleName(), "onStop()");
        super.onStop();
    }

    @Override
    protected void onDestroy() {
        DLog.d(getClass().getSimpleName(), "onDestroy()");
        super.onDestroy();
    }
}
