package com.xiangxun.sampling.ui.main;

import android.os.Bundle;
import android.view.View;

import com.xiangxun.sampling.R;
import com.xiangxun.sampling.base.BaseActivity;
import com.xiangxun.sampling.binder.ContentBinder;
import com.xiangxun.sampling.binder.ViewsBinder;
import com.xiangxun.sampling.common.dlog.DLog;
import com.xiangxun.sampling.widget.header.TitleView;

/**
 * Created by Zhangyuhui/Darly on 2017/7/6.
 * Copyright by [Zhangyuhui/Darly]
 * ©2017 XunXiang.Company. All rights reserved.
 *
 * @TODO:历史地块异常查询各个功能
 */
@ContentBinder(R.layout.activity_sampling_exception)
public class SamplingHisExceptionActivity extends BaseActivity {
    @ViewsBinder(R.id.id_exception_title)
    private TitleView titleView;


    @Override
    protected void initView(Bundle savedInstanceState) {
        titleView.setTitle("地块异常上报");
    }

    @Override
    protected void loadData() {

    }

    @Override
    protected void initListener() {
        titleView.setLeftBackOneListener(R.mipmap.ic_back_title, new View.OnClickListener() {

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
