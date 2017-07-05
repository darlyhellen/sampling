package com.xiangxun.sampling.ui.setting;

import android.os.Bundle;
import android.text.InputType;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

import com.xiangxun.sampling.R;
import com.xiangxun.sampling.base.BaseActivity;
import com.xiangxun.sampling.base.SystemCfg;
import com.xiangxun.sampling.binder.ContentBinder;
import com.xiangxun.sampling.common.ToastApp;
import com.xiangxun.sampling.common.Utils;
import com.xiangxun.sampling.widget.clearedit.ViewEditText;
import com.xiangxun.sampling.widget.dialog.ProgressLoadingDialog;
import com.xiangxun.sampling.widget.header.TitleView;


/**
 * 修改密码
 *
 * @author ChenXiangshi
 * @className Layout_userlogin
 * @date 2015-6-10 上午9:52:07
 */
@ContentBinder(R.layout.activity_change_password)
public class ChangePasswordActivity extends BaseActivity {
    private TitleView titleView;

    private Button btnOk;

    private String userid;
    private String oldpwd;
    private String oldpasswords;
    private String newpasswords;
    private String renewpasswords;
    private ProgressLoadingDialog mDialog;

    private ViewEditText oldPassword;
    private ViewEditText newPassword;
    private ViewEditText reNewPassword;


    @Override
    protected void initView(Bundle savedInstanceState) {
        titleView = (TitleView) findViewById(R.id.tv_comm_title);
        titleView.setTitle(R.string.modifyPwd);
        oldPassword = (ViewEditText) findViewById(R.id.old_password);
        newPassword = (ViewEditText) findViewById(R.id.new_password);
        reNewPassword = (ViewEditText) findViewById(R.id.renew_password);
        btnOk = (Button) this.findViewById(R.id.buttonloginok);
    }

    @Override
    protected void loadData() {
        mDialog = new ProgressLoadingDialog(ChangePasswordActivity.this, "正在修改密码，请稍候...");
        oldpwd = SystemCfg.getWhitePwd(this);
        userid = SystemCfg.getUserId(this);

        oldPassword.setTextViewText("旧密码");
        oldPassword.setEditTextHint("请输入旧密码");
        newPassword.setTextViewText("新密码");
        newPassword.setEditTextHint("请输入新密码");
        reNewPassword.setTextViewText("确认新密码");
        reNewPassword.setEditTextHint("请输入新密码");

        int passwordType = InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD;
        oldPassword.setEditTextInputMode(passwordType);
        newPassword.setEditTextInputMode(passwordType);
        reNewPassword.setEditTextInputMode(passwordType);
    }


    @Override
    public void initListener() {
        btnOk.setOnClickListener(new BtnOkOnClickListener());
        titleView.setLeftBackOneListener(R.mipmap.back_normal, new OnClickListener() {

            @Override
            public void onClick(View arg0) {
                onBackPressed();
            }
        });
    }

    private class BtnOkOnClickListener implements OnClickListener {

        @Override
        public void onClick(View v) {
            Utils.hideSoftInputFromWindow(ChangePasswordActivity.this);

            oldpasswords = oldPassword.getEditTextText();
            newpasswords = newPassword.getEditTextText();
            renewpasswords = reNewPassword.getEditTextText();
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
                newPassword.setEditTextText("");
                reNewPassword.setEditTextText("");
                return;
            }
            if (!oldpasswords.equals(oldpwd)) {
                ToastApp.showToast(R.string.checkPwd);
                oldPassword.setEditTextText("");
                return;
            }

            mDialog.show();
            new Thread() {
                public void run() {
//                    HttpUtil hu = new HttpUtil();
//                    String url = ApiUrl.changePassword(ChangePasswordActivity.this);
//                    System.out.println("url=" + url);
//                    Map<String, String> params = new LinkedHashMap<String, String>();
//                    params.put("oldP", Utils.getCipherText(oldpasswords));
//                    params.put("newP", Utils.getCipherText(newpasswords));
//                    params.put("account", SystemCfg.getAccount(ChangePasswordActivity.this));
//                    params.put("imei", SystemCfg.getIMEI(ChangePasswordActivity.this));
//                    params.put("crc", getCRC(params));
//                    String resStr = "";
//                    try {
//                        resStr = hu.queryStringForPost(url, params, HTTP.UTF_8);
//                        System.out.println("changepwd==" + resStr);
//                        if (null != resStr && resStr.contains("1000") && resStr.contains("修改成功")) {
//                            SystemCfg.setWhitePwd(ChangePasswordActivity.this, newpasswords);
//                            handler.sendEmptyMessage(0);
//                        } else if (resStr.contains("1001")) {
//                            handler.sendEmptyMessage(2);
//                        } else {
//                            handler.sendEmptyMessage(1);
//                        }
//                    } catch (Exception e) {
//                        handler.sendEmptyMessage(1);
//                        e.printStackTrace();
//                    }
                }
            }.start();
        }
    }


//	@SuppressLint("HandlerLeak")
//	private Handler handler = new Handler() {
//		public void handleMessage(android.os.Message msg) {
//			mDialog.dismiss();
//			if (msg.what == 0) {
//				ToastApp.showToast("密码修改成功");
//				finish();
//			} else if (msg.what == 1) {
//				ToastApp.showToast("网络异常");
//			} else {
//				ToastApp.showToast("密码修改失败");
//			}
//		};
//	};

}
