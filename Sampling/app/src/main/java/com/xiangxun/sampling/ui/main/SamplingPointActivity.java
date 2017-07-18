package com.xiangxun.sampling.ui.main;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.TextView;

import com.xiangxun.sampling.R;
import com.xiangxun.sampling.base.BaseActivity;
import com.xiangxun.sampling.bean.PlannningData.Pointly;
import com.xiangxun.sampling.bean.PlannningData.ResultPointData;
import com.xiangxun.sampling.bean.PlannningData.Scheme;
import com.xiangxun.sampling.binder.ContentBinder;
import com.xiangxun.sampling.binder.ViewsBinder;
import com.xiangxun.sampling.common.SharePreferHelp;
import com.xiangxun.sampling.common.ToastApp;
import com.xiangxun.sampling.common.dlog.DLog;
import com.xiangxun.sampling.ui.adapter.PointAdapter;
import com.xiangxun.sampling.ui.biz.SamplingPointListener.SamplingPointInterface;
import com.xiangxun.sampling.ui.presenter.SamplingPointPresenter;
import com.xiangxun.sampling.widget.header.TitleView;
import com.xiangxun.sampling.widget.xlistView.ItemClickListenter;

import java.util.List;

import se.emilsjolander.stickylistheaders.StickyListHeadersListView;

/**
 * Created by Zhangyuhui/Darly on 2017/7/6.
 * Copyright by [Zhangyuhui/Darly]
 * ©2017 XunXiang.Company. All rights reserved.
 *
 * @TODO:采样计划点击到某个计划页面，展示详细点位信息。
 */
@ContentBinder(R.layout.activity_sampling_point)
public class SamplingPointActivity extends BaseActivity implements SamplingPointInterface {

    @ViewsBinder(R.id.id_point_title)
    private TitleView titleView;

    @ViewsBinder(R.id.id_point_wlist)
    private StickyListHeadersListView wlist;
    @ViewsBinder(R.id.id_point_text)
    private TextView textView;
    private Scheme planning;
    private PointAdapter adapter;

    private List<Pointly> data;
    private boolean isSence;

    private SamplingPointPresenter presenter;

    @Override
    protected void initView(Bundle savedInstanceState) {
        planning = (Scheme) getIntent().getSerializableExtra("Scheme");
        isSence = getIntent().getBooleanExtra("SENCE", false);
        if (planning == null) {
            ToastApp.showToast("传递参数错误");
            return;
        }
        titleView.setTitle(planning.name);
        //在这里根据方案ID请求点位信息。
        presenter = new SamplingPointPresenter(this);

    }

    @Override
    protected void loadData() {
        Object ob = SharePreferHelp.getValue(planning.id);
        if (data == null) {
            if (ob != null) {
                data = ((ResultPointData) ob).result;
            }
        }
        adapter = new PointAdapter(data, R.layout.item_planning_list, this, isSence);
        wlist.setAdapter(adapter);
        presenter.point(planning.id, ob == null ? null : ((ResultPointData) ob).resTime);
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
            titleView.setRightViewRightTextOneListener("新增", new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //新增点位逻辑页面
                    Intent intent = new Intent(SamplingPointActivity.this, AddNewPointPlanningActivity.class);
                    intent.putExtra("Scheme", planning);//新增时将方案传递过去。
                    startActivity(intent);
                }
            });
        }
        wlist.setOnItemClickListener(new ItemClickListenter() {
            @Override
            public void NoDoubleItemClickListener(AdapterView<?> parent, View view, int position, long id) {
                DLog.i("onItemClick--" + position);
                Pointly point = (Pointly) parent.getItemAtPosition(position);
                for (Pointly po : data) {
                    if (point.data.id.equals(po.data.id)) {
                        po.data.setUserSee(true);
                        break;
                    }
                }
                adapter.setData(data);
                if (isSence) {
                    //到现场采集页面.
                    DLog.i("到现场采集页面--" + position);
                    Intent intent = new Intent(SamplingPointActivity.this, SenceActivity.class);
                    intent.putExtra("Scheme", planning);
                    intent.putExtra("SamplingKey", point);
                    startActivity(intent);
                } else {
                    Intent intent = new Intent(SamplingPointActivity.this, AddNewPointPlanningActivity.class);
                    intent.putExtra("Scheme", planning);
                    intent.putExtra("SamplingKey", point);
                    startActivity(intent);
                }
            }
        });
    }

    //点位解析回调
    @Override
    public void onLoginSuccess(List<Pointly> info) {
        data = info;
        if (data != null && data.size() > 0) {
            wlist.setVisibility(View.VISIBLE);
            textView.setVisibility(View.GONE);
            adapter.setData(data);
        } else {
            wlist.setVisibility(View.GONE);
            textView.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void onLoginFailed() {
        Object s = SharePreferHelp.getValue(planning.id);
        if (s != null) {
            data = ((ResultPointData) s).result;
            adapter.setData(data);
        } else {
            wlist.setVisibility(View.GONE);
            textView.setVisibility(View.VISIBLE);
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
