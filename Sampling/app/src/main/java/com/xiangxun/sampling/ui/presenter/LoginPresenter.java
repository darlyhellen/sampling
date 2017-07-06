package com.xiangxun.sampling.ui.presenter;

import android.app.ActivityManager;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.view.View;

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


/**
 * @author zhangyh2 s 上午10:57:39 TODO 控制器 页面动作操作
 */
public class LoginPresenter {

    private String TAG = getClass().getSimpleName();
    private LoginListener userBiz;
    private LoginListener.LoginInterface main;


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
                //if (!NetUtils.isNetworkAvailable(context) && SystemCfg.getUserId(context) != null && !SystemCfg.getUserId(context).equals("")) {
                LoginInfo.isOffLine = true;
                SystemCfg.setUserId(context, "00001");
                SystemCfg.setAccount(context, main.getUserName());
                SystemCfg.setUserName(context, "管理员");
                SystemCfg.setDepartment(context, "研究所");
                SystemCfg.setDepartmentID(context, "101");
                SystemCfg.setUserImage(context,"http://img0.imgtn.bdimg.com/it/u=852482742,684232062&fm=26&gp=0.jpg");
                SystemCfg.setIMEI(context, XiangXunApplication.getInstance().getDevId());
                SystemCfg.setWhitePwd(context, main.getPassword());
                Intent offline = new Intent(context, MainFragmentActivity.class);
                context.startActivity(offline);
                main.end();
//                } else {
//                    LoginInfo.isOffLine = false;
//                    String deviceId = XiangXunApplication.getInstance().getDevId();
//                    //String password = Utils.getCipherText(main.getPassword());
//                    login(context, main.getUserName(), main.getPassword(), deviceId);
//                }
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
                        user.imei = XiangXunApplication.getInstance().getDevId();
                        SystemCfg.setUserId(context, null != user.id ? user.id.toString() : "");
                        SystemCfg.setAccount(context, null != user.account ? user.account.toString() : "");
                        SystemCfg.setUserName(context, null != user.name ? user.name.toString() : "");
                        SystemCfg.setDepartment(context, null != user.orgName ? user.orgName.toString() : "");
                        SystemCfg.setDepartmentID(context, null != user.orgId ? user.orgId.toString() : "");
                        SystemCfg.setIMEI(context, null != user.imei ? user.imei.toString() : "");
                        SystemCfg.setWhitePwd(context, main.getPassword());
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