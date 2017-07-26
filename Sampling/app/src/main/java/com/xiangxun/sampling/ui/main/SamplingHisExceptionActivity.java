package com.xiangxun.sampling.ui.main;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.TextView;

import com.xiangxun.sampling.R;
import com.xiangxun.sampling.base.BaseActivity;
import com.xiangxun.sampling.bean.HisExceptionInfo.HisException;
import com.xiangxun.sampling.bean.Index;
import com.xiangxun.sampling.bean.SimplingTarget;
import com.xiangxun.sampling.binder.ContentBinder;
import com.xiangxun.sampling.binder.ViewsBinder;
import com.xiangxun.sampling.ui.adapter.SamplingHisExceptionAdapter;
import com.xiangxun.sampling.ui.biz.HisExceptionListener.HisExceptionInterface;
import com.xiangxun.sampling.ui.presenter.HisExceptionPresenter;
import com.xiangxun.sampling.widget.header.TitleView;
import com.xiangxun.sampling.widget.xlistView.ItemClickListenter;

import java.util.List;

import se.emilsjolander.stickylistheaders.StickyListHeadersListView;

/**
 * Created by Zhangyuhui/Darly on 2017/7/6.
 * Copyright by [Zhangyuhui/Darly]
 * ©2017 XunXiang.Company. All rights reserved.
 *
 * @TODO:历史地块异常查询各个功能
 */
@ContentBinder(R.layout.activity_sampling_planning)
public class SamplingHisExceptionActivity extends BaseActivity implements HisExceptionInterface {
    @ViewsBinder(R.id.id_planning_title)
    private TitleView titleView;
    @ViewsBinder(R.id.id_planning_wlist)
    private StickyListHeadersListView xlist;
    @ViewsBinder(R.id.id_planning_text)
    private TextView textView;

    private SamplingHisExceptionAdapter adapter;

    private List<HisException> data;

    private HisExceptionPresenter presenter;


    @Override
    protected void initView(Bundle savedInstanceState) {
        titleView.setTitle("历史地块异常");
        //直接请求地块异常信息
        presenter = new HisExceptionPresenter(this);
        presenter.hisList();
    }

    @Override
    protected void loadData() {
        adapter = new SamplingHisExceptionAdapter(data, R.layout.item_planning_list, this);
        xlist.setAdapter(adapter);
    }

    @Override
    protected void initListener() {
        titleView.setLeftBackOneListener(R.mipmap.ic_back_title, new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {
                onBackPressed();
            }
        });

        xlist.setOnItemClickListener(new ItemClickListenter() {
            @Override
            public void NoDoubleItemClickListener(AdapterView<?> parent, View view, int position, long id) {
                //点击跳转到地块详情页面，只是展示效果。不能进行任何操作
                HisException e = (HisException) parent.getItemAtPosition(position);
                if (e != null) {
                    Intent intent = new Intent(SamplingHisExceptionActivity.this, SamplingHisExceptionPageActivity.class);
                    intent.putExtra("id", e.id);
                    startActivity(intent);
                }
            }
        });
    }

    @Override
    public void onDateSuccess(List<HisException> result) {
        data = result;
        if (data != null && data.size() > 1) {
            xlist.setVisibility(View.VISIBLE);
            adapter.setData(data);
            textView.setVisibility(View.GONE);
        } else {
            xlist.setVisibility(View.GONE);
            textView.setVisibility(View.VISIBLE);
            textView.setText("没有指地块异常");
        }
    }

    @Override
    public void onDateFailed(String info) {
        if (data != null && data.size() > 1) {
            xlist.setVisibility(View.VISIBLE);
            adapter.setData(data);
            textView.setVisibility(View.GONE);
        } else {
            xlist.setVisibility(View.GONE);
            textView.setVisibility(View.VISIBLE);
            textView.setText("没有指地块异常");
        }
    }

    @Override
    public void setDisableClick() {

    }

    @Override
    public void setEnableClick() {

    }
}
