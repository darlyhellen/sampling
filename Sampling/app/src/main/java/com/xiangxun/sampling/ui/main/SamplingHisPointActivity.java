package com.xiangxun.sampling.ui.main;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.TextView;

import com.xiangxun.sampling.R;
import com.xiangxun.sampling.base.BaseActivity;
import com.xiangxun.sampling.bean.HisPlanningData;
import com.xiangxun.sampling.bean.PlannningData.Pointly;
import com.xiangxun.sampling.bean.PlannningData.Scheme;
import com.xiangxun.sampling.binder.ContentBinder;
import com.xiangxun.sampling.binder.ViewsBinder;
import com.xiangxun.sampling.common.ToastApp;
import com.xiangxun.sampling.common.dlog.DLog;
import com.xiangxun.sampling.ui.adapter.HistoryPointAdapter;
import com.xiangxun.sampling.ui.biz.SamplingPointHisListener.SamplingPointHisInterface;
import com.xiangxun.sampling.ui.presenter.SamplingPointHisPresenter;
import com.xiangxun.sampling.widget.header.TitleView;
import com.xiangxun.sampling.widget.xlistView.ItemClickListenter;

import java.util.List;

import se.emilsjolander.stickylistheaders.StickyListHeadersListView;

/**
 * Created by Zhangyuhui/Darly on 2017/7/6.
 * Copyright by [Zhangyuhui/Darly]
 * ©2017 XunXiang.Company. All rights reserved.
 *
 * @TODO:历史点位信息展示,根据历史方案id获取到点位信息。
 */
@ContentBinder(R.layout.activity_sampling_point)
public class SamplingHisPointActivity extends BaseActivity implements SamplingPointHisInterface {

    @ViewsBinder(R.id.id_point_title)
    private TitleView titleView;

    @ViewsBinder(R.id.id_point_wlist)
    private StickyListHeadersListView wlist;
    @ViewsBinder(R.id.id_point_text)
    private TextView textView;
    private Scheme planning;
    private HistoryPointAdapter adapter;

    private List<HisPlanningData.HisPoint> data;

    private SamplingPointHisPresenter presenter;

    @Override
    protected void initView(Bundle savedInstanceState) {
        planning = (Scheme) getIntent().getSerializableExtra("Scheme");
        if (planning == null) {
            ToastApp.showToast("传递参数错误");
            return;
        }
        titleView.setTitle(planning.missionName);
        //在这里根据方案ID请求点位信息。
        presenter = new SamplingPointHisPresenter(this);
        presenter.point(planning.missionId,planning.sampleCode);
    }

    @Override
    protected void loadData() {
        adapter = new HistoryPointAdapter(data, R.layout.item_planning_list, this);
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
        wlist.setOnItemClickListener(new ItemClickListenter() {
            @Override
            public void NoDoubleItemClickListener(AdapterView<?> parent, View view, int position, long id) {
                //到现场采集页面.
                DLog.i("到现场采集页面--" + position);

                HisPlanningData.HisPoint pointly = (HisPlanningData.HisPoint) parent.getItemAtPosition(position);
                if (pointly!=null){
                    Intent intent = new Intent(SamplingHisPointActivity.this, HisSenceActivity.class);
                    intent.putExtra("ID", pointly.id);
                    intent.putExtra("missionId", pointly.missionId);
                    intent.putExtra("tableName",pointly.tableName);
                    startActivity(intent);
                }


            }
        });
    }

    //点位解析回调
    @Override
    public void onLoginSuccess(List<HisPlanningData.HisPoint> info) {
        data = info;
        if (data != null && data.size() > 0) {
            adapter.setData(data);
            wlist.setVisibility(View.VISIBLE);
            textView.setVisibility(View.GONE);
        } else {
            wlist.setVisibility(View.GONE);
            textView.setVisibility(View.VISIBLE);
            textView.setText("没有已完成的点位信息");
        }
    }

    @Override
    public void onLoginFailed() {
        wlist.setVisibility(View.GONE);
        textView.setVisibility(View.VISIBLE);
        textView.setText("没有已完成的点位信息");
    }

    @Override
    public void setDisableClick() {

    }

    @Override
    public void setEnableClick() {

    }

}
