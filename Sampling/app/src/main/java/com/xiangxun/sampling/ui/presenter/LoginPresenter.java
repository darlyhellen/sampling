package com.xiangxun.sampling.ui.presenter;

import android.app.ActivityManager;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.text.TextUtils;
import android.view.View;

import com.xiangxun.sampling.BuildConfig;
import com.xiangxun.sampling.R;
import com.xiangxun.sampling.base.FrameListener;
import com.xiangxun.sampling.base.SystemCfg;
import com.xiangxun.sampling.base.XiangXunApplication;
import com.xiangxun.sampling.bean.LoginInfo;
import com.xiangxun.sampling.bean.ResultData;
import com.xiangxun.sampling.bean.ResultData.LoginData;
import com.xiangxun.sampling.common.ConstantStatus;
import com.xiangxun.sampling.common.NetUtils;
import com.xiangxun.sampling.common.ToastApp;
import com.xiangxun.sampling.common.Utils;
import com.xiangxun.sampling.ui.MainFragmentActivity;
import com.xiangxun.sampling.ui.biz.LoginListener;
import com.xiangxun.sampling.ui.setting.SetGuide;
import com.xiangxun.sampling.ui.setting.SystemSettingActivity;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


/**
 * @author zhangyh2 s 上午10:57:39 TODO 控制器 页面动作操作
 */
public class LoginPresenter {

    private String TAG = getClass().getSimpleName();
    private LoginListener userBiz;
    private LoginListener.LoginInterface main;

    //固定的管理员用户名密码
    private String master = "xxkj";
    private String mtpass = "000000";


    public LoginPresenter(LoginListener.LoginInterface main) {
        this.main = main;
        this.userBiz = new LoginListener();
    }


    /**
     * @param context
     * @param v       TODO点击事件在这里进行处理
     */
    public void onClickDown(Context context, View v) {
        switch (v.getId()) {
            case R.id.id_login_guide:
                if (!ActivityManager.isUserAMonkey()) {
                    Intent intent = new Intent(context, SetGuide.class);
                    context.startActivity(intent);
                }
                break;
            case R.id.id_login_set:
                if (!ActivityManager.isUserAMonkey()) {
                    Intent intent = new Intent(context, SystemSettingActivity.class);
                    intent.putExtra("isFlag", 1);
                    context.startActivity(intent);
                }
                break;
            case R.id.id_login_btn:
                if (TextUtils.isEmpty(main.getUserName()) || TextUtils.isEmpty(main.getPassword())) {
                    ToastApp.showToast("用户名密码不为空");
                    main.setEnableClick();
                    return;
                }

                if (main.getPassword().contains(" ") && main.getUserName().contains(" ")) {
                    main.setEnableClick();
                    ToastApp.showToast("用户名密码不能包含空格");
                    return;
                }
                Pattern pattern = Pattern
                        .compile("([^\\._\\w\\u4e00-\\u9fa5])*");
                Matcher matcher = pattern.matcher(main.getUserName());
                if (matcher.matches()) {
                    ToastApp.showToast("用户名不能包含表情");
                    main.setEnableClick();
                    return;
                }

                if (master.equals(main.getUserName()) && mtpass.equals(main.getPassword())) {
                    if (!ActivityManager.isUserAMonkey()) {
                        Intent intent = new Intent(context, SystemSettingActivity.class);
                        intent.putExtra("isFlag", 1);
                        context.startActivity(intent);
                    }
                    main.setEnableClick();
                    main.cleanEdit();
                } else {
                    SystemCfg.setAccount(context, main.getUserName());
                    SystemCfg.setWhitePwd(context, main.getPassword());
                    String deviceId = XiangXunApplication.getInstance().getDevId();
                    Map<String, String> params = new LinkedHashMap<String, String>();
                    params.put("imei", deviceId);
                    params.put("pwd", Utils.getCipherText(main.getPassword()));
                    params.put("account", main.getUserName());
                    SystemCfg.setCRC(context, Utils.getCRC(params));
                    login(context, main.getUserName(), main.getPassword(), deviceId);
                }
                break;
            default:
                break;
        }
    }


    public void login(final Context context, String name, String pass, String deviceId) {
        main.setDisableClick();
        userBiz.onLogin(name, pass, deviceId, new FrameListener<LoginData>() {
            @Override
            public void onSucces(LoginData data) {
                main.setEnableClick();
                String resDesc = data.resDesc;
                if (resDesc.contains("登录成功")) {
                    ResultData.UserInfos user = data.result;
                    if (user != null) {
                        main.onLoginSuccess();
                    } else {
                        main.onLoginFailed("登录失败，请检查网络！");
                    }

                } else if (resDesc.contains("账户名错误")) {
                    main.onLoginFailed("账户名错误");
                } else if (resDesc.contains("账户密码错误")) {
                    main.onLoginFailed("账户密码错误");
                } else if (resDesc.contains("非法设备")) {
                    main.onLoginFailed("非法设备");
                } else {
                    main.onLoginFailed("登录失败，请检查网络！");
                }
            }

            @Override
            public void onFaild(int code, String info) {
                main.setEnableClick();
                main.onLoginFailed(info);
            }
        });
    }
}