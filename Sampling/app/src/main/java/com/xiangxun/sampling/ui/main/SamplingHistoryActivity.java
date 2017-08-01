package com.xiangxun.sampling.ui.main;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.TextView;

import com.xiangxun.sampling.R;
import com.xiangxun.sampling.base.BaseActivity;
import com.xiangxun.sampling.bean.PlannningData;
import com.xiangxun.sampling.binder.ContentBinder;
import com.xiangxun.sampling.binder.ViewsBinder;
import com.xiangxun.sampling.common.SharePreferHelp;
import com.xiangxun.sampling.common.dlog.DLog;
import com.xiangxun.sampling.ui.SearchWorkOrderDialogFragment;
import com.xiangxun.sampling.ui.SearchWorkOrderDialogFragment.SearchListener;
import com.xiangxun.sampling.ui.adapter.StickyAdapter;
import com.xiangxun.sampling.ui.biz.SamplingHistoryListener;
import com.xiangxun.sampling.ui.biz.SamplingHistoryListener.SamplingHistoryInterface;
import com.xiangxun.sampling.ui.presenter.SamplingHistoryPresenter;
import com.xiangxun.sampling.widget.header.TitleView;
import com.xiangxun.sampling.widget.xlistView.ItemClickListenter;

import java.util.List;

import se.emilsjolander.stickylistheaders.StickyListHeadersListView;

/**
 * Created by Zhangyuhui/Darly on 2017/7/6.
 * Copyright by [Zhangyuhui/Darly]
 * ©2017 XunXiang.Company. All rights reserved.
 *
 * @TODO:历史采样展示历史数据
 */
@ContentBinder(R.layout.activity_sampling_planning)
public class SamplingHistoryActivity extends BaseActivity implements SamplingHistoryInterface, SearchListener {
    @ViewsBinder(R.id.id_planning_title)
    private TitleView titleView;


    @ViewsBinder(R.id.id_planning_wlist)
    private StickyListHeadersListView wlist;
    @ViewsBinder(R.id.id_planning_text)
    private TextView textView;
    private List<PlannningData.Scheme> data;
    private StickyAdapter adapter;

    private SamplingHistoryPresenter presenter;

    private String sampleName;
    private String sampleTarget;

    @Override
    protected void initView(Bundle savedInstanceState) {
        titleView.setTitle("历史采样");
        presenter = new SamplingHistoryPresenter(this);
        presenter.getHistory();
    }

    @Override
    protected void loadData() {
        adapter = new StickyAdapter(null, R.layout.item_planning_list, this, false);
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
        titleView.setRightViewRightOneListener(R.mipmap.ic_title_search, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //历史记录中的选择
                SearchWorkOrderDialogFragment dialog = new SearchWorkOrderDialogFragment();
                Bundle bundle = new Bundle();
                bundle.putString("CLASS", "SamplingHistoryActivity");
                bundle.putString("SampleName", sampleName);
                bundle.putString("Target", sampleTarget);
                dialog.setArguments(bundle);
                dialog.show(getFragmentManager(), "SearchWorkOrderDialogFragment");
            }
        });
        wlist.setOnItemClickListener(new ItemClickListenter() {
            @Override
            public void NoDoubleItemClickListener(AdapterView<?> parent, View view, int position, long id) {
                PlannningData.Scheme planning = (PlannningData.Scheme) parent.getItemAtPosition(position);
                Intent intent = new Intent(SamplingHistoryActivity.this, SamplingHisPointActivity.class);
                intent.putExtra("Scheme", planning);//传递过去方案对象
                startActivity(intent);
            }
        });
    }


    @Override
    public void onLoginSuccess(List<PlannningData.Scheme> info) {
        data = info;
        if (data != null && data.size() > 0) {
            wlist.setVisibility(View.VISIBLE);
            textView.setVisibility(View.GONE);
            adapter.setData(data);
        } else {
            wlist.setVisibility(View.GONE);
            textView.setVisibility(View.VISIBLE);
            textView.setText("没有已完成的任务");
        }
    }

    @Override
    public void onLoginFailed() {
        wlist.setVisibility(View.GONE);
        textView.setVisibility(View.VISIBLE);
        textView.setText("没有已完成的任务");
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
    public void findParamers(String sampleName, String target, String over) {

    }
}
