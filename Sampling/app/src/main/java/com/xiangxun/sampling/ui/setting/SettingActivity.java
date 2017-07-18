package com.xiangxun.sampling.ui.setting;

import android.os.Bundle;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextUtils;
import android.text.style.ForegroundColorSpan;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.xiangxun.sampling.R;
import com.xiangxun.sampling.base.BaseActivity;
import com.xiangxun.sampling.base.SystemCfg;
import com.xiangxun.sampling.binder.ContentBinder;
import com.xiangxun.sampling.binder.ViewsBinder;
import com.xiangxun.sampling.common.dlog.DLog;
import com.xiangxun.sampling.common.image.ImageLoaderUtil;
import com.xiangxun.sampling.widget.header.TitleView;

/**
 * Created by Zhangyuhui/Darly on 2017/7/5.
 * Copyright by [Zhangyuhui/Darly]
 * ©2017 XunXiang.Company. All rights reserved.
 *
 * @TODO: 用户设置页面。
 */
@ContentBinder(R.layout.activity_set)
public class SettingActivity extends BaseActivity {

    @ViewsBinder(R.id.id_set_title)
    private TitleView title;
    @ViewsBinder(R.id.account)
    private TextView mTvAccount;
    @ViewsBinder(R.id.realname)
    private TextView mTvName;
    @ViewsBinder(R.id.region)
    private TextView mTvDepartment;
    @ViewsBinder(R.id.phone)
    private TextView mTvPhone;
    @ViewsBinder(R.id.user_photo)
    private ImageView user_photo;
    @ViewsBinder(R.id.id_user_info)
    private RelativeLayout user_info;


    @Override
    protected void initView(Bundle savedInstanceState) {
        title.setTitle(R.string.st_set_title);
        title.removeBackground();
        user_photo.setLayoutParams(new LinearLayout.LayoutParams(SystemCfg.getWidth(this) / 5, SystemCfg.getWidth(this) / 5));
        initFragments(SettingFragment.class, R.id.id_set_frame);

    }

    @Override
    protected void loadData() {
        if (!TextUtils.isEmpty(SystemCfg.getUserImage(this))) {
            ImageLoaderUtil.getInstance().loadImage(SystemCfg.getUserImage(this), user_photo);
        } else {
            user_photo.setImageResource(R.mipmap.ic_user);
        }
        String str;
        SpannableString ss;
        int color = getResources().getColor(R.color.white);

        str = String.format(getResources().getString(R.string.police_account), SystemCfg.getAccount(this));
        ss = new SpannableString(str);
        ss.setSpan(new ForegroundColorSpan(color), 4, str.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        mTvAccount.setText(ss);

        str = String.format("姓    名：" + SystemCfg.getUserName(this));
        ss = new SpannableString(str);
        ss.setSpan(new ForegroundColorSpan(color), 7, str.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        mTvName.setText(ss);

        str = String.format(getResources().getString(R.string.police_department), SystemCfg.getDepartment(this));
        ss = new SpannableString(str);
        ss.setSpan(new ForegroundColorSpan(color), 5, str.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        mTvDepartment.setText(ss);

        mTvPhone.setText(ss);
    }

    @Override
    protected void initListener() {
        user_info.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // startActivity(new Intent(SettingActivity.this, UserInformationActivity.class));
            }
        });
        title.setLeftBackOneListener(R.mipmap.ic_back_title, new View.OnClickListener() {
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
