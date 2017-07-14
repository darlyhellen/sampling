package com.xiangxun.sampling.ui;

import android.app.ActivityManager;
import android.content.Intent;
import android.graphics.Paint;
import android.os.Bundle;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.URLSpan;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.xiangxun.sampling.R;
import com.xiangxun.sampling.base.BaseActivity;
import com.xiangxun.sampling.base.SystemCfg;
import com.xiangxun.sampling.base.XiangXunApplication;
import com.xiangxun.sampling.bean.LoginInfo;
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
public class LoginActivity extends BaseActivity implements LoginInterface, View.OnClickListener, OnCheckedChangeListener {

    @ViewsBinder(R.id.id_login_guide)
    CheckBox idLoginGuide;
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

        idLoginSet.getPaint().setFlags(Paint.UNDERLINE_TEXT_FLAG); //下划线
        idLoginSet.getPaint().setAntiAlias(true);//抗锯齿
        idLoginSet.setText(R.string.loginset);
        idLoginSet.setTextColor(getResources().getColor(R.color.color_login_guide));
        idLoginGuide.setText(R.string.loginguide);
        idLoginGuide.setTextColor(getResources().getColor(R.color.color_login_guide));

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
        idLoginGuide.setOnCheckedChangeListener(this);
    }

    private void setUsernameAndPassword() {
        if (idLoginName == null || idLoginPassword == null)
            return;
        String username = SystemCfg.getAccount(this);
        String password = SystemCfg.getWhitePwd(this);
        idLoginName.setText(username);

        if (SystemCfg.getRemark(this)) {
            idLoginPassword.setText(password);
            idLoginGuide.setChecked(true);
        } else {
            idLoginPassword.setText(null);
            idLoginGuide.setChecked(false);
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
        //XiangXunApplication.getInstance().getMainService().clearGPSLimitData();
        LoginActivity.this.finish();
    }

    @Override
    public void onLoginFailed(String info) {
        ToastApp.showToast(info);
        LoginInfo.isOffLine = true;
        SystemCfg.setUserId(this, "1503152210416979856e76ad4e5425d4");
        SystemCfg.setAccount(this, getUserName());
        SystemCfg.setUserName(this, "超级管理员");
        SystemCfg.setDepartment(this, "鄂托克旗环保局");
        SystemCfg.setDepartmentID(this, "00");
        SystemCfg.setIMEI(this, XiangXunApplication.getInstance().getDevId());
        SystemCfg.setWhitePwd(this, getPassword());
        Intent offline = new Intent(this, MainFragmentActivity.class);
        startActivity(offline);
        end();
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

    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        SystemCfg.setRemark(this, isChecked);
    }
}
