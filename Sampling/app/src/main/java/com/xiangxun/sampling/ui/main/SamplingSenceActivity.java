package com.xiangxun.sampling.ui.main;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ExpandableListView;
import android.widget.TextView;

import com.xiangxun.sampling.R;
import com.xiangxun.sampling.base.BaseActivity;
import com.xiangxun.sampling.bean.PlannningData.Scheme;
import com.xiangxun.sampling.bean.SamplingSenceGroup;
import com.xiangxun.sampling.binder.ContentBinder;
import com.xiangxun.sampling.binder.ViewsBinder;
import com.xiangxun.sampling.common.dlog.DLog;
import com.xiangxun.sampling.ui.adapter.SamplingSenceAdapter;
import com.xiangxun.sampling.ui.biz.SamplingPlanningListener.SamplingPlanningInterface;
import com.xiangxun.sampling.ui.presenter.SamplingPlanningPresenter;
import com.xiangxun.sampling.widget.header.TitleView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Zhangyuhui/Darly on 2017/7/6.
 * Copyright by [Zhangyuhui/Darly]
 * ©2017 XunXiang.Company. All rights reserved.
 *
 * @TODO:现场采样展示效果
 */
@ContentBinder(R.layout.activity_sence_ex)
public class SamplingSenceActivity extends BaseActivity implements SamplingPlanningInterface {
    @ViewsBinder(R.id.id_sence_title)
    private TitleView titleView;

    @ViewsBinder(R.id.id_sence_wlist)
    private ExpandableListView wlist;
    @ViewsBinder(R.id.id_sence_text)
    private TextView textView;
    private List<SamplingSenceGroup.SenceGroup> data;
    private SamplingSenceAdapter adapter;

    private SamplingPlanningPresenter presenter;

    @Override
    protected void initView(Bundle savedInstanceState) {
        titleView.setTitle("现场采样");
        //在这里进行方案列表请求。获取到的信息，进行缓存。对修改的信息进行处理操作。
        presenter = new SamplingPlanningPresenter(this);
    }

    @Override
    protected void loadData() {
        adapter = new SamplingSenceAdapter(this, data);
        wlist.setAdapter(adapter);
       // presenter.planning(null/*s == null ? null : ((ResultData) s).resTime*/);
        presenter.findPlanning();
    }

    @Override
    protected void initListener() {
        titleView.setLeftBackOneListener(R.mipmap.ic_back_title, new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {
                onBackPressed();
            }
        });
        //重写OnGroupClickListener，实现当展开时，ExpandableListView不自动滚动
        wlist.setOnGroupClickListener(new ExpandableListView.OnGroupClickListener() {
            @Override
            public boolean onGroupClick(ExpandableListView parent, View v, int groupPosition, long id) {
                if (parent.isGroupExpanded(groupPosition)) {
                    parent.collapseGroup(groupPosition);
                } else {
                    //第二个参数false表示展开时是否触发默认滚动动画
                    parent.expandGroup(groupPosition, false);
                }
                return true;
            }
        });
        wlist.setOnChildClickListener(new ExpandableListView.OnChildClickListener() {
            @Override
            public boolean onChildClick(ExpandableListView parent, View v, int groupPosition, int childPosition, long id) {
                Scheme data = (Scheme) adapter.getChild(groupPosition, childPosition);
                DLog.i(getClass().getSimpleName(), groupPosition + "---" + childPosition + "---" + data);
                //到现场采集页面.
                Intent intent = new Intent(SamplingSenceActivity.this, SenceActivity.class);
                intent.putExtra("Scheme", data);
                startActivityForResult(intent, 1000);
                return false;
            }
        });
    }

    @Override
    public void onLoginSuccessV1(List<SamplingSenceGroup.SenceGroup> result) {
        data = result;
        if (data != null && data.size() > 0) {
            wlist.setVisibility(View.VISIBLE);
            textView.setVisibility(View.GONE);
            adapter.setData(data);
        } else {
            wlist.setVisibility(View.GONE);
            textView.setVisibility(View.VISIBLE);
            textView.setText("没有采样的计划");
        }
    }

    //现场采样中获取采样计划列表
    @Override
    public void onLoginSuccess(List<Scheme> info) {
        //data = info;
        data = new ArrayList<SamplingSenceGroup.SenceGroup>();

        SamplingSenceGroup.SenceGroup daqi = new SamplingSenceGroup().new SenceGroup();
        daqi.regType = "大气采样";
        daqi.result = info;
        data.add(daqi);
        SamplingSenceGroup.SenceGroup tur = new SamplingSenceGroup().new SenceGroup();
        tur.regType = "土壤采样";
        tur.result = info;
        data.add(tur);
        if (data != null && data.size() > 0) {
            wlist.setVisibility(View.VISIBLE);
            textView.setVisibility(View.GONE);
            adapter.setData(data);
        } else {
            wlist.setVisibility(View.GONE);
            textView.setVisibility(View.VISIBLE);
            textView.setText("没有采样的计划");
        }
    }

    @Override
    public void onLoginFailed() {
        wlist.setVisibility(View.GONE);
        textView.setVisibility(View.VISIBLE);
        textView.setText("没有采样的计划");
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
