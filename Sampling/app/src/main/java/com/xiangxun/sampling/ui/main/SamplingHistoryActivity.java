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
 * @TODO:历史采样展示历史数据
 */
@ContentBinder(R.layout.activity_sampling_planning)
public class SamplingHistoryActivity extends BaseActivity {
    @ViewsBinder(R.id.id_planning_title)
    private TitleView titleView;

    @Override
    protected void initView(Bundle savedInstanceState) {
        titleView.setTitle("历史采样");
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


}
