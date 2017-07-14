package com.xiangxun.sampling.ui.main;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.TextView;

import com.xiangxun.sampling.R;
import com.xiangxun.sampling.base.BaseActivity;
import com.xiangxun.sampling.bean.SamplingKey;
import com.xiangxun.sampling.bean.SamplingPlanning;
import com.xiangxun.sampling.binder.ContentBinder;
import com.xiangxun.sampling.binder.ViewsBinder;
import com.xiangxun.sampling.common.SharePreferHelp;
import com.xiangxun.sampling.common.dlog.DLog;
import com.xiangxun.sampling.ui.StaticListener;
import com.xiangxun.sampling.ui.StaticListener.RefreshMainUIListener;
import com.xiangxun.sampling.ui.adapter.StickyAdapter;
import com.xiangxun.sampling.widget.header.TitleView;
import com.xiangxun.sampling.widget.xlistView.ItemClickListenter;

import java.util.List;

import se.emilsjolander.stickylistheaders.StickyListHeadersListView;

/**
 * Created by Zhangyuhui/Darly on 2017/7/6.
 * Copyright by [Zhangyuhui/Darly]
 * ©2017 XunXiang.Company. All rights reserved.
 *
 * @TODO:采样计划展示表格,逻辑为：服务端通过推送，下发到所有手机端，手机端根据推送资料。获取到任务列表，这里进行任务列表展示。
 */
@ContentBinder(R.layout.activity_sampling_planning)
public class SamplingPlanningActivity extends BaseActivity implements RefreshMainUIListener {

    @ViewsBinder(R.id.id_planning_title)
    private TitleView titleView;

    @ViewsBinder(R.id.id_planning_wlist)
    private StickyListHeadersListView wlist;
    @ViewsBinder(R.id.id_planning_text)
    private TextView textView;
    private List<SamplingPlanning> data;
    private StickyAdapter adapter;


    @Override
    protected void initView(Bundle savedInstanceState) {
        titleView.setTitle("采样计划");
    }

    @Override
    protected void loadData() {
        adapter = new StickyAdapter(null, R.layout.item_planning_list, this, false);
        wlist.setAdapter(adapter);
        StaticListener.getInstance().setRefreshMainUIListener(this);
        if (data == null) {
            Object s = SharePreferHelp.getValue("SamplingPlanning");
            if (s == null) {
                data = StaticListener.findData();
                SharePreferHelp.putValue("SamplingPlanning", data);
            } else {
                data = (List<SamplingPlanning>) s;
                for (SamplingPlanning p : data) {
                    for (SamplingKey key : p.getPoints()) {
                        if ("102".equals(key.getId())) {
                            key.getPoint().setName("我的歌神啊");
                            key.getPoint().setSamply(false);
                            key.getPoint().setLatitude(111);
                            key.getPoint().setLongitude(222);
                        }
                    }
                }
            }
        }
        StaticListener.getInstance().getRefreshMainUIListener().refreshMainUI(data);
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
                SamplingPlanning planning = (SamplingPlanning) parent.getItemAtPosition(position);
                for (SamplingPlanning pl : data) {
                    if (planning.getId().equals(pl.getId())) {
                        pl.setUserSee(true);
                        break;
                    }
                }
                adapter.setData(data);
                Intent intent = new Intent(SamplingPlanningActivity.this, SamplingPointActivity.class);
                intent.putExtra("SamplingPlanning", planning);
                intent.putExtra("SENCE", false);
                startActivity(intent);
            }
        });
    }

    @Override
    public void refreshMainUI(List<SamplingPlanning> planningList) {
        if (planningList != null && planningList.size() > 0) {
            wlist.setVisibility(View.VISIBLE);
            textView.setVisibility(View.GONE);
            adapter.setData(planningList);
        } else {
            wlist.setVisibility(View.GONE);
            textView.setVisibility(View.VISIBLE);
        }
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
