package com.xiangxun.sampling.ui.setting;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;

import com.xiangxun.sampling.R;
import com.xiangxun.sampling.base.BaseActivity;
import com.xiangxun.sampling.base.SystemCfg;
import com.xiangxun.sampling.binder.ContentBinder;
import com.xiangxun.sampling.binder.ViewsBinder;
import com.xiangxun.sampling.common.dlog.DLog;
import com.xiangxun.sampling.common.image.ImageLoaderUtil;
import com.xiangxun.sampling.widget.groupview.DetailView;
import com.xiangxun.sampling.widget.header.TitleView;

@ContentBinder(R.layout.activity_user_infomation)
public class UserInformationActivity extends BaseActivity {
    @ViewsBinder(R.id.id_userinfo_title)
    private TitleView titleView;
    @ViewsBinder(R.id.id_userinfo_account)
    private DetailView account;
    @ViewsBinder(R.id.id_userinfo_name)
    private DetailView name;
    @ViewsBinder(R.id.id_userinfo_tel)
    private DetailView tel;
    @ViewsBinder(R.id.id_userinfo_dept)
    private DetailView dept;
    @ViewsBinder(R.id.id_userinfo_photo)
    private ImageView photo;


    @Override
    protected void initView(Bundle savedInstanceState) {
        titleView.setTitle("个人信息");
    }

    @Override
    protected void loadData() {

        account.isEdit(false);
        account.setInfo("用户名：", SystemCfg.getAccount(this),"");
        name.isEdit(true);
        name.setInfo("姓名：", SystemCfg.getUserName(this),"");
        tel.isEdit(false);
        tel.setInfo("手机号：", SystemCfg.getIMEI(this),"");
        dept.isEdit(false);
        dept.setInfo("所属部门：", SystemCfg.getDepartment(this),"");

        if (!TextUtils.isEmpty(SystemCfg.getUserImage(this))) {
            ImageLoaderUtil.getInstance().loadImage(SystemCfg.getUserImage(this), photo);
        }
    }

    @Override
    public void initListener() {

        titleView.setRightViewLeftOneListener(R.mipmap.ic_updata_userinfo, new OnClickListener() {
            @Override
            public void onClick(View v) {
                //上传用户信息。
            }
        });
        titleView.setLeftBackOneListener(R.mipmap.ic_back_title, new OnClickListener() {

            @Override
            public void onClick(View arg0) {
                onBackPressed();
            }
        });
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
