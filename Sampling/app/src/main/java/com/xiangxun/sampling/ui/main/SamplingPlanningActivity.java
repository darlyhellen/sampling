package com.xiangxun.sampling.ui.main;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.TextView;

import com.xiangxun.sampling.R;
import com.xiangxun.sampling.base.BaseActivity;
import com.xiangxun.sampling.bean.PlannningData.ResultData;
import com.xiangxun.sampling.bean.PlannningData.Scheme;
import com.xiangxun.sampling.bean.SamplingSenceGroup;
import com.xiangxun.sampling.binder.ContentBinder;
import com.xiangxun.sampling.binder.ViewsBinder;
import com.xiangxun.sampling.common.SharePreferHelp;
import com.xiangxun.sampling.common.dlog.DLog;
import com.xiangxun.sampling.ui.adapter.StickyAdapter;
import com.xiangxun.sampling.ui.biz.SamplingPlanningListener.SamplingPlanningInterface;
import com.xiangxun.sampling.ui.presenter.SamplingPlanningPresenter;
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
@ContentBinder(R.layout.activity_sampling_sence)
public class SamplingPlanningActivity extends BaseActivity implements SamplingPlanningInterface {

    @ViewsBinder(R.id.id_sence_title)
    private TitleView titleView;

    @ViewsBinder(R.id.id_sence_wlist)
    private StickyListHeadersListView wlist;
    @ViewsBinder(R.id.id_sence_text)
    private TextView textView;
    private List<Scheme> data;
    private StickyAdapter adapter;

    private SamplingPlanningPresenter presenter;


    @Override
    protected void initView(Bundle savedInstanceState) {
        titleView.setTitle("采样方案");
        //在这里进行方案列表请求。获取到的信息，进行缓存。对修改的信息进行处理操作。
        presenter = new SamplingPlanningPresenter(this);
    }

    @Override
    protected void loadData() {
        adapter = new StickyAdapter(null, R.layout.item_planning_list, this, false);
        wlist.setAdapter(adapter);
        Object s = SharePreferHelp.getValue("ResultData");
        if (data == null) {
            if (s != null) {
                data = ((ResultData) s).result;
                adapter.setData(data);
            }
        }
        presenter.planning(null/*s == null ? null : ((ResultData) s).resTime*/);
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
                Scheme planning = (Scheme) parent.getItemAtPosition(position);
                for (Scheme pl : data) {
                    if (planning.id.equals(pl.id)) {
                        pl.setUserSee(true);
                        break;
                    }
                }
                adapter.setData(data);
                Intent intent = new Intent(SamplingPlanningActivity.this, SamplingPointActivity.class);
                intent.putExtra("Scheme", planning);//传递过去方案对象
                intent.putExtra("SENCE", false);
                startActivity(intent);
            }
        });
    }

    @Override
    public void onLoginSuccessV1(List<SamplingSenceGroup.SenceGroup> result) {

    }

    //实现整个View操作
    @Override
    public void onLoginSuccess(List<Scheme> info) {
        data = info;
        if (data != null && data.size() > 0) {
            wlist.setVisibility(View.VISIBLE);
            textView.setVisibility(View.GONE);
            adapter.setData(data);
        } else {
            wlist.setVisibility(View.GONE);
            textView.setVisibility(View.VISIBLE);
            textView.setText("没有任务信息");
        }
    }

    @Override
    public void onLoginFailed() {
        Object s = SharePreferHelp.getValue("ResultData");
        if (s != null) {
            data = ((ResultData) s).result;
            if (data!=null&&data.size()>0) {
                adapter.setData(data);
                wlist.setVisibility(View.VISIBLE);
                textView.setVisibility(View.GONE);
            }else {
                wlist.setVisibility(View.GONE);
                textView.setVisibility(View.VISIBLE);
                textView.setText("没有任务信息");
            }
        } else {
            wlist.setVisibility(View.GONE);
            textView.setVisibility(View.VISIBLE);
            textView.setText("没有任务信息");
        }
    }

    @Override
    public void end() {

    }

    @Override
    public void setDisableClick() {

    }

    @Override
    public void setEnableClick() {

    }

}
