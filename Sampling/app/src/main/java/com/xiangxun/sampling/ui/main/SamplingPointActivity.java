package com.xiangxun.sampling.ui.main;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import com.xiangxun.sampling.R;
import com.xiangxun.sampling.base.BaseActivity;
import com.xiangxun.sampling.bean.SamplingPlanning;
import com.xiangxun.sampling.bean.SamplingPoint;
import com.xiangxun.sampling.binder.ContentBinder;
import com.xiangxun.sampling.binder.ViewsBinder;
import com.xiangxun.sampling.common.ToastApp;
import com.xiangxun.sampling.common.dlog.DLog;
import com.xiangxun.sampling.ui.adapter.PointAdapter;
import com.xiangxun.sampling.widget.header.TitleView;
import com.xiangxun.sampling.widget.xlistView.ItemClickListenter;

import java.util.List;

/**
 * Created by Zhangyuhui/Darly on 2017/7/6.
 * Copyright by [Zhangyuhui/Darly]
 * ©2017 XunXiang.Company. All rights reserved.
 *
 * @TODO:采样计划点击到某个计划页面，展示详细点位信息。
 */
@ContentBinder(R.layout.activity_sampling_point)
public class SamplingPointActivity extends BaseActivity {

    @ViewsBinder(R.id.id_point_title)
    private TitleView titleView;

    @ViewsBinder(R.id.id_point_wlist)
    private ListView wlist;
    @ViewsBinder(R.id.id_point_text)
    private TextView textView;
    private SamplingPlanning planning;
    private PointAdapter adapter;

    private List<SamplingPoint> data;
    private boolean isSence;

    @Override
    protected void initView(Bundle savedInstanceState) {
        planning = (SamplingPlanning) getIntent().getSerializableExtra("SamplingPlanning");
        isSence = getIntent().getBooleanExtra("SENCE", false);
        if (planning == null) {
            ToastApp.showToast("传递参数错误");
            return;
        }
        titleView.setTitle(planning.getTitle());
    }

    @Override
    protected void loadData() {
        if (data == null){
            data = planning.getPoints();
            data.add(0,new SamplingPoint());
        }
        adapter = new PointAdapter(data, R.layout.item_planning_list, this, isSence);
        wlist.setAdapter(adapter);
    }

    @Override
    protected void initListener() {
        titleView.setLeftBackOneListener(R.mipmap.ic_back_title, new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {
                onBackPressed();
            }
        });
        if (!isSence) {
            titleView.setRightViewRightOneListener(R.mipmap.newfile, new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //新增点位逻辑页面
                    Intent intent = new Intent(SamplingPointActivity.this, AddNewPointPlanningActivity.class);
                    SamplingPlanning p = new SamplingPlanning();
                    p.setId(planning.getId());
                    p.setDepate(planning.getDepate());
                    p.setTitle(planning.getTitle());
                    p.setPlace(planning.getPlace());
                    intent.putExtra("SamplingPlanning", p);
                    startActivity(intent);
                }
            });
        }
        wlist.setOnItemClickListener(new ItemClickListenter() {
            @Override
            public void NoDoubleItemClickListener(AdapterView<?> parent, View view, int position, long id) {
                DLog.i("onItemClick--" + position);
                if (position != 0) {
                    SamplingPoint point = (SamplingPoint) parent.getItemAtPosition(position);
                    if (isSence) {
                        //到现场采集页面.
                        DLog.i("到现场采集页面--" + position);
                    } else {
                        Intent intent = new Intent(SamplingPointActivity.this, AddNewPointPlanningActivity.class);
                        SamplingPlanning p = new SamplingPlanning();
                        p.setId(planning.getId());
                        p.setDepate(planning.getDepate());
                        p.setTitle(planning.getTitle());
                        p.setPlace(planning.getPlace());
                        intent.putExtra("SamplingPlanning", p);
                        intent.putExtra("SamplingPoint", point);
                        startActivity(intent);
                    }
                }
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
