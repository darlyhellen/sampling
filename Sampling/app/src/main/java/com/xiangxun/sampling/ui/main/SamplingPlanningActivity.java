package com.xiangxun.sampling.ui.main;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.TextView;

import com.xiangxun.sampling.R;
import com.xiangxun.sampling.base.BaseActivity;
import com.xiangxun.sampling.binder.ContentBinder;
import com.xiangxun.sampling.binder.ViewsBinder;
import com.xiangxun.sampling.common.dlog.DLog;
import com.xiangxun.sampling.ui.adapter.PlanningAdapter;
import com.xiangxun.sampling.widget.header.TitleView;
import com.xiangxun.sampling.widget.listview.WholeListView;
import com.xiangxun.sampling.widget.xlistView.ItemClickListenter;
import com.xiangxun.sampling.widget.xlistView.XListView;

import java.util.List;

/**
 * Created by Zhangyuhui/Darly on 2017/7/6.
 * Copyright by [Zhangyuhui/Darly]
 * ©2017 XunXiang.Company. All rights reserved.
 *
 * @TODO:采样计划展示表格,逻辑为：服务端通过推送，下发到所有手机端，手机端根据推送资料。获取到任务列表，这里进行任务列表展示。
 */
@ContentBinder(R.layout.activity_sampling_planning)
public class SamplingPlanningActivity extends BaseActivity {

    @ViewsBinder(R.id.id_planning_title)
    private TitleView titleView;

    @ViewsBinder(R.id.id_planning_wlist)
    private WholeListView wlist;
    @ViewsBinder(R.id.id_planning_text)
    private TextView textView;

    private PlanningAdapter adapter;

    @Override
    protected void initView(Bundle savedInstanceState) {
        titleView.setTitle("采样计划");
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

        wlist.setOnItemClickListener(new ItemClickListenter() {
            @Override
            public void NoDoubleItemClickListener(AdapterView<?> parent, View view, int position, long id) {
                DLog.i("onItemClick--" + position);
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
